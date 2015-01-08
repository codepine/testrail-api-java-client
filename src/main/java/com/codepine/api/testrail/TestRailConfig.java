package com.cymbocha.apis.testrail;

import com.google.common.base.Preconditions;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * Configuration for using this client library.
 *
 * @author kms
 */
@Value
@Accessors(chain = true)
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
