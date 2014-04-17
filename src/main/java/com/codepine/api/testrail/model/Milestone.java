package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TestRail milestone.
 */
@Data
@Accessors(chain = true)
public class Milestone {

    private int id;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private String name;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private String description;

    private int projectId;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private Date dueOn;

    @JsonView({TestRail.Milestones.Update.class})
    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;

    private Date completedOn;

    private String url;
}
