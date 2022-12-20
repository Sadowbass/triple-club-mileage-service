package com.triple.mileageservice.domain.place.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID placeId;

    public Place(UUID placeId) {
        this.placeId = placeId;
    }
}
