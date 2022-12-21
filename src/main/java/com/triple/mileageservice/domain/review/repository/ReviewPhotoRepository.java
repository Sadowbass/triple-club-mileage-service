package com.triple.mileageservice.domain.review.repository;

import com.triple.mileageservice.domain.review.entity.ReviewPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhotos, Long>, ReviewPhotoRepositoryCustom {

}
