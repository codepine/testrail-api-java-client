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

package com.codepine.api.testrail;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Exception representing error returned by TestRail API.
 */
public class TestRailException extends RuntimeException {

    private static final long serialVersionUID = -2131644110724458502L;

    @Getter
    private final int responseCode;

    /**
     * @param responseCode the HTTP response code from the TestRail server
     * @param error        the error message from TestRail service
     */
    TestRailException(int responseCode, String error) {
        super(responseCode + " - " + error);
        this.responseCode = responseCode;
    }

    /**
     * Builder for {@code TestRailException}.
     */
    @Setter
    static class Builder {
        private int responseCode;
        private String error;

        public TestRailException build() {
            Preconditions.checkNotNull(responseCode);
            Preconditions.checkNotNull(error);
            return new TestRailException(responseCode, error);
        }
    }
}
