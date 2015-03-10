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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link com.codepine.api.testrail.internal.StringToMapDeserializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class StringToMapDeserializerTest {

    private StringToMapDeserializer stringToMapDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Before
    public void setUp() {
        stringToMapDeserializer = new StringToMapDeserializer();
    }

    @Test
    public void W_null_T_null() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn(null);
        Map<String, String> actualMap = stringToMapDeserializer.deserialize(jsonParser, null);

        // THEN
        assertNull(actualMap);
    }

    @Test
    public void W_empty_T_empty() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("");
        Map<String, String> actualMap = stringToMapDeserializer.deserialize(jsonParser, null);

        // THEN
        assertTrue("Map is not empty", actualMap.isEmpty());
    }

    @Test
    public void W_singleCommaSeparatedValuePair_T_singlePair() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("a,b");
        Map<String, String> actualMap = stringToMapDeserializer.deserialize(jsonParser, null);

        // THEN
        Map<String, String> expectedMap = Collections.singletonMap("a", "b");
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void W_threeCommaSeparatedValuePairsOnNewLines_T_threePairs() throws IOException {
        // WHEN
        when(jsonParser.getValueAsString()).thenReturn("a,b\nc, d\ne,f");
        Map<String, String> actualMap = stringToMapDeserializer.deserialize(jsonParser, null);

        // THEN
        Map<String, String> expectedMap = new HashMap<>(3);
        expectedMap.put("a", "b");
        expectedMap.put("c", "d");
        expectedMap.put("e", "f");
        assertEquals(expectedMap, actualMap);
    }
}
