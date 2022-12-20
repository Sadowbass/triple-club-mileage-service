package com.triple.mileageservice.domain.review.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class CannotFindReviewException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODE = ResponseCodes.CANNOT_FIND_REVIEW;

    public CannotFindReviewException(String reviewId) {
        super(
                RESPONSE_CODE.getRspCode(),
                String.format(RESPONSE_CODE.getRspMessage(), reviewId),
                RESPONSE_CODE.getHttpStatus()
        );
    }
}
