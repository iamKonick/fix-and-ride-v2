package com.api.fix_and_ride.repository;


 import com.api.fix_and_ride.entity.UserEntity;
 import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
