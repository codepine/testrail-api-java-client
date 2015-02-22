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

import com.google.common.base.Optional;
import lombok.*;

/**
 * Configuration for using this client library.
 */
@Value
@ToString(exclude = {"password"})
public class TestRailConfig {

    private final String baseApiUrl;
    private final String username;
    private final String password;
    private final Optional<String> applicationName;

    private TestRailConfig(final String baseApiUrl, final String username, final String password, final String applicationName) {
        this.baseApiUrl = baseApiUrl;
        this.username = username;
        this.password = password;
        this.applicationName = Optional.fromNullable(applicationName);
    }

    /**
     * Get a builder to build an instance of {@code TestRailConfig}.
     *
     * @param endPoint the URL end point where your TestRail is hosted, for e.g. https://example.com/testrail
     * @param username the username of the TestRail user on behalf of which the communication with TestRail will happen
     * @param password the password of the same TestRail user
     * @return a builder to build {@code TestRailConfig} instance
     */
    public static Builder builder(@NonNull final String endPoint, @NonNull final String username, @NonNull final String password) {
        return new Builder(endPoint, username, password);
    }

    /**
     * Builder for {@code TestRailConfig}.
     */
    public static class Builder {

        private static final String DEFAULT_BASE_API_PATH = "index.php?/api/v2/";

        private final String endPoint;
        private final String username;
        private final String password;
        private String apiPath;
        private String applicationName;

        /**
         * @param endPoint the URL end point where your TestRail is hosted, for e.g. https://example.com/testrail
         * @param username the username of the TestRail user on behalf of which the communication with TestRail will happen
         * @param password the password of the same TestRail user
         */
        private Builder(final String endPoint, final String username, final String password) {
            String sanitizedEndPoint = endPoint.trim();
            if (!sanitizedEndPoint.endsWith("/")) {
                sanitizedEndPoint = sanitizedEndPoint + "/";
            }
            this.endPoint = sanitizedEndPoint;
            this.username = username;
            this.password = password;
            apiPath = DEFAULT_BASE_API_PATH;
        }

        /**
         * Set the URL path of your TestRail API. Useful to override the default API path of standard TestRail deployments.
         *
         * @param apiPath the URL path of your TestRail API
         * @return this for chaining
         * @throws java.lang.NullPointerException if apiPath is null
         */
        public Builder apiPath(@NonNull final String apiPath) {
            String sanitizedApiPath = apiPath.trim();
            if (sanitizedApiPath.startsWith("/")) {
                sanitizedApiPath = sanitizedApiPath.substring(1);
            }
            if (!sanitizedApiPath.endsWith("/")) {
                sanitizedApiPath = sanitizedApiPath + "/";
            }
            this.apiPath = sanitizedApiPath;
            return this;
        }

        /**
         * Set the name of the application which will communicate with TestRail.
         *
         * @param applicationName name of the application
         * @return this for chaining
         * @throws java.lang.NullPointerException if applicationName is null
         */
        public Builder applicationName(@NonNull final String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        /**
         * Build an instance of {@code TestRailConfig}.
         *
         * @return a new instance
         */
        public TestRailConfig build() {
            return new TestRailConfig(endPoint + apiPath, username, password, applicationName);
        }
    }

}
