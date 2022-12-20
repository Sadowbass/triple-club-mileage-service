package com.triple.mileageservice.domain.review.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class CannotModifyReviewException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODES = ResponseCodes.CANNOT_MODIFY_REVIEW;

    public CannotModifyReviewException() {
        super(
                RESPONSE_CODES.getRspCode(),
                RESPONSE_CODES.getRspMessage(),
                RESPONSE_CODES.getHttpStatus()
        );
    }
}
