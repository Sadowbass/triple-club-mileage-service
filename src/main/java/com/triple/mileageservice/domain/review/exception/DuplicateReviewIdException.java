package com.triple.mileageservice.domain.review.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

import java.util.UUID;

public class DuplicateReviewIdException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODES = ResponseCodes.DUPLICATE_REVIEW_ID;

    public DuplicateReviewIdException(UUID reviewId) {
        super(
                RESPONSE_CODES.getRspCode(),
                String.format(RESPONSE_CODES.getRspMessage(), reviewId.toString()),
                RESPONSE_CODES.getHttpStatus()
        );
    }
}
