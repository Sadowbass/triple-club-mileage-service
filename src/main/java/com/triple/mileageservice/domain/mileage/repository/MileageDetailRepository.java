package com.triple.mileageservice.domain.mileage.repository;

import com.triple.mileageservice.domain.mileage.entity.Mileage;
import com.triple.mileageservice.domain.mileage.entity.MileageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MileageDetailRepository extends JpaRepository<MileageDetail, Long> {

    List<MileageDetail> findAllByMileage(Mileage mileage);
}
