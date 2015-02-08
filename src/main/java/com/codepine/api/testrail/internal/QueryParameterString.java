package com.codepine.api.testrail.internal;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * String representing query parameters of a URL.
 */
public final class QueryParameterString {

    private final StringBuilder queryParamStringBuilder = new StringBuilder();

    @JsonAnySetter
    public void addQueryParameter(String key, String value) throws UnsupportedEncodingException {
        if (queryParamStringBuilder.length() > 0) {
            queryParamStringBuilder.append('&');
        }
        queryParamStringBuilder.append(URLEncoder.encode(key, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"));
    }

    @Override
    public String toString() {
        return queryParamStringBuilder.toString();
    }
}
