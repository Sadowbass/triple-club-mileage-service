package com.triple.mileageservice.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileageservice.domain.review.entity.ReviewPhotos;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.Collection;

import static com.triple.mileageservice.domain.review.entity.QReviewPhotos.reviewPhotos;

public class ReviewPhotoRepositoryImpl implements ReviewPhotoRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ReviewPhotoRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void deletePhotos(Collection<ReviewPhotos> deleteTargets) {
        if (ObjectUtils.isEmpty(deleteTargets)) {
            return;
        }

        queryFactory.delete(reviewPhotos)
                .where(reviewPhotos.in(deleteTargets))
                .execute();
        em.flush();
        em.clear();
    }
}
