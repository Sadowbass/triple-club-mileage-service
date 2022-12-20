package com.triple.mileageservice.domain.mileage.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
import com.triple.mileageservice.domain.review.entity.Review;
import com.triple.mileageservice.domain.user.entity.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeqId")
    private Users users;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewSeqId")
    private Review review;

    @OneToMany(mappedBy = "mileage")
    private List<MileageDetail> mileageDetails = new ArrayList<>();

    private Integer reviewMileage = 0;

    public Mileage(Users users, Review review, int reviewMileage) {
        this.users = users;
        this.review = review;
        this.reviewMileage = reviewMileage;
    }

    public void changeMileage(int changedMileage) {
        this.reviewMileage += changedMileage;
    }
}
