package com.codepine.api.testrail.model;

import com.codepine.api.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TestRail suite.
 */
@Data
public class Suite {

    private int id;

    @JsonView({TestRail.Suites.Add.class, TestRail.Suites.Update.class})
    private String name;

    @JsonView({TestRail.Suites.Add.class, TestRail.Suites.Update.class})
    private String description;

    private int projectId;

    private String url;

}