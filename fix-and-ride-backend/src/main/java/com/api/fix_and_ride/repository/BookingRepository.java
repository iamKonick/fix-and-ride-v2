package com.api.fix_and_ride.repository;

import com.api.fix_and_ride.entity.BookingEntity;
import com.api.fix_and_ride.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    // User can see only their own bookings
    List<BookingEntity> findByUser_Email(String email);

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findByServiceId(Long serviceId);

    List<BookingEntity> findByStatus(BookingStatus status);

    List<BookingEntity> findByUserIdAndStatus(Long userId, BookingStatus status);

    @Query("""
        SELECT b FROM BookingEntity b
        JOIN FETCH b.service
        WHERE b.user.id = :userId
    """)
    List<BookingEntity> findUserBookingsWithService(@Param("userId") Long userId);
}
