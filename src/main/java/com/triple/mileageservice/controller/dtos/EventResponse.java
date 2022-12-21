package com.triple.mileageservice.controller.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.triple.mileageservice.common.ResponseCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventResponse {

    private int rspCode;
    private String rspMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

    public EventResponse(int rspCode, String rspMessage) {
        this(rspCode, rspMessage, null);
    }

    public EventResponse(ResponseCodes responseCodes, Object result) {
        this.rspCode = responseCodes.getRspCode();
        this.rspMessage = responseCodes.getRspMessage();
        this.result = result;
    }
}
