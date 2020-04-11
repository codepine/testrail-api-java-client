/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Kunal Shah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.codepine.api.testrail.model;

import com.codepine.api.testrail.TestRail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * TestRail plan.
 */
@Data
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
        private Integer suiteId;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private String description;
        
        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private Integer assignedtoId;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        @Getter(value = AccessLevel.PRIVATE)
        private Boolean includeAll;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class, TestRail.Plans.UpdateEntry.class})
        private List<Integer> caseIds;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
        private List<Integer> configIds;

        @JsonView({TestRail.Plans.Add.class, TestRail.Plans.AddEntry.class})
        private List<Run> runs;

        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        public static class Run extends com.codepine.api.testrail.model.Run {
            private String entryId;
            private int entryIndex;
        }

        public Boolean isIncludeAll() {
            return getIncludeAll();
        }
    }
}
