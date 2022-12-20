package com.triple.mileageservice.domain.mileage.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileageservice.domain.mileage.dto.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static com.triple.mileageservice.domain.mileage.entity.QMileage.mileage;
import static com.triple.mileageservice.domain.mileage.entity.QMileageDetail.mileageDetail;
import static com.triple.mileageservice.domain.review.entity.QReview.review;
import static com.triple.mileageservice.domain.user.entity.QUsers.users;

public class MileageRepositoryImpl implements MileageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MileageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MileageResponse queryUserMileage(UUID userId) {
        return queryFactory
                .select(
                        new QMileageResponse(
                                users.userId, mileage.seqId.count(), mileage.reviewMileage.sum()
                        )
                )
                .from(users)
                .leftJoin(review).on(review.users.eq(users))
                .leftJoin(mileage).on(mileage.review.eq(review))
                .where(userIdEq(userId))
                .fetchOne();
    }

    @Override
    public MileageDetailResponse queryUserMileageDetails(UUID userId, int pageNum, String order) {
        int defaultLimit = 20;
        if (pageNum < 1) {
            pageNum = 1;
        }

        Long count = getCount(userId);
        if (null == count) {
            return new MileageDetailResponse(userId, pageNum - 1, 0, 0L, null);
        }

        List<MileageDetailData> content = getDetailPage(userId, pageNum, defaultLimit, order);
        return new MileageDetailResponse(userId, pageNum - 1, getLastPage(count, defaultLimit), count, content);
    }

    private Long getCount(UUID userId) {
        return queryFactory
                .select(users.seqId.count())
                .from(review)
                .join(review.users, users)
                .join(mileage)
                .on(mileage.users.eq(users))
                .join(mileage.mileageDetails, mileageDetail)
                .where(userIdEq(userId))
                .fetchOne();
    }

    private List<MileageDetailData> getDetailPage(UUID userId, int pageNum, int defaultLimit, String order) {
        return queryFactory
                .select(
                        new QMileageDetailData(
                                review.reviewId,
                                mileageDetail.eventAction,
                                mileageDetail.changedMileage,
                                mileageDetail.reason,
                                mileageDetail.createdAt
                        )
                )
                .from(review)
                .join(review.users, users)
                .join(mileage)
                .on(mileage.users.eq(users))
                .join(mileage.mileageDetails, mileageDetail)
                .limit(defaultLimit)
                .offset((long) (pageNum - 1) * defaultLimit)
                .orderBy(order(order))
                .where(userIdEq(userId))
                .fetch();
    }

    private OrderSpecifier<?> order(String order) {
        return order.equals("asc") ? mileageDetail.seqId.asc() : mileageDetail.seqId.desc();
    }

    private int getLastPage(Long count, int defaultLimit) {
        int lastPage = (int) (count / defaultLimit);
        lastPage += count % defaultLimit > 0 ? 1 : 0;
        return lastPage;
    }

    private BooleanExpression userIdEq(UUID userId) {
        return users.userId.eq(userId);
    }
}
