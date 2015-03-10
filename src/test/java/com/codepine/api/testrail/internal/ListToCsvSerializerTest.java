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

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link com.codepine.api.testrail.internal.ListToCsvSerializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListToCsvSerializerTest {

    private ListToCsvSerializer listToCsvSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        listToCsvSerializer = new ListToCsvSerializer();
    }

    @Test
    public void W_null_T_NoJson() throws IOException {
        // WHEN
        listToCsvSerializer.serialize(null, jsonGenerator, null);

        // THEN
        verifyZeroInteractions(jsonGenerator);
    }

    @Test
    public void W_empty_T_empty() throws IOException {
        // WHEN
        listToCsvSerializer.serialize(Collections.emptyList(), jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeString("");
    }

    @Test
    public void W_singleElement_T_singleElement() throws IOException {
        // WHEN
        listToCsvSerializer.serialize(Collections.singletonList("a"), jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeString("a");
    }

    @Test
    public void W_threeStringElements_T_threeCommaSeparatedValues() throws IOException {
        // WHEN
        listToCsvSerializer.serialize(Arrays.asList("a", "b", "c"), jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeString("a,b,c");
    }

    @Test
    public void W_threeIntegerElements_T_threeCommaSeparatedValues() throws IOException {
        // WHEN
        listToCsvSerializer.serialize(Arrays.asList(1, 2, 3), jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeString("1,2,3");
    }
}
