package com.triple.mileageservice.domain.review.repository;

import com.triple.mileageservice.domain.place.entity.Place;

import java.util.UUID;

public interface ReviewRepositoryCustom {

    boolean existByUserIdAndPlaceId(UUID userId, UUID placeId);

    boolean existsByPlace(Place place);
}
