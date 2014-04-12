package com.cymbocha.apis.testrail.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * TestRail configuration.
 */
@Data
@Accessors(chain = true)
public class Configuration {

    private int id;
    private String name;
    private int projectId;
    private List<Config> configs;

    @Data
    @Accessors(chain = true)
    public class Config {

        private int id;
        private String name;
        private int groupId;

    }
}
