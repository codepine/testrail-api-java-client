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

import com.codepine.api.testrail.internal.*;
import com.codepine.api.testrail.model.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TestRail request.
 */
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
    private String restPath;
    private String apiSegment;
    private final Class<? extends T> responseClass;
    private final TypeReference<? extends T> responseType;
    private final TypeReference<Page<T>> pageType;
    private UrlConnectionFactory urlConnectionFactory = DEFAULT_URL_CONNECTION_FACTORY;

    Request(TestRailConfig config, Method method, String restPath, Class<? extends T> responseClass, TypeReference<? extends T>
            responseType, TypeReference<Page<T>> pageType) {
        this.config = config;
        this.method = method;

        this.responseClass = responseClass;
        this.responseType = responseType;
        this.pageType = pageType;
        this.apiSegment = config.getBaseApiUrl().split("\\?")[1];
        this.restPath = restPath.replace(this.apiSegment, "");
    }

    /**
     * @param config TestRail configuration
     * @param method the HTTP method for request
     * @param restPath the path of the request URL
     * @param responseClass the type of the response entity
     */
    Request(TestRailConfig config, Method method, String restPath, @NonNull Class<? extends T> responseClass) {
        this(config, method, restPath, responseClass, null, null);
    }

    /**
     * @param config TestRail configuration
     * @param method the HTTP method for request
     * @param restPath the path of the request URL
     * @param responseType the type of the response entity
     */
    Request(TestRailConfig config, Method method, String restPath, @NonNull TypeReference<? extends T> responseType) {
        this(config, method, restPath, null, responseType, null);
    }

    Request(TestRailConfig config, Method method, String restPath, @NonNull TypeReference<? extends T> responseType,
            @NonNull TypeReference<Page<T>> pageType) {
        this(config, method, restPath, null, responseType, pageType);
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
            String basicAuth = "Basic "
                    + DatatypeConverter.printBase64Binary((config.getUsername()
                    + ":" + config.getPassword()).getBytes(Charset.forName("UTF-8")));
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
                    String payload = new String(ByteStreams.toByteArray(responseStream), Charsets.UTF_8).replace("\"_links\":", "\"links\":");
                    if (((ParameterizedType) responseType.getType()).getRawType().getTypeName().equals("java.util.List")
                    && payload.contains("\"offset\":") && payload.contains("\"limit\":") && payload.contains("\"offset\":")) {
                        Matcher matcher = Pattern.compile("get_([^\\_/]+)").matcher(restPath);
                        if (matcher.find())
                            PageDeserializer.field = matcher.group(1);
                        try {
                            PageDeserializer.type = Class.forName(((ParameterizedType) responseType.getType()).getActualTypeArguments()[0].getTypeName());
                        }
                        catch(Exception e) {
                            return ((T)new ArrayList());
                        }
                        if (supplementForDeserialization != null) {
                            PageDeserializer.supplement = supplementForDeserialization;
                        }
                        Page<T> page = JSON.readValue(payload, pageType);
                        if (page._links.next != null) {
                            restPath = page._links.next.replace(this.apiSegment, "");
                            T concat = execute();
                            T models = page.objects;
                            ((List)models).addAll(((List)concat));
                            return models;
                        }
                        else
                            return page.objects;
                    }
                    else if (supplementForDeserialization != null) {
                        String supplementKey = responseType.getType().toString();
                        if (responseType.getType() instanceof ParameterizedType) {
                            Type[] actualTypes = ((ParameterizedType) responseType.getType()).getActualTypeArguments();
                            if (actualTypes.length == 1 && actualTypes[0] instanceof Class<?>) {
                                supplementKey = actualTypes[0].toString();
                            }
                        }
                        return JSON.reader(responseType).with(new InjectableValues.Std().addValue(supplementKey, supplementForDeserialization)).readValue(payload);
                    }
                    return JSON.readValue(payload, responseType);
                }
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
