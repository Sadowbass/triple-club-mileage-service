package com.triple.mileageservice.controller.dtos;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.common.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EventRequest {

    private EventType type;
    private EventAction action;
    private UUID reviewId;
    private String content;
    private Set<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;

    public EventRequest(EventType type, EventAction action, UUID reviewId, String content, Set<UUID> attachedPhotoIds, UUID userId, UUID placeId) {
        this.type = type;
        this.action = action;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }
}
