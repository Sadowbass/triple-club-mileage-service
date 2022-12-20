package com.triple.mileageservice.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TripleApiException extends RuntimeException {

    private int rspCode;
    private String rspMessage;
    private HttpStatus httpStatus;

    public TripleApiException(int rspCode, String rspMessage, HttpStatus httpStatus) {
        super(rspMessage);
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
        this.httpStatus = httpStatus;
    }
}
