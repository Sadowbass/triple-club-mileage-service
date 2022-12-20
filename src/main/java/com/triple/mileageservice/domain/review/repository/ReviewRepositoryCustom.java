package com.triple.mileageservice.domain.review.repository;

import java.util.UUID;

public interface ReviewRepositoryCustom {

    boolean existByUserIdAndPlaceId(UUID userId, UUID placeId);
}
