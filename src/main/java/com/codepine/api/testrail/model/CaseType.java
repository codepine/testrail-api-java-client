package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail case type.
 */
@Data
@Accessors(chain = true)
public class CaseType {

    private int id;
    private String name;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isDefault;

}
