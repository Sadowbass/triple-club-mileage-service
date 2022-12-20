package com.triple.mileageservice.controller.dtos;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.common.EventType;
import com.triple.mileageservice.common.ResponseCodes;
import com.triple.mileageservice.common.exceptions.TripleApiException;
import com.triple.mileageservice.common.util.UUIDUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class EventRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(EventRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        ObjectMapper om = new ObjectMapper();
        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        try {
            return om.readValue(requestBody, TempEventRequest.class)
                    .toEventRequest();
        } catch (JsonParseException ex) {
            log.error("parse failed. check request message : {}", requestBody);
            throw new TripleApiException(
                    ResponseCodes.WRONG_REQUEST_MESSAGE.getRspCode(),
                    ResponseCodes.WRONG_REQUEST_MESSAGE.getRspMessage(),
                    ResponseCodes.WRONG_REQUEST_MESSAGE.getHttpStatus()
            );
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TempEventRequest {

        private String type;
        private String action;
        private String reviewId;
        private String content;
        private Set<String> attachedPhotoIds;
        private String userId;
        private String placeId;

        private EventRequest toEventRequest() {
            return EventRequest.builder()
                    .type(EventType.fromString(type))
                    .action(EventAction.fromString(action))
                    .reviewId(UUIDUtils.stringToUUID("reviewId", reviewId))
                    .content(content)
                    .attachedPhotoIds(createPhotoIds())
                    .userId(UUIDUtils.stringToUUID("userId", userId))
                    .placeId(UUIDUtils.stringToUUID("placeId", placeId))
                    .build();
        }

        private Set<UUID> createPhotoIds() {
            if (Objects.isNull(attachedPhotoIds)) {
                return new HashSet<>();
            }
            return attachedPhotoIds.stream()
                    .map(photoId -> UUIDUtils.stringToUUID("attachedPhotoIds", photoId))
                    .collect(Collectors.toSet());
        }
    }
}
