package com.triple.mileageservice.domain.place.repository;

import com.triple.mileageservice.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPlaceId(UUID placeId);
}
