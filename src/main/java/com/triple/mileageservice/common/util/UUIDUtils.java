package com.triple.mileageservice.common.util;

import com.triple.mileageservice.controller.exceptions.WrongRequiredValueException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtils {

    public static UUID stringToUUID(String fieldName, String value) {
        try {
            Objects.requireNonNull(value);
            UUID uuid = UUID.fromString(value);
            if (uuid.toString().equals(value)) {
                return uuid;
            } else {
                throw new WrongRequiredValueException(fieldName);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new WrongRequiredValueException(fieldName);
        }
    }
}
