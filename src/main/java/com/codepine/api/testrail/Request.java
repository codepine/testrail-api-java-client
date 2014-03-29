package com.cymbocha.apis.testrail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * TestRail request.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j
public abstract class Request<T> {

    /**
     * Allowed HTTP methods.
     */
    protected static enum Method {
        GET, POST;
    }

    private static final ObjectMapper JSON = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

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
                    + ":" + config.getPassword()).getBytes(Charset
                    .forName("UTF-8")));
            con.setRequestProperty("Authorization", basicAuth);
            if (method == Method.POST) {
                Object content = getContent();
                if (content != null) {
                    con.setDoOutput(true);
                    JSON.writeValue(new BufferedOutputStream(con.getOutputStream()), getContent());
                }
            }
            log.debug("Sending " + method + " request to URL : " + url);
            int responseCode = con.getResponseCode();
            log.debug("Response Code : " + responseCode);

            if (responseCode != 200) {
                TestRailException.Builder exceptionBuilder = new TestRailException.Builder().setResponseCode(responseCode);
                throw ((TestRailException.Builder) JSON.readerForUpdating(exceptionBuilder).readValue(
                        new BufferedInputStream(con.getErrorStream()))).build();
            }

            InputStream responseStream = new BufferedInputStream(con.getInputStream());
            if (responseClass != null) {
                return JSON.readValue(responseStream, responseClass);
            } else {
                return JSON.readValue(responseStream, responseType);
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

    protected static <T> TypeReference<T> responseType() {
        return new TypeReference<T>() {
        };
    }

    protected Object getContent() {
        return null;
    }

}
