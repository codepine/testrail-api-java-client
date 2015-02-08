package com.codepine.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail test case priority.
 */
@Data
public class Priority {

    private int id;

    private String name;

    private String shortName;

    private int priority;

    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isDefault;

}
