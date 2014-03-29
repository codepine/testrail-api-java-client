package com.cymbocha.apis.testrail;


import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Exception representing error returned by TestRail service.
 *
 * @author kms
 */
public class TestRailException extends RuntimeException {

    private static final long serialVersionUID = -2131644110724458502L;

    @Getter
    private final int responseCode;

    /**
     * @param responseCode the HTTP response code from the TestRail server
     * @param error the error message from TestRail service
     */
    TestRailException(int responseCode, String error) {
        super(error);
        this.responseCode = responseCode;
    }

    /**
     * Builder for {@code TestRailException}.
     */
    @Accessors(chain=true)
    static class Builder {
        @Setter
        private int responseCode;
        @Setter
        private String error;

        public TestRailException build() {
            Preconditions.checkNotNull(responseCode);
            Preconditions.checkNotNull(error);
            return new TestRailException(responseCode, error);
        }
    }
}
