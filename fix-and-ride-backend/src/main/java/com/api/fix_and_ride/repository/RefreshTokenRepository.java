package com.api.fix_and_ride.repository;

import com.api.fix_and_ride.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserEmail(String email);

    Optional<RefreshTokenEntity> findByUserEmail(String email);
}
