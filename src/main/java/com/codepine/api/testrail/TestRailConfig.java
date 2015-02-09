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

import com.google.common.base.Preconditions;
import lombok.*;

/**
 * Configuration for using this client library.
 */
@Value
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"password"})
public class TestRailConfig {

    private final String applicationName;
    private final String baseApiUrl;
    private final String username;
    private final String password;

    /**
     * Get a builder to build an instance of {@code TestRailConfig}.
     *
     * @param applicationName the name of the application which will communicate with TestRail
     * @param username        the username of the TestRail user on behalf of which the communication with TestRail will happen
     * @param password        the password of the same TestRail user
     * @return a builder to build {@code TestRailConfig} instance
     */
    public static Builder builder(@NonNull final String applicationName, @NonNull final String username, @NonNull final String password) {
        return new Builder(applicationName, username, password);
    }

    /**
     * Builder for {@code TestRailConfig}.
     */
    public static class Builder {

        private static final String DEFAULT_BASE_API_PATH = "index.php?/api/v2/";

        private final String applicationName;
        private final String username;
        private final String password;
        private String baseApiUrl;

        /**
         * @param applicationName the name of the application which will communicate with TestRail
         * @param username        the username of the TestRail user on behalf of which the communication with TestRail will happen
         * @param password        the password of the same TestRail user
         */
        private Builder(final String applicationName, final String username, final String password) {
            this.applicationName = applicationName;
            this.username = username;
            this.password = password;
        }

        /**
         * Set the base URL of your TestRail API. The URL should be a complete URL where your TestRail API is hosted. For e.g. https://example.com/testrail/index.php?/api/v2/
         * Note: This method cannot be used with {@link #endPoint}. Only use this method if {@link #endPoint} does not meet your requirements.
         *
         * @param baseApiUrl the complete URL where your TestRail API is hosted
         * @return this for chaining
         */
        public Builder baseApiUrl(final String baseApiUrl) {
            Preconditions.checkState(baseApiUrl == null, "Base API URL is already set");
            this.baseApiUrl = baseApiUrl;
            return this;
        }


        /**
         * Set the end point URL where your TestRail is hosted. This is useful for standard TestRail deployments. E.g. https://example.com/testrail
         * Note: This method cannot be used with {@link #baseApiUrl}. If possible, use this method over {@link #baseApiUrl}.
         *
         * @param endPoint the end point URL where your TestRail is hosted
         * @return this for chaining
         */
        public Builder endPoint(final String endPoint) {
            Preconditions.checkState(baseApiUrl == null, "Base API URL is already set");
            this.baseApiUrl = (endPoint.endsWith("/") ? endPoint : endPoint + "/") + DEFAULT_BASE_API_PATH;
            return this;
        }

        /**
         * Build an instance of {@code TestRailConfig}.
         *
         * @return a new instance
         */
        public TestRailConfig build() {
            return new TestRailConfig(applicationName, baseApiUrl, username, password);
        }
    }

}
