package com.triple.mileageservice.controller;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;
import com.triple.mileageservice.controller.dtos.EventResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.triple.mileageservice.controller")
public class ControllerAdvice {

    @ExceptionHandler(TripleApiException.class)
    public ResponseEntity<EventResponse> handleTripleException(TripleApiException ex) {
        log.error("Response triple exception : [rspMsg : {}], [rspMessage : {}]", ex.getRspCode(), ex.getRspMessage());

        EventResponse eventResponse = new EventResponse(ex.getRspCode(), ex.getRspMessage());
        return new ResponseEntity<>(eventResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EventResponse> handleException(Exception ex) {
        log.error("Response internal error : [cause : {}]", ex.getMessage());

        EventResponse eventResponse = new EventResponse(ResponseCodes.INTERNAL_SERVER_ERROR, null);
        return new ResponseEntity<>(eventResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}