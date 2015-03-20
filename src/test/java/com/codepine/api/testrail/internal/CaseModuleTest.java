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

import com.codepine.api.testrail.model.Case;
import com.codepine.api.testrail.model.CaseField;
import com.codepine.api.testrail.model.Field;
import com.fasterxml.jackson.databind.InjectableValues;
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
 * Tests for {@link com.codepine.api.testrail.internal.CaseModule}.
 * <p>This test does not use mocks. It has some dependencies which it assumes are tested separately.</p>
 */
public class CaseModuleTest {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new CaseModule(), new UnixTimestampModule());

    @Test(expected = IllegalArgumentException.class)
    public void G_noCustomCaseFields_W_caseStringWithCustomStepsField_T_exception() throws IOException {
        // GIVEN
        List<CaseField> caseFields = Collections.emptyList();

        // WHEN
        objectMapper.reader(Case.class).with(new InjectableValues.Std().addValue(Case.class.toString(), caseFields)).readValue(this.getClass().getResourceAsStream("/case_with_step_field_set.json"));
    }

    @Test
    public void G_noCustomCaseFields_W_caseStringWithNoCustomStepsField_T_correctDeserialization() throws IOException {
        // GIVEN
        List<CaseField> caseFields = Collections.emptyList();

        // WHEN
        Case actualCase = objectMapper.reader(Case.class).with(new InjectableValues.Std().addValue(Case.class.toString(), caseFields)).readValue(this.getClass().getResourceAsStream("/case_with_no_custom_fields.json"));

        // THEN
        Case expectedCase = new Case().setId(13).setTitle("Test Case 2").setSectionId(6).setTypeId(6).setPriorityId(4).setCreatedBy(1).setCreatedOn(new Date(1425683583000L)).setUpdatedBy(1).setUpdatedOn(new Date(1425845918000L)).setSuiteId(4);
        assertEquals(expectedCase, actualCase);
    }

    @Test
    public void G_customCaseFieldSteps_W_caseStringWithCustomStepsField_T_correctDeserializationAndStepsField() throws IOException {
        // GIVEN
        CaseField stepField = objectMapper.readValue(this.getClass().getResourceAsStream("/step_field.json"), CaseField.class);
        List<CaseField> caseFields = Collections.singletonList(stepField);

        // WHEN
        Case actualCase = objectMapper.reader(Case.class).with(new InjectableValues.Std().addValue(Case.class.toString(), caseFields)).readValue(this.getClass().getResourceAsStream("/case_with_step_field_set.json"));

        // THEN
        List<Field.Step> steps = Arrays.asList(new Field.Step().setContent("Step 1").setExpected("Expected 1"), new Field.Step().setContent("Step 2").setExpected("Expected 2"));
        Case expectedCase = new Case().setId(13).setTitle("Test Case 2").setSectionId(6).setTypeId(6).setPriorityId(4).setCreatedBy(1).setCreatedOn(new Date(1425683583000L)).setUpdatedBy(1).setUpdatedOn(new Date(1425845918000L)).setSuiteId(4).addCustomField("separated_steps", steps);
        assertEquals(expectedCase, actualCase);
    }
}
