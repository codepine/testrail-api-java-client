/**
 *
 */
package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TestRail project.
 */
@Data
@Accessors(chain = true)
public class Project {

    private int id;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String name;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String announcement;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private boolean showAnnouncement;

    @JsonView(TestRail.Projects.Update.class)
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;

    private Long completedOn;

    private String url;

}
