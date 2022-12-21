package com.triple.mileageservice.domain.mileage.entity;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.common.entity.BaseEntity;
import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.user.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MileageDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeqId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewSeqId")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mileageSeqId")
    private Mileage mileage;

    @Enumerated(EnumType.STRING)
    private EventAction eventAction;
    private Integer changedMileage;
    private String reason;

    public static MileageDetail createNewMileageDetail(Users user, Review review, Mileage mileage, int changedMileage, MileageEvent mileageEvent) {
        return MileageDetail.builder()
                .users(user)
                .review(review)
                .mileage(mileage)
                .changedMileage(changedMileage)
                .mileageEvent(mileageEvent)
                .build();
    }

    @Builder
    private MileageDetail(Users users, Review review, Mileage mileage, int changedMileage, MileageEvent mileageEvent) {
        this.users = users;
        this.review = review;
        this.mileage = mileage;
        this.changedMileage = changedMileage;
        this.eventAction = mileageEvent.getEventAction();
        this.reason = createReason(mileageEvent);
    }

    private String createReason(MileageEvent mileageEvent) {
        String reason = "";

        if (mileageEvent.getEventAction() == EventAction.ADD) {
            reason = "new review";
        } else if (mileageEvent.getEventAction() == EventAction.MOD) {
            reason = createModifiedReason(mileageEvent, reason);
        } else {
            reason = "delete";
        }

        return reason.trim();
    }

    private String createModifiedReason(MileageEvent mileageEvent, String reason) {
        if (isChange(mileageEvent.isBeforeContent(), mileageEvent.isAfterContent())) {
            reason += " content";
        }
        if (isChange(mileageEvent.isBeforePhoto(), mileageEvent.isAfterPhoto())) {
            reason += " photo";
        }

        return reason;
    }

    private boolean isChange(boolean before, boolean after) {
        return before != after;
    }
}
