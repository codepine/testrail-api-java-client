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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Tests for {@link com.codepine.api.testrail.internal.BooleanToIntSerializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class BooleanToIntSerializerTest {

    private BooleanToIntSerializer booleanToIntSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        booleanToIntSerializer = new BooleanToIntSerializer();
    }

    @Test
    public void W_null_T_0() throws IOException {
        // WHEN
        Boolean value = null;
        booleanToIntSerializer.serialize(value, jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeNumber(0);
    }

    @Test
    public void W_false_T_0() throws IOException {
        // WHEN
        Boolean value = false;
        booleanToIntSerializer.serialize(value, jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeNumber(0);
    }

    @Test
    public void W_true_T_1() throws IOException {
        // WHEN
        Boolean value = false;
        booleanToIntSerializer.serialize(value, jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeNumber(0);
    }
}
