package com.triple.mileageservice.domain.review.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "review")
    private Set<ReviewPhotos> photos = new HashSet<>();

    private boolean isFirst = false;

    private boolean isDelete = false;

    @Builder
    private Review(UUID reviewId, String content, Users users, Place place) {
        this.reviewId = reviewId;
        this.content = content;
        this.users = users;
        this.place = place;
    }

    public void modifyReview(String content, Set<ReviewPhotos> photos) {
        this.content = content;
        changePhotos(photos);
    }

    public void changePhotos(Set<ReviewPhotos> newPhotos) {
        Set<ReviewPhotos> removeTarget = this.photos.stream()
                .filter(reviewPhotos -> !newPhotos.contains(reviewPhotos))
                .collect(Collectors.toSet());
        this.photos.removeAll(removeTarget);

        newPhotos.forEach(newPhoto -> {
            newPhoto.setReview(this);
            this.photos.add(newPhoto);
        });
    }

    public void deleteReview() {
        this.isDelete = true;
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
