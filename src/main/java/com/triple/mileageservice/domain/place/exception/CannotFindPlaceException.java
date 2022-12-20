package com.triple.mileageservice.domain.place.exception;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;

public class CannotFindPlaceException extends TripleApiException {

    private static final ResponseCodes RESPONSE_CODE = ResponseCodes.CANNOT_FIND_PLACE;

    public CannotFindPlaceException(String placeId) {
        super(
                RESPONSE_CODE.getRspCode(),
                String.format(RESPONSE_CODE.getRspMessage(), placeId),
                RESPONSE_CODE.getHttpStatus()
        );
    }
}
