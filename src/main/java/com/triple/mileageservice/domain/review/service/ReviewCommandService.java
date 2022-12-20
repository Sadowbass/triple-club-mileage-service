package com.triple.mileageservice.domain.review.service;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.domain.mileage.dto.MileageEvent;
import com.triple.mileageservice.domain.place.entity.Place;
import com.triple.mileageservice.domain.place.exception.CannotFindPlaceException;
import com.triple.mileageservice.domain.place.repository.PlaceRepository;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.review.entity.ReviewPhotos;
import com.triple.mileageservice.domain.review.exception.*;
import com.triple.mileageservice.domain.review.repository.ReviewRepository;
import com.triple.mileageservice.domain.user.entity.Users;
import com.triple.mileageservice.domain.user.exceptions.CannotFindUserException;
import com.triple.mileageservice.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewCommandService implements ReviewService {

    private final UsersRepository usersRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public MileageEvent doReviewEvent(EventRequest eventRequest) {
        if (EventAction.ADD == eventRequest.getAction()) {
            return addReview(eventRequest);
        } else if (EventAction.MOD == eventRequest.getAction()) {
            return modReview(eventRequest);
        } else if (EventAction.DELETE == eventRequest.getAction()) {
            return deleteReview(eventRequest);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Unsupported %s Action", eventRequest.getAction().name())
            );
        }
    }

    private MileageEvent addReview(EventRequest eventRequest) {
        canAdd(eventRequest);
        Users user = findUserByUserId(eventRequest.getUserId());
        Place place = findPlaceByPlaceId(eventRequest.getPlaceId());

        Review newReview = createNewReview(eventRequest, user, place);
        newReview.setFirst(!reviewRepository.existsByPlace(place));
        newReview.changePhotos(createReviewPhotoFromUUID(eventRequest));

        reviewRepository.save(newReview);

        return createMileageEvent(eventRequest, user, newReview);
    }

    private void canAdd(EventRequest eventRequest) {
        if (reviewRepository.existByUserIdAndPlaceId(eventRequest.getUserId(), eventRequest.getPlaceId())) {
            throw new UserAlreadyReviewException(eventRequest.getUserId(), eventRequest.getPlaceId());
        }
        if (reviewRepository.existsByReviewId(eventRequest.getReviewId())) {
            throw new DuplicateReviewIdException(eventRequest.getReviewId());
        }
    }

    private Review createNewReview(EventRequest eventRequest, Users user, Place place) {
        return Review.builder()
                .reviewId(eventRequest.getReviewId())
                .content(eventRequest.getContent())
                .users(user)
                .place(place)
                .build();
    }

    private MileageEvent modReview(EventRequest eventRequest) {
        Review review = findReviewByReviewId(eventRequest.getReviewId());
        Users user = review.getUsers();
        canModOrDelete(user, review);

        MileageEvent mileageEvent = createMileageEvent(eventRequest, user, review);
        review.modifyReview(eventRequest.getContent(), createReviewPhotoFromUUID(eventRequest));

        return mileageEvent;
    }

    private MileageEvent deleteReview(EventRequest eventRequest) {
        Review review = findReviewByReviewId(eventRequest.getReviewId());
        Users user = review.getUsers();
        canModOrDelete(user, review);

        MileageEvent mileageEvent = createMileageEvent(eventRequest, user, review);
        review.deleteReview();

        return mileageEvent;
    }

    private Users findUserByUserId(UUID userId) {
        return usersRepository.findByUserId(userId)
                .orElseThrow(() -> new CannotFindUserException(userId.toString()));
    }

    private Place findPlaceByPlaceId(UUID placeId) {
        return placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new CannotFindPlaceException(placeId.toString()));
    }

    private Review findReviewByReviewId(UUID reviewId) {
        return reviewRepository.findByReviewIdUsingFetch(reviewId)
                .orElseThrow(() -> new CannotFindReviewException(reviewId.toString()));
    }

    private MileageEvent createMileageEvent(EventRequest eventRequest, Users user, Review review) {
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

    private Set<ReviewPhotos> createReviewPhotoFromUUID(EventRequest eventRequest) {
        return eventRequest.getAttachedPhotoIds().stream()
                .map(ReviewPhotos::new)
                .collect(Collectors.toSet());
    }

    private void canModOrDelete(Users user, Review review) {
        if (!isSameUser(user, review)) {
            throw new CannotModifyReviewException();
        }
        if (review.isDelete()) {
            throw new DeletedReviewException();
        }
    }

    private boolean isSameUser(Users user, Review review) {
        return Objects.equals(review.getUsers().getSeqId(), user.getSeqId());
    }
}