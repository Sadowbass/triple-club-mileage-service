package com.triple.mileageservice.domain.review.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileageservice.domain.place.entity.Place;

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
                .where(allEq(userId, placeId), notDeleted())
                .fetchFirst();

        return null != result;
    }

    @Override
    public boolean existsByPlace(Place place) {
        Long result = queryFactory
                .select(review.seqId)
                .from(review)
                .where(review.place.eq(place), notDeleted())
                .fetchFirst();

        return null != result;
    }

    private BooleanExpression allEq(UUID userId, UUID placeId) {
        return userIdEq(userId).and(placeIdEq(placeId));
    }

    private BooleanExpression userIdEq(UUID userId) {
        return users.userId.eq(userId);
    }

    private BooleanExpression placeIdEq(UUID placeId) {
        return place.placeId.eq(placeId);
    }

    private BooleanExpression notDeleted() {
        return review.isDelete.isFalse();
    }
}
