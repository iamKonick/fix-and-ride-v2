package com.api.fix_and_ride.repository;

import com.api.fix_and_ride.entity.ServiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceItemRepository extends JpaRepository<ServiceItemEntity, Long> {
    boolean existsByKeyName(String keyName);

    Optional<ServiceItemEntity> findByKeyName(String keyName);
}
