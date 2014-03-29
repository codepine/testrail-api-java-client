/**
 *
 */
package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author kms
 */
@Data
@Accessors(chain = true)
public class Project {

    private int id;
    private String name;
    private String announcement;
    private boolean showAnnouncement;
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;
    private Long completedOn;
    private String url;

}
