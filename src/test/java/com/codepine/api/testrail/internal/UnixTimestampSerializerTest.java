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
import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link com.codepine.api.testrail.internal.UnixTimestampModule.UnixTimestampSerializer}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UnixTimestampSerializerTest {

    private UnixTimestampModule.UnixTimestampSerializer unixTimestampSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() {
        unixTimestampSerializer = new UnixTimestampModule.UnixTimestampSerializer();
    }

    @Test
    public void W_null_T_notSerialized() throws IOException {
        // WHEN
        unixTimestampSerializer.serialize(null, jsonGenerator, null);

        // THEN
        verifyZeroInteractions(jsonGenerator);
    }

    @Test
    public void W_notNull_T_serializedCorrectly() throws IOException {
        // WHEN
        unixTimestampSerializer.serialize(new Date(1424641170000L), jsonGenerator, null);

        // THEN
        verify(jsonGenerator).writeNumber(1424641170L);
    }

}
