package com.triple.mileageservice.domain.review.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

import java.util.UUID;

public class UserAlreadyReviewException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODES = ResponseCodes.USER_ALREADY_REVIEW;

    public UserAlreadyReviewException(UUID userId, UUID placeId) {
        super(
                RESPONSE_CODES.getRspCode(),
                String.format(RESPONSE_CODES.getRspMessage(), userId.toString(), placeId.toString()),
                RESPONSE_CODES.getHttpStatus()
        );
    }
}
