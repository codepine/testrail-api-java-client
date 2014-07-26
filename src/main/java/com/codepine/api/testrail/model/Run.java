package com.cymbocha.apis.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String name;

    private String description;

    private String url;

    private int projectId;

    private Integer planId;

    private int suiteId;

    private Integer milestoneId;

    private Integer assignedtoId;

    private boolean includeAll;

    private Date createdOn;

    private int createdBy;

    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;

    private Date completedOn;

    private List<String> config;

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
