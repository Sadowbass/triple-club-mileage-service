package com.triple.mileageservice.domain.review.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class DeletedReviewException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODES = ResponseCodes.ALREADY_DELETED_REVIEW;

    public DeletedReviewException() {
        super(
                RESPONSE_CODES.getRspCode(),
                RESPONSE_CODES.getRspMessage(),
                RESPONSE_CODES.getHttpStatus()
        );
    }
}
