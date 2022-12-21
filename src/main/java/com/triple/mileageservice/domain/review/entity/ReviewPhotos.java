package com.triple.mileageservice.domain.review.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhotos extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewSeqId")
    private Review review;

    @Column(columnDefinition = "BINARY(16)")
    private UUID photoId;

    public static Set<ReviewPhotos> createReviewPhotoFromUUID(Set<UUID> attachedPhotoIds) {
        return attachedPhotoIds.stream()
                .map(ReviewPhotos::new)
                .collect(Collectors.toSet());
    }

    public ReviewPhotos(UUID photoId) {
        this.photoId = photoId;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public int hashCode() {
        return photoId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return Objects.equals(this.photoId, ((ReviewPhotos) obj).photoId);
    }
}
