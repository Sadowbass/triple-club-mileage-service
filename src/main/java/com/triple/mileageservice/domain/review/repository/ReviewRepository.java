package com.triple.mileageservice.domain.review.repository;

import com.triple.mileageservice.domain.place.entity.Place;
import com.triple.mileageservice.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existsByPlace(Place place);

    boolean existsByReviewId(UUID reviewId);

    Optional<Review> findByReviewId(UUID reviewId);

    @Query("select r from Review r join fetch r.users where r.seqId = :seqId")
    Optional<Review> findByIdUsingFetch(@Param("seqId") Long seqId);

    @Query("select r from Review r join fetch r.users where r.reviewId = :reviewId")
    Optional<Review> findByReviewIdUsingFetch(@Param("reviewId") UUID reviewId);
}
