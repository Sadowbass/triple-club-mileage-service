package com.triple.mileageservice.domain.review.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
import com.triple.mileageservice.controller.dtos.EventRequest;
import com.triple.mileageservice.domain.place.entity.Place;
import com.triple.mileageservice.domain.user.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID reviewId;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeqId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeSeqId")
    private Place place;

    @OneToMany(mappedBy = "review", cascade = CascadeType.PERSIST)
    private Set<ReviewPhotos> photos = new HashSet<>();

    private boolean isFirst = false;

    private boolean isDelete = false;

    public static Review createNewReview(EventRequest eventRequest, Users user, Place place) {
        return Review.builder()
                .reviewId(eventRequest.getReviewId())
                .content(eventRequest.getContent())
                .users(user)
                .place(place)
                .build();
    }

    @Builder
    private Review(UUID reviewId, String content, Users users, Place place) {
        this.reviewId = reviewId;
        this.content = content;
        this.users = users;
        this.place = place;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void deleteReview() {
        this.isDelete = true;
    }

    public void addPhotos(Set<ReviewPhotos> newPhotos) {
        newPhotos.forEach(newPhoto -> {
            newPhoto.setReview(this);
            this.photos.add(newPhoto);
        });
    }

    public void mergePhotos(Set<ReviewPhotos> newPhotos) {
        Set<ReviewPhotos> insertTargets = newPhotos.stream()
                .filter(photo -> !photos.contains(photo))
                .collect(Collectors.toSet());
        addPhotos(insertTargets);
    }

    public Set<ReviewPhotos> deletePhotosAndGetTargetPhotos(Set<ReviewPhotos> newPhotos) {
        Set<ReviewPhotos> deleteTarget = photos.stream()
                .filter(photo -> !newPhotos.contains(photo))
                .collect(Collectors.toSet());
        photos.removeAll(deleteTarget);

        return deleteTarget;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean hasContent() {
        return !ObjectUtils.isEmpty(content);
    }

    public boolean hasPhoto() {
        return !ObjectUtils.isEmpty(photos);
    }
}
