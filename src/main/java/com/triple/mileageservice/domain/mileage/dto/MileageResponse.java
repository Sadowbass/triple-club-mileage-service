package com.triple.mileageservice.domain.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class MileageResponse {

    private UUID userId;
    private Long numberOfReviews;
    private Integer mileage;
    private String detailPath;

    @QueryProjection
    public MileageResponse(UUID userId, Long numberOfReviews, Integer mileage) {
        this.userId = userId;
        this.numberOfReviews = numberOfReviews;
        this.mileage = !Objects.isNull(mileage) ? mileage : 0;
        this.detailPath = "/mileage/" + userId + "/details?pageNum=1";
    }
}
