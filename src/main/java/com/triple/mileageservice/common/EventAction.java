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

    public static boolean isAdd(EventAction eventAction) {
        return typeOf(eventAction, EventAction.ADD);
    }

    public static boolean isMod(EventAction eventAction) {
        return typeOf(eventAction, EventAction.MOD);
    }

    public static boolean isDelete(EventAction eventAction) {
        return typeOf(eventAction, EventAction.DELETE);
    }

    public static boolean typeOf(EventAction requestAction, EventAction expectAction) {
        return requestAction == expectAction;
    }
}