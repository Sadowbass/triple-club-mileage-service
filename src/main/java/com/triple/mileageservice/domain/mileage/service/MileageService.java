package com.triple.mileageservice.domain.mileage.service;

import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import com.triple.mileageservice.domain.mileage.dto.MileageResponse;

public interface MileageService {

    MileageResponse doMileageEvent(MileageEvent mileageEvent);
}
