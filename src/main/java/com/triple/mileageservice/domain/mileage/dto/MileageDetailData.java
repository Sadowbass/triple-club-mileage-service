package com.triple.mileageservice.domain.mileage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.triple.mileageservice.common.EventAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MileageDetailData {

    private UUID reviewId;
    private EventAction eventAction;
    private Integer changedMileage;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @QueryProjection
    public MileageDetailData(UUID reviewId, EventAction eventAction, Integer changedMileage, String reason, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.eventAction = eventAction;
        this.changedMileage = changedMileage;
        this.reason = reason;
        this.createdAt = createdAt;
    }
}
