package com.triple.mileageservice.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.UUID;

import static com.triple.mileageservice.domain.place.entity.QPlace.place;
import static com.triple.mileageservice.domain.review.entity.QReview.review;
import static com.triple.mileageservice.domain.user.entity.QUsers.users;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existByUserIdAndPlaceId(UUID userId, UUID placeId) {
        Long result = queryFactory
                .select(review.seqId)
                .from(review)
                .join(review.users, users)
                .join(review.place, place)
                .where(users.userId.eq(userId), place.placeId.eq(placeId), review.isDelete.isFalse())
                .fetchFirst();

        return null != result;
    }
}
