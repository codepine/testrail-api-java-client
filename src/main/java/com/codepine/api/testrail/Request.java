package com.cymbocha.apis.testrail;

import com.cymbocha.apis.testrail.internal.UnixTimestampModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import javax.net.ssl.HttpsURLConnection;
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

    protected static final ObjectMapper JSON = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES).configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false).setSerializationInclusion(JsonInclude.Include.NON_DEFAULT).registerModules(new FieldModule(), new PlanModule(), new UnixTimestampModule());

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
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
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
                    throw ((TestRailException.Builder) JSON.readerForUpdating(exceptionBuilder).readValue(
                            new BufferedInputStream(errorStream))).build();
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

    private String getUrl() {
        return config.getBaseApiUrl() + restPath;
    }

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
