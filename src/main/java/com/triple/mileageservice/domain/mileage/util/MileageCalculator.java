package com.triple.mileageservice.domain.mileage.util;

import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MileageCalculator {

    public static int calcAdd(MileageEvent event) {
        int mileage = 0;

        mileage += sumIfTrue(event.isAfterContent());
        mileage += sumIfTrue(event.isAfterPhoto());
        mileage += sumIfTrue(event.isFirst());

        return mileage;
    }

    public static int calcMod(MileageEvent event) {
        int mileage = 0;

        mileage += compareCase(event.isBeforeContent(), event.isAfterContent());
        mileage += compareCase(event.isBeforePhoto(), event.isAfterPhoto());

        return mileage;
    }

    public static int calcDelete(MileageEvent event) {
        int mileage = 0;

        mileage += minusIfFalse(!event.isBeforeContent());
        mileage += minusIfFalse(!event.isBeforePhoto());
        mileage += minusIfFalse(!event.isFirst());

        return mileage;
    }

    private static int compareCase(boolean before, boolean after) {
        if (before) {
            return minusIfFalse(after);
        } else {
            return sumIfTrue(after);
        }
    }

    private static int minusIfFalse(boolean bool) {
        return bool ? 0 : -1;
    }

    private static int sumIfTrue(boolean bool) {
        return bool ? 1 : 0;
    }
}
