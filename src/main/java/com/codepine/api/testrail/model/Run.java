package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * TestRail run.
 */
@Data
@Accessors(chain = true)
public class Run {

    private int id;

    @JsonView({TestRail.Runs.Add.class, TestRail.Runs.Update.class})
    private String name;

    @JsonView({TestRail.Runs.Add.class, TestRail.Runs.Update.class})
    private String description;

    private String url;

    private int projectId;

    private Integer planId;

    @JsonView(TestRail.Runs.Add.class)
    private int suiteId;

    @JsonView({TestRail.Runs.Add.class, TestRail.Runs.Update.class})
    private Integer milestoneId;

    @JsonView({TestRail.Runs.Add.class, TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
    private Integer assignedtoId;

    @JsonView({TestRail.Runs.Add.class, TestRail.Runs.Update.class, TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
    private Boolean includeAll;

    @JsonView({TestRail.Runs.Add.class, TestRail.Runs.Update.class, TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
    private List<Integer> caseIds;

    private Date createdOn;

    private int createdBy;

    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;

    private Date completedOn;

    private List<String> config;

    @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
    private List<Integer> configIds;

    private int passedCount;

    private int blockedCount;

    private int untestedCount;

    private int retestCount;

    private int failedCount;

    private int customStatus1Count;

    private int customStatus2Count;

    private int customStatus3Count;

    private int customStatus4Count;

    private int customStatus5Count;

    private int customStatus6Count;

    private int customStatus7Count;

}
