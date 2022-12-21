package com.triple.mileageservice.domain.review.repository;

import com.triple.mileageservice.domain.review.entity.ReviewPhotos;

import java.util.Collection;

public interface ReviewPhotoRepositoryCustom {

    void deletePhotos(Collection<ReviewPhotos> deleteTargets);
}
