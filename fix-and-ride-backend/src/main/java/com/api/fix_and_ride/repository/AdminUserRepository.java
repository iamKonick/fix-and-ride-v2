package com.api.fix_and_ride.repository;

import com.api.fix_and_ride.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {
    Optional<AdminUserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
