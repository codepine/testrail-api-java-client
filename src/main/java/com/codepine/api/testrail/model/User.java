package com.codepine.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail user.
 */
@Data
public class User {

    private int id;
    private String email;
    private String name;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isActive;
}
