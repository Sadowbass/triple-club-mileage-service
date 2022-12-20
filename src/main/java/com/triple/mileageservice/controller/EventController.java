package com.triple.mileageservice.controller;

import com.triple.mileageservice.common.EventType;
import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.controller.dtos.EventResponse;
import com.triple.mileageservice.controller.exceptions.WrongRequiredValueException;
import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import com.triple.mileageservice.domain.mileage.dto.MileageResponse;
import com.triple.mileageservice.domain.mileage.service.MileageService;
import com.triple.mileageservice.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final ReviewService reviewCommandService;
    private final MileageService mileageCommandService;

    @PostMapping(value = "/events", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public EventResponse events(EventRequest eventRequest) {
        if (eventRequest.getType() == EventType.REVIEW) {
            MileageEvent mileageEvent = reviewCommandService.doReviewEvent(eventRequest);
            MileageResponse mileageResponse = mileageCommandService.doMileageEvent(mileageEvent);

            return new EventResponse(ResponseCodes.OK, mileageResponse);
        } else {
            throw new WrongRequiredValueException("type");
        }
    }
}
