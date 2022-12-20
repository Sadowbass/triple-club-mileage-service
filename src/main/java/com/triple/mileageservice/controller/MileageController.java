package com.triple.mileageservice.controller;

import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.util.UUIDUtils;
import com.triple.mileageservice.controller.dtos.EventResponse;
import com.triple.mileageservice.domain.mileage.dto.MileageDetailResponse;
import com.triple.mileageservice.domain.mileage.dto.MileageResponse;
import com.triple.mileageservice.domain.mileage.repository.MileageRepository;
import com.triple.mileageservice.domain.user.exceptions.CannotFindUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileage")
public class MileageController {

    private final MileageRepository mileageRepository;

    @GetMapping("/{userId}")
    public EventResponse responseUserMileage(@PathVariable String userId) {
        UUID userUUID = UUIDUtils.stringToUUID("userId", userId);

        MileageResponse mileageResponse = mileageRepository.queryUserMileage(userUUID);
        if (null == mileageResponse.getUserId()) {
            throw new CannotFindUserException(userUUID.toString());
        }

        return new EventResponse(ResponseCodes.OK, mileageResponse);
    }

    @GetMapping("/{userId}/details")
    public EventResponse responseUserMileageDetail(
            @PathVariable String userId,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        UUID userUUID = UUIDUtils.stringToUUID("userId", userId);
        MileageDetailResponse response = mileageRepository.queryUserMileageDetails(userUUID, pageNum, order);

        return new EventResponse(ResponseCodes.OK, response);
    }
}
