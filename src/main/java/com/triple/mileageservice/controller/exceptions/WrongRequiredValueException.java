package com.triple.mileageservice.controller.exceptions;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class WrongRequiredValueException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODE = ResponseCodes.WRONG_REQUIRED_VALUE;

    public WrongRequiredValueException(String field) {
        super(
                RESPONSE_CODE.getRspCode(),
                String.format(RESPONSE_CODE.getRspMessage(), field),
                RESPONSE_CODE.getHttpStatus()
        );
    }
}
