package com.triple.mileageservice.common;

import com.triple.mileageservice.controller.exceptions.WrongRequiredValueException;

public enum EventType {
    REVIEW;

    public static EventType fromString(String type) {
        try {
            return EventType.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new WrongRequiredValueException("type");
        }
    }
}
