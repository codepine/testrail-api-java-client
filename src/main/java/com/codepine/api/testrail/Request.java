/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Kunal Shah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.codepine.api.testrail;

import com.codepine.api.testrail.internal.FieldModule;
import com.codepine.api.testrail.internal.PlanModule;
import com.codepine.api.testrail.internal.QueryParameterString;
import com.codepine.api.testrail.internal.UnixTimestampModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * TestRail request.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j
public abstract class Request<T> {

    private static final ObjectMapper JSON = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new FieldModule(), new PlanModule(), new UnixTimestampModule());

    private static final ObjectMapper QUERY_PARAM_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new UnixTimestampModule());

    @NonNull
    private final TestRailConfig config;
    @NonNull
    private final Method method;
    @NonNull
    private final String restPath;
    private final Class<? extends T> responseClass;
    private final TypeReference<? extends T> responseType;

    /**
     * @param config
     * @param method
     * @param restPath
     * @param responseClass
     */
    protected Request(TestRailConfig config, Method method, String restPath, @NonNull Class<? extends T> responseClass) {
        this(config, method, restPath, responseClass, null);
    }

    /**
     * @param config
     * @param method
     * @param restPath
     * @param responseType
     */
    protected Request(TestRailConfig config, Method method, String restPath, @NonNull TypeReference<? extends T> responseType) {
        this(config, method, restPath, null, responseType);
    }

    public T execute() {
        try {

            URL url = new URL(getUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method.name());
            con.setRequestProperty("User-Agent", config.getApplicationName());
            con.setRequestProperty("Content-Type", "application/json");
            String basicAuth = "Basic "
                    + DatatypeConverter.printBase64Binary((config.getUsername()
                    + ":" + config.getPassword()).getBytes(Charset.forName("UTF-8")));
            con.setRequestProperty("Authorization", basicAuth);
            if (method == Method.POST) {
                Object content = getContent();
                if (content != null) {
                    con.setDoOutput(true);
                    try (OutputStream outputStream = new BufferedOutputStream(con.getOutputStream())) {
                        JSON.writerWithView(this.getClass()).writeValue(outputStream, content);
                    }
                }
            }
            log.debug("Sending " + method + " request to URL : " + url);
            int responseCode = 0;
            try {
                responseCode = con.getResponseCode();
            } catch (IOException e) {
                // swallow it since for 401 getResponseCode throws an IOException
                responseCode = con.getResponseCode();
            }
            log.debug("Response Code : " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                try (InputStream errorStream = con.getErrorStream()) {
                    TestRailException.Builder exceptionBuilder = new TestRailException.Builder().setResponseCode(responseCode);
                    if (errorStream == null) {
                        throw exceptionBuilder.setError("<server did not send any error message>").build();
                    }
                    throw JSON.readerForUpdating(exceptionBuilder).<TestRailException.Builder>readValue(new BufferedInputStream(errorStream)).build();
                }
            }

            try (InputStream responseStream = new BufferedInputStream(con.getInputStream())) {
                if (responseClass != null) {
                    if (responseClass == Void.class) {
                        return null;
                    }
                    return JSON.readValue(responseStream, responseClass);
                } else {
                    return JSON.readValue(responseStream, responseType);
                }
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get URL string for this request.
     *
     * @return the string URL
     * @throws IOException if there is an error creating query parameter string
     */
    private String getUrl() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(config.getBaseApiUrl()).append(restPath);

        String queryParamJson = QUERY_PARAM_MAPPER.writerWithView(getClass()).writeValueAsString(this);
        String queryParamString = QUERY_PARAM_MAPPER.readValue(queryParamJson, QueryParameterString.class).toString();
        if (!queryParamString.isEmpty()) {
            urlBuilder.append("&").append(queryParamString);
        }

        return urlBuilder.toString();
    }

    /**
     * Override this method to provide content to be send with {@code Method#POST} requests.
     *
     * @return content
     */
    protected Object getContent() {
        return null;
    }

    /**
     * Allowed HTTP methods.
     */
    protected static enum Method {
        GET, POST;
    }

}
