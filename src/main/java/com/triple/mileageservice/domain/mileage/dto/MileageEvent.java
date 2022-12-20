package com.triple.mileageservice.domain.mileage.dto;

import com.triple.mileageservice.common.EventAction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MileageEvent {

    private EventAction eventAction;

    private Long userSeqId;
    private Long reviewSeqId;

    private boolean beforeContent;
    private boolean afterContent;

    private boolean beforePhoto;
    private boolean afterPhoto;

    private boolean first;
}
