package com.triple.mileageservice.domain.user.repository;

import com.triple.mileageservice.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(UUID userId);
}
