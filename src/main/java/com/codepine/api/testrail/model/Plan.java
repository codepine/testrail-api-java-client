package com.cymbocha.apis.testrail.model;

import com.cymbocha.apis.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * TestRail plan.
 */
@Data
@Accessors(chain = true)
public class Plan {

    private int id;

    @JsonView({TestRail.Plans.Add.class, TestRail.Plans.Update.class})
    private String name;

    @JsonView({TestRail.Plans.Add.class, TestRail.Plans.Update.class})
    private String description;

    private String url;

    private int projectId;

    @JsonView({TestRail.Plans.Add.class, TestRail.Plans.Update.class})
    private Integer milestoneId;

    private Integer assignedtoId;

    private Date createdOn;

    private int createdBy;

    @JsonProperty
    @Getter(onMethod = @_({@JsonIgnore}))
    private boolean isCompleted;

    private Date completedOn;

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

    @JsonView({TestRail.Plans.Add.class})
    private List<Entry> entries;

    @Data
    public static class Entry {

        private String id;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private String name;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
        private int suiteId;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private Integer assignedtoId;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private Boolean includeAll;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private List<Integer> caseIds;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
        private List<Integer> configIds;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
        private List<Run> runs;

        @Data
        @ToString(callSuper = true)
        @Accessors(chain = true)
        public static class Run extends com.cymbocha.apis.testrail.model.Run {
            private String entryId;
            private int entryIndex;
        }
    }
}
