package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail user.
 */
@Data
@Accessors(chain = true)
public class User {

    private int id;
    private String email;
    private String name;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isActive;
}
