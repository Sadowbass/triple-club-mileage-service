package com.triple.mileageservice.domain.mileage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MileageDetailResponse {

    private UUID userId;
    private int currentPage;
    private int lastPage;
    private long totalCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MileageDetailData> details;
}
