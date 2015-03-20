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

import com.codepine.api.testrail.model.CaseField;
import com.codepine.api.testrail.model.Field;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
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
 * Tests for {@link com.codepine.api.testrail.internal.ResultModule}.
 * <p>This test does not use mocks. It has some dependencies which it assumes are tested separately.</p>
 */
public class ResultModuleTest {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new ResultModule(), new UnixTimestampModule());

    @Test(expected = IllegalArgumentException.class)
    public void G_noCustomResultFields_W_resultStringWithCustomStepResultsField_T_exception() throws IOException {
        // GIVEN
        List<CaseField> resultFields = Collections.emptyList();

        // WHEN
        objectMapper.reader(Result.class).with(new InjectableValues.Std().addValue(Result.class.toString(), resultFields)).readValue(this.getClass().getResourceAsStream("/result_with_step_result_field_set.json"));
    }

    @Test
    public void G_noCustomResultFields_W_resultStringWithNoCustomResultsField_T_correctDeserialization() throws IOException {
        // GIVEN
        List<CaseField> resultFields = Collections.emptyList();

        // WHEN
        Result actualResult = objectMapper.reader(Result.class).with(new InjectableValues.Std().addValue(Result.class.toString(), resultFields)).readValue(this.getClass().getResourceAsStream("/result_with_no_custom_fields.json"));

        // THEN
        Result expectedResult = new Result().setId(11).setTestId(48).setStatusId(1).setCreatedBy(1).setCreatedOn(new Date(1425687075000L));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void G_customResultFieldStepResults_W_resultStringWithCustomStepResultsField_T_correctDeserializationAndStepResultsField() throws IOException {
        // GIVEN
        ResultField stepResultField = objectMapper.readValue(this.getClass().getResourceAsStream("/step_result_field.json"), ResultField.class);
        List<ResultField> resultFields = Collections.singletonList(stepResultField);

        // WHEN
        Result actualResult = objectMapper.reader(Result.class).with(new InjectableValues.Std().addValue(Result.class.toString(), resultFields)).readValue(this.getClass().getResourceAsStream("/result_with_step_result_field_set.json"));

        // THEN
        List<Field.StepResult> stepResults = Arrays.asList(new Field.StepResult().setContent("Step 1").setExpected("Expected 1").setActual("Expected 2").setStatusId(4), new Field.StepResult().setContent("Step 2").setExpected("Expected 2").setActual("Unexpected").setStatusId(3));
        Result expectedResult = new Result().setId(11).setTestId(48).setStatusId(1).setCreatedBy(1).setCreatedOn(new Date(1425687075000L)).addCustomField("step_results", stepResults);
        assertEquals(expectedResult, actualResult);
    }
}
