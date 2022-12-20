package com.triple.mileageservice.domain.user.exceptions;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class CannotFindUserException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODE = ResponseCodes.CANNOT_FIND_USER;

    public CannotFindUserException(String userId) {
        super(
                RESPONSE_CODE.getRspCode(),
                String.format(RESPONSE_CODE.getRspMessage(), userId),
                RESPONSE_CODE.getHttpStatus()
        );
    }
}
