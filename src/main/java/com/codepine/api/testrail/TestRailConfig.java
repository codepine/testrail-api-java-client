package com.cymbocha.apis.testrail; /**
 *
 */

import lombok.Value;

/**
 * @author kms
 */
@Value
public class TestRailConfig {

    private static final String DEFAULT_BASE_PATH = "index.php?/api/v2/";

    private final String applicationName;
    private final String domain;
    private final String username;
    private final String password;

    /**
     * @param applicationName
     * @param domain
     * @param username
     * @param password
     */
    public TestRailConfig(String applicationName, String domain,
                          String username, String password) {
        this.applicationName = applicationName;
        this.domain = domain.endsWith("/") ? domain : domain + "/";
        this.username = username;
        this.password = password;
    }

    /**
     * Get the base URL where API is hosted.
     *
     * @return the base URL
     */
    public String getBaseApiUrl() {
        return "https://" + domain + DEFAULT_BASE_PATH;
    }

}
