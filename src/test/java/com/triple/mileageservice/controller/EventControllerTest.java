package com.triple.mileageservice.controller;

import com.triple.mileageservice.common.EventAction;
import com.triple.mileageservice.common.EventType;
import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.domain.mileage.entity.Mileage;
import com.triple.mileageservice.domain.mileage.entity.MileageDetail;
import com.triple.mileageservice.domain.mileage.repository.MileageDetailRepository;
import com.triple.mileageservice.domain.mileage.repository.MileageRepository;
import com.triple.mileageservice.domain.place.entity.Place;
import com.triple.mileageservice.domain.place.repository.PlaceRepository;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.review.exception.DuplicateReviewIdException;
import com.triple.mileageservice.domain.review.exception.UserAlreadyReviewException;
import com.triple.mileageservice.domain.review.repository.ReviewRepository;
import com.triple.mileageservice.domain.user.entity.Users;
import com.triple.mileageservice.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class EventControllerTest {

    @Autowired
    EventController eventController;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MileageRepository mileageRepository;
    @Autowired
    MileageDetailRepository detailRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    void addReview() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setContent("content");
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.ADD);

        Set<UUID> photos = new HashSet<>();
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        eventRequest.setAttachedPhotoIds(photos);

        //when
        eventController.events(eventRequest);
        em.flush();
        em.clear();

        //then
        Review savedReview = reviewRepository.findByReviewId(reviewId).orElseThrow();
        assertThat(savedReview.isFirst()).isTrue();
        assertThat(savedReview.getPhotos()).hasSize(3);

        Mileage savedMileage = mileageRepository.findByReview(savedReview).orElseThrow();
        assertThat(savedMileage.getReviewMileage()).isEqualTo(3);

        List<MileageDetail> details = detailRepository.findAllByMileage(savedMileage);
        assertThat(details).hasSize(1);
        assertThat(details.get(0).getChangedMileage()).isEqualTo(3);
    }

    @Test
    @Transactional
    void addReviewWithDuplicateReviewId() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        addDefault(userId, placeId, reviewId);

        //when
        UUID newPlaceId = UUID.randomUUID();
        placeRepository.save(new Place(newPlaceId));

        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(newPlaceId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.ADD);

        Set<UUID> photos = new HashSet<>();
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        eventRequest.setAttachedPhotoIds(photos);

        //then
        assertThatThrownBy(() -> eventController.events(eventRequest))
                .isInstanceOf(DuplicateReviewIdException.class);
    }

    @Test
    @Transactional
    void addReviewWithSameUserAndPlace() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        addDefault(userId, placeId, reviewId);

        //when
        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(UUID.randomUUID());
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.ADD);

        Set<UUID> photos = new HashSet<>();
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        eventRequest.setAttachedPhotoIds(photos);

        //then
        assertThatThrownBy(() -> eventController.events(eventRequest))
                .isInstanceOf(UserAlreadyReviewException.class);
    }

    @Test
    @Transactional
    void modDifferentCondition() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        addDefault(userId, placeId, reviewId);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setContent("content");
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.MOD);

        Set<UUID> photos = new HashSet<>();
        eventRequest.setAttachedPhotoIds(photos);

        //when
        eventController.events(eventRequest);
        em.flush();
        em.clear();

        //then
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow();

        Mileage mileage = mileageRepository.findByReview(review).orElseThrow();
        assertThat(mileage.getReviewMileage()).isEqualTo(2);

        List<MileageDetail> allByMileage = detailRepository.findAllByMileage(mileage);
        assertThat(allByMileage).hasSize(2);
        int calculatedMileage = allByMileage.stream()
                .mapToInt(MileageDetail::getChangedMileage)
                .sum();
        assertThat(calculatedMileage).isEqualTo(2);
    }

    @Test
    @Transactional
    void modSameCondition() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        addDefault(userId, placeId, reviewId);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setContent("content");
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.MOD);

        Set<UUID> photos = new HashSet<>();
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        eventRequest.setAttachedPhotoIds(photos);

        //when
        eventController.events(eventRequest);
        em.flush();
        em.clear();

        //then
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow();

        Mileage mileage = mileageRepository.findByReview(review).orElseThrow();
        assertThat(mileage.getReviewMileage()).isEqualTo(3);

        List<MileageDetail> allByMileage = detailRepository.findAllByMileage(mileage);
        assertThat(allByMileage).hasSize(1);
        int calculatedMileage = allByMileage.stream()
                .mapToInt(MileageDetail::getChangedMileage)
                .sum();
        assertThat(calculatedMileage).isEqualTo(3);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        //given
        UUID userId = UUID.randomUUID();
        usersRepository.save(new Users(userId));
        UUID placeId = UUID.randomUUID();
        placeRepository.save(new Place(placeId));
        UUID reviewId = UUID.randomUUID();

        addDefault(userId, placeId, reviewId);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.DELETE);

        Set<UUID> photos = new HashSet<>();
        eventRequest.setAttachedPhotoIds(photos);

        //when
        eventController.events(eventRequest);
        em.flush();
        em.clear();

        //then
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow();

        Mileage mileage = mileageRepository.findByReview(review).orElseThrow();
        assertThat(mileage.getReviewMileage()).isZero();

        List<MileageDetail> allByMileage = detailRepository.findAllByMileage(mileage);
        assertThat(allByMileage).hasSize(2);
        int calculatedMileage = allByMileage.stream()
                .mapToInt(MileageDetail::getChangedMileage)
                .sum();
        assertThat(calculatedMileage).isZero();
    }

    private void addDefault(UUID userId, UUID placeId, UUID reviewId) {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setUserId(userId);
        eventRequest.setPlaceId(placeId);
        eventRequest.setReviewId(reviewId);
        eventRequest.setContent("content");
        eventRequest.setType(EventType.REVIEW);
        eventRequest.setAction(EventAction.ADD);

        Set<UUID> photos = new HashSet<>();
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        photos.add(UUID.randomUUID());
        eventRequest.setAttachedPhotoIds(photos);

        eventController.events(eventRequest);
        em.flush();
        em.clear();
    }
}