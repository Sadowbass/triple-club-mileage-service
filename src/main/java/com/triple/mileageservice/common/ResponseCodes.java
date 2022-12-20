package com.triple.mileageservice.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCodes {
    OK(0, "Request Success.", HttpStatus.OK),

    CANNOT_FIND_USER(11, "Cannot find user. userId : %s", HttpStatus.BAD_REQUEST),
    CANNOT_FIND_PLACE(12, "Cannot find place. placeId : %s", HttpStatus.BAD_REQUEST),
    CANNOT_FIND_REVIEW(13, "Cannot find review. review Id : %s", HttpStatus.BAD_REQUEST),
    USER_ALREADY_REVIEW(14, "User already review this place. %s, %s", HttpStatus.BAD_REQUEST),
    DUPLICATE_REVIEW_ID(15, "Duplicated review id. %s", HttpStatus.BAD_REQUEST),
    CANNOT_MODIFY_REVIEW(16, "User and reviewed user are different", HttpStatus.BAD_REQUEST),
    ALREADY_DELETED_REVIEW(17, "Review already deleted", HttpStatus.BAD_REQUEST),

    EMPTY_REQUIRED_VALUE(21, "Empty required field. field : %s", HttpStatus.BAD_REQUEST),
    WRONG_REQUIRED_VALUE(22, "Wrong required value. field : %s", HttpStatus.BAD_REQUEST),
    WRONG_REQUEST_MESSAGE(23, "Wrong request message", HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(99, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int rspCode;
    private final String rspMessage;
    private final HttpStatus httpStatus;

    ResponseCodes(int rspCode, String rspMessage, HttpStatus httpStatus) {
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
        this.httpStatus = httpStatus;
    }
}
