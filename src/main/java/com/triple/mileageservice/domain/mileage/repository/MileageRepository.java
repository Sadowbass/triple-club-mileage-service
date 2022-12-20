package com.triple.mileageservice.domain.mileage.repository;

import com.triple.mileageservice.domain.mileage.entity.Mileage;
import com.triple.mileageservice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileageRepository extends JpaRepository<Mileage, Long>, MileageRepositoryCustom {

    Optional<Mileage> findByReview(Review review);
}
