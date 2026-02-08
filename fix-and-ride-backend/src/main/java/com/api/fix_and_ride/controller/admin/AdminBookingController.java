package com.api.fix_and_ride.controller.admin;


import com.api.fix_and_ride.entity.BookingEntity;
import com.api.fix_and_ride.entity.BookingStatus;
import com.api.fix_and_ride.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/booking")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingRepository bookingRepo;

    // ADMIN: get all bookings
    @GetMapping
    public List<BookingEntity> allBookings(
            @AuthenticationPrincipal String adminEmail
    ) {
        return bookingRepo.findAll();
    }

    // ADMIN: get details of a booking
    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal String adminEmail
    ) {
        return bookingRepo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ADMIN: update booking status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal String adminEmail
    ) {
        var booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.valueOf(status));
        bookingRepo.save(booking);

        return ResponseEntity.ok(booking);
    }
}
