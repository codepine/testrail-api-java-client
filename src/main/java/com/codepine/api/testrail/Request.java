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

import com.codepine.api.testrail.internal.CaseModule;
import com.codepine.api.testrail.internal.FieldModule;
import com.codepine.api.testrail.internal.PlanModule;
import com.codepine.api.testrail.internal.QueryParameterString;
import com.codepine.api.testrail.internal.ResultModule;
import com.codepine.api.testrail.internal.UnixTimestampModule;
import com.codepine.api.testrail.internal.UrlConnectionFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * TestRail request.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j
public abstract class Request<T> {

    private static final UrlConnectionFactory DEFAULT_URL_CONNECTION_FACTORY = new UrlConnectionFactory();

    private static final ObjectMapper JSON = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .registerModules(new CaseModule(), new FieldModule(), new PlanModule(), new ResultModule(), new UnixTimestampModule());

    @NonNull
    private final TestRailConfig config;
    @NonNull
    private final Method method;
    @NonNull
    private final String restPath;
    private final Class<? extends T> responseClass;
    private final TypeReference<? extends T> responseType;
    private UrlConnectionFactory urlConnectionFactory = DEFAULT_URL_CONNECTION_FACTORY;

    /**
     * @param config TestRail configuration
     * @param method the HTTP method for request
     * @param restPath the path of the request URL
     * @param responseClass the type of the response entity
     */
    Request(TestRailConfig config, Method method, String restPath, @NonNull Class<? extends T> responseClass) {
        this(config, method, restPath, responseClass, null);
    }

    /**
     * @param config TestRail configuration
     * @param method the HTTP method for request
     * @param restPath the path of the request URL
     * @param responseType the type of the response entity
     */
    Request(TestRailConfig config, Method method, String restPath, @NonNull TypeReference<? extends T> responseType) {
        this(config, method, restPath, null, responseType);
    }

    /**
     * Execute this request.
     *
     * @return response from TestRail
     */
    public T execute() {
        try {

            String url = getUrl();
            HttpURLConnection con = (HttpURLConnection) urlConnectionFactory.getUrlConnection(url);
            con.setRequestMethod(method.name());
            if (config.getApplicationName().isPresent()) {
                con.setRequestProperty("User-Agent", config.getApplicationName().get());
            }
            con.setRequestProperty("Content-Type", "application/json");
            String sourceString = config.getUsername() + ":" + config.getPassword();
            String basicAuth = "Basic " +
                Base64.getEncoder().encodeToString(sourceString.getBytes(Charset.forName("UTF-8")));
            con.setRequestProperty("Authorization", basicAuth);
            if (method == Method.POST) {
                con.setDoOutput(true);
                Object content = getContent();
                if (content != null) {
                    try (OutputStream outputStream = new BufferedOutputStream(con.getOutputStream())) {
                        JSON.writerWithView(this.getClass()).writeValue(outputStream, content);
                    }
                } else {
                    con.setFixedLengthStreamingMode(0);
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
                Object supplementForDeserialization = getSupplementForDeserialization();
                if (responseClass != null) {
                    if (responseClass == Void.class) {
                        return null;
                    }
                    if (supplementForDeserialization != null) {
                        return JSON.reader(responseClass).with(new InjectableValues.Std().addValue(responseClass.toString(), supplementForDeserialization)).readValue(responseStream);
                    }
                    return JSON.readValue(responseStream, responseClass);
                } else {
                    if (supplementForDeserialization != null) {
                        String supplementKey = responseType.getType().toString();
                        if (responseType.getType() instanceof ParameterizedType) {
                            Type[] actualTypes = ((ParameterizedType) responseType.getType()).getActualTypeArguments();
                            if (actualTypes.length == 1 && actualTypes[0] instanceof Class<?>) {
                                supplementKey = actualTypes[0].toString();
                            }
                        }
                        return JSON.reader(responseType).with(new InjectableValues.Std().addValue(supplementKey, supplementForDeserialization)).readValue(responseStream);
                    }
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

        String queryParamJson = JSON.writerWithView(getClass()).writeValueAsString(this);
        String queryParamString = JSON.readValue(queryParamJson, QueryParameterString.class).toString();
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
    Object getContent() {
        return null;
    }

    /**
     * Override this method to provide supplementary information to deserializer.
     *
     * @return any object acting as supplement for deserialization
     */
    Object getSupplementForDeserialization() {
        return null;
    }

    /**
     * Set URL connection factory. Only used for testing.
     *
     * @param urlConnectionFactory the URL connection factory
     */
    void setUrlConnectionFactory(UrlConnectionFactory urlConnectionFactory) {
        this.urlConnectionFactory = urlConnectionFactory;
    }

    /**
     * Allowed HTTP methods.
     */
    static enum Method {
        GET, POST;
    }

}
