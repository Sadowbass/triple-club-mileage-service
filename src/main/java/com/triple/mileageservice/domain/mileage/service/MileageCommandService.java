package com.triple.mileageservice.domain.mileage.service;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import com.triple.mileageservice.domain.mileage.dto.MileageResponse;
import com.triple.mileageservice.domain.mileage.entity.Mileage;
import com.triple.mileageservice.domain.mileage.entity.MileageDetail;
import com.triple.mileageservice.domain.mileage.repository.MileageDetailRepository;
import com.triple.mileageservice.domain.mileage.repository.MileageRepository;
import com.triple.mileageservice.domain.mileage.util.MileageCalculator;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.review.repository.ReviewRepository;
import com.triple.mileageservice.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MileageCommandService implements MileageService {

    private final MileageRepository mileageRepository;
    private final MileageDetailRepository detailRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public MileageResponse doMileageEvent(MileageEvent mileageEvent) {
        EventAction eventAction = mileageEvent.getEventAction();

        if (isAdd(eventAction)) {
            return addMileage(mileageEvent);
        } else if (isMod(eventAction) || isDelete(eventAction)) {
            return modOrDeleteMileage(mileageEvent);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Unsupported %s Action", eventAction.name())
            );
        }
    }

    private MileageResponse addMileage(MileageEvent mileageEvent) {
        Review review = findReviewByReviewSeqFetch(mileageEvent.getReviewSeqId());
        Users user = review.getUsers();

        int reviewMileage = MileageCalculator.calcAdd(mileageEvent);
        Mileage mileage = new Mileage(user, review, reviewMileage);
        MileageDetail mileageDetail = createNewMileageDetail(user, review, mileage, reviewMileage, mileageEvent);

        mileageRepository.save(mileage);
        detailRepository.save(mileageDetail);

        return mileageRepository.queryUserMileage(user.getUserId());
    }

    private MileageResponse modOrDeleteMileage(MileageEvent mileageEvent) {
        Review review = findReviewByReviewSeqFetch(mileageEvent.getReviewSeqId());
        Users user = review.getUsers();

        int changedMileage = mileageEvent.getEventAction() == EventAction.MOD ?
                MileageCalculator.calcMod(mileageEvent) : MileageCalculator.calcDelete(mileageEvent);

        if (isChange(changedMileage)) {
            Mileage mileage = findMileageByReview(review);
            mileage.changeMileage(changedMileage);

            MileageDetail mileageDetail = createNewMileageDetail(user, review, mileage, changedMileage, mileageEvent);
            detailRepository.save(mileageDetail);
        }

        return mileageRepository.queryUserMileage(user.getUserId());
    }

    private boolean isAdd(EventAction eventAction) {
        return typeOf(eventAction, EventAction.ADD);
    }

    private boolean isMod(EventAction eventAction) {
        return typeOf(eventAction, EventAction.MOD);
    }

    private boolean isDelete(EventAction eventAction) {
        return typeOf(eventAction, EventAction.DELETE);
    }

    private boolean typeOf(EventAction requestAction, EventAction expectAction) {
        return requestAction == expectAction;
    }

    private Review findReviewByReviewSeqFetch(Long reviewSeqId) {
        return reviewRepository.findByIdUsingFetch(reviewSeqId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("cannot find review by pk : %d", reviewSeqId)));
    }

    private Mileage findMileageByReview(Review review) {
        return mileageRepository.findByReview(review)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("cannot find mileage by review : %s", review.getReviewId()))
                );
    }

    private MileageDetail createNewMileageDetail(Users user, Review review, Mileage mileage, int changedMileage, MileageEvent mileageEvent) {
        return MileageDetail.builder()
                .users(user)
                .review(review)
                .mileage(mileage)
                .changedMileage(changedMileage)
                .mileageEvent(mileageEvent)
                .build();
    }

    private boolean isChange(int changedMileage) {
        return changedMileage != 0;
    }
}
