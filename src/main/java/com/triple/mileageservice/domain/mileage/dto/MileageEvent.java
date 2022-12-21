package com.triple.mileageservice.domain.mileage.dto;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.user.entity.Users;
import lombok.*;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MileageEvent {

    private EventAction eventAction;

    private Long userSeqId;
    private Long reviewSeqId;

    private boolean beforeContent;
    private boolean afterContent;

    private boolean beforePhoto;
    private boolean afterPhoto;

    private boolean first;

    public static MileageEvent createMileageEvent(EventRequest eventRequest, Users user, Review review) {
        return MileageEvent.builder()
                .userSeqId(user.getSeqId())
                .reviewSeqId(review.getSeqId())
                .eventAction(eventRequest.getAction())
                .beforeContent(review.hasContent())
                .afterContent(!ObjectUtils.isEmpty(eventRequest.getContent()))
                .beforePhoto(review.hasPhoto())
                .afterPhoto(!ObjectUtils.isEmpty(eventRequest.getAttachedPhotoIds()))
                .first(review.isFirst())
                .build();
    }
}
