package com.triple.mileageservice.common;

import com.triple.mileageservice.controller.exceptions.WrongRequiredValueException;

public enum EventAction {
    ADD, MOD, DELETE;

    public static EventAction fromString(String action) {
        try {
            return EventAction.valueOf(action);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new WrongRequiredValueException("action");
        }
    }
}