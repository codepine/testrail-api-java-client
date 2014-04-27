package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail status.
 */
@Data
@Accessors(chain = true)
public class Status {

    private int id;
    private String name;
    private String label;
    private int colorDark;
    private int colorMedium;
    private int colorBright;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isSystem;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isUntested;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isFinal;
}
