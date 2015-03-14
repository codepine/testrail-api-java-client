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

package com.codepine.api.testrail.internal;

import com.codepine.api.testrail.model.Plan;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link com.codepine.api.testrail.internal.PlanModule}.
 * <p>This test does not use mocks. It has some dependencies which it assumes are tested separately.</p>
 */
public class PlanModuleTest {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new PlanModule(), new UnixTimestampModule());

    @Test
    public void W_planWithNoEntries_T_correctDeserialization() throws IOException {
        // WHEN
        Plan actualPlan = objectMapper.readValue(this.getClass().getResourceAsStream("/plan_with_no_entries.json"), Plan.class);

        // THEN
        Plan expectedPlan = new Plan().setId(6).setName("Chrome Plan").setUntestedCount(7).setFailedCount(2).setProjectId(1).setCreatedBy(1).setCreatedOn(new Date(1415642870000L)).setUrl("http://somehost/testrail/index.php?/plans/view/6");
        assertEquals(expectedPlan, actualPlan);
    }

    @Test
    public void W_planWithEntries_T_correctDeserializationAndRunsHaveCreatedOnAndCreatedBySameAsPlan() throws IOException {
        // WHEN
        Plan actualPlan = objectMapper.readValue(this.getClass().getResourceAsStream("/plan_with_entries.json"), Plan.class);

        // THEN
        Plan.Entry.Run run = (Plan.Entry.Run) new Plan.Entry.Run().setEntryIndex(1).setEntryId("67cef25c-0e7b-4457-8eda-aa33596d9a04").setId(7).setSuiteId(1).setName("Test Suite 1").setIncludeAll(false).setPassedCount(1).setProjectId(1).setPlanId(6).setConfigIds(Collections.<Integer>emptyList()).setUrl("http://somehost/testrail/index.php?/runs/view/7").setCreatedBy(1).setCreatedOn(new Date(1415642870000L));
        Plan.Entry entry = new Plan.Entry().setId("67cef25c-0e7b-4457-8eda-aa33596d9a04").setSuiteId(1).setName("Test Suite 1").setRuns(Collections.singletonList(run));
        List<Plan.Entry> entries = Arrays.asList(entry, new Plan.Entry().setId("f070f479-723f-4aeb-80b6-feb532263d3d").setSuiteId(3).setName("Test Suite 2"));
        Plan expectedPlan = new Plan().setId(6).setName("Chrome Plan").setUntestedCount(7).setFailedCount(2).setProjectId(1).setCreatedBy(1).setCreatedOn(new Date(1415642870000L)).setUrl("http://somehost/testrail/index.php?/plans/view/6").setEntries(entries);
        assertEquals(expectedPlan, actualPlan);
    }
}
