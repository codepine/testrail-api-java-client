/**
 *
 */
package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TestRail project.
 */
@Data
public class Project {

    private int id;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String name;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String announcement;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private boolean showAnnouncement;

    @JsonView(TestRail.Projects.Update.class)
    @Getter(value = AccessLevel.PRIVATE)
    @Setter(value = AccessLevel.PRIVATE)
    private Boolean isCompleted;

    private Long completedOn;

    private String url;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private int suiteMode;

    public Boolean isCompleted() {
        return getIsCompleted();
    }

    public Project setCompleted(boolean isCompleted) {
        return setIsCompleted(isCompleted);
    }

}
