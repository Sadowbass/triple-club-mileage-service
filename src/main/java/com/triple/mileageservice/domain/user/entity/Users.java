package com.triple.mileageservice.domain.user.entity;

import com.triple.mileageservice.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;

    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID userId;

    public Users(UUID userId) {
        this.userId = userId;
    }
}
