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

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link com.codepine.api.testrail.internal.CsvToListDeserializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvToListDeserializerTest {

    private CsvToListDeserializer csvToListDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Before
    public void setUp() {
        csvToListDeserializer = new CsvToListDeserializer();
    }

    @Test
    public void W_null_T_null() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn(null);
        List<String> list = csvToListDeserializer.deserialize(jsonParser, null);

        // THEN
        assertNull("Expected list to be null but found: " + list, list);
    }

    @Test
    public void W_empty_T_empty() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("");
        List<String> list = csvToListDeserializer.deserialize(jsonParser, null);

        // THEN
        assertTrue("Expected list to be empty but found: " + list, list.isEmpty());
    }

    @Test
    public void W_noComma_T_singleElement() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("a b c d");
        List<String> actualList = csvToListDeserializer.deserialize(jsonParser, null);

        // THEN
        List<String> expectedList = Collections.singletonList("a b c d");
        assertEquals("Expected list is not same as actual list.", expectedList, actualList);
    }

    @Test
    public void W_fourCommaSeparatedValues_T_fourElements() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("a, b, c, d");
        List<String> actualList = csvToListDeserializer.deserialize(jsonParser, null);

        // THEN
        List<String> expectedList = Arrays.asList("a", "b", "c", "d");
        assertEquals("Expected list is not same as actual list.", expectedList, actualList);
    }

    @Test
    public void W_fourCommaSeparatedValuesWithOneEmpty_T_threeElements() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("a, b, , d");
        List<String> actualList = csvToListDeserializer.deserialize(jsonParser, null);

        // THEN
        List<String> expectedList = Arrays.asList("a", "b", "d");
        assertEquals("Expected list is not same as actual list.", expectedList, actualList);
    }
}
