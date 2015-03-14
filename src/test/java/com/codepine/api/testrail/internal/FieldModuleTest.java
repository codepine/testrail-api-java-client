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

import com.codepine.api.testrail.model.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link com.codepine.api.testrail.internal.FieldModule}.
 */
public class FieldModuleTest {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModules(new FieldModule());

    @Test
    public void W_notNull_T_correctFieldAndOptions() throws IOException {
        // WHEN
        Field actualField = objectMapper.readValue(this.getClass().getResourceAsStream("/step_field.json"), Field.class);

        // THEN
        Field.Config.StepsOptions options = (Field.Config.StepsOptions) new Field.Config.StepsOptions().setFormat("markdown").setHasExpected(true).setRows(6).setRequired(false);
        Field.Config.Context context = new Field.Config.Context().setGlobal(true).setProjectIds(Collections.<Integer>emptyList());
        Field.Config config = new Field.Config().setId("47e68955-c7fc-4c01-8313-d9de6c4cad7c").setContext(context).setOptions(options);
        Field expectedField = new Field().setId(4).setTypeId(10).setType(Field.Type.STEPS).setName("separated_steps").setSystemName("custom_separated_steps").setLabel("Separated Steps").setConfigs(Collections.singletonList(config)).setDisplayOrder(4);
        assertEquals(expectedField, actualField);
    }
}
