package com.triple.mileageservice.domain.review.service;

import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.domain.mileage.dto.MileageEvent;

public interface ReviewService {

    MileageEvent doReviewEvent(EventRequest eventRequest);
}
