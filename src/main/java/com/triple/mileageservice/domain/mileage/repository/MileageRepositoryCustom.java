package com.triple.mileageservice.domain.mileage.repository;

import com.triple.mileageservice.domain.mileage.dto.MileageResponse;
import com.triple.mileageservice.domain.mileage.dto.MileageDetailResponse;

import java.util.UUID;

public interface MileageRepositoryCustom {

    MileageResponse queryUserMileage(UUID userId);

    MileageDetailResponse queryUserMileageDetails(UUID userId, int pageNum, String order);
}
