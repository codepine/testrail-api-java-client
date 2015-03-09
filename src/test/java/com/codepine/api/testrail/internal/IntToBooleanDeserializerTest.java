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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link com.codepine.api.testrail.internal.IntToBooleanDeserializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class IntToBooleanDeserializerTest {

    private IntToBooleanDeserializer intToBooleanDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Before
    public void setUp() {
        intToBooleanDeserializer = new IntToBooleanDeserializer();
    }

    @Test
    public void W_0_T_false() throws IOException {
        // WHEN
        when(jsonParser.getValueAsInt(0)).thenReturn(0);
        Boolean actualBoolean = intToBooleanDeserializer.deserialize(jsonParser, null);

        // THEN
        assertFalse(actualBoolean);
    }

    @Test
    public void W_1_T_true() throws IOException {
        // WHEN
        when(jsonParser.getValueAsInt(0)).thenReturn(1);
        Boolean actualBoolean = intToBooleanDeserializer.deserialize(jsonParser, null);

        // THEN
        assertTrue(actualBoolean);
    }

    @Test
    public void W_negative_T_false() throws IOException {
        // WHEN
        when(jsonParser.getValueAsInt(0)).thenReturn(-1);
        Boolean actualBoolean = intToBooleanDeserializer.deserialize(jsonParser, null);

        // THEN
        assertFalse(actualBoolean);
    }

    @Test
    public void W_greaterThan1_T_true() throws IOException {
        // WHEN
        when(jsonParser.getValueAsInt(0)).thenReturn(5);
        Boolean actualBoolean = intToBooleanDeserializer.deserialize(jsonParser, null);

        // THEN
        assertTrue(actualBoolean);
    }
}
