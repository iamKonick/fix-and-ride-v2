package com.api.fix_and_ride.controller;

import com.api.fix_and_ride.entity.BookingEntity;
import com.api.fix_and_ride.repository.BookingRepository;
import com.api.fix_and_ride.repository.ServiceItemRepository;
import com.api.fix_and_ride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/user/booking")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ServiceItemRepository serviceRepo;

    // USER creates booking
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @RequestParam Long serviceId,
            @AuthenticationPrincipal String email
    ) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        BookingEntity booking = new BookingEntity();


        return ResponseEntity.ok(bookingRepo.save(booking));
    }

    // USER sees own bookings
    @GetMapping("/my")
    public ResponseEntity<?> myBookings(
            @AuthenticationPrincipal String email
    ) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(bookingRepo.findByUser_Email(user.getEmail()));
    }

    // USER views a specific booking (only if belongs to them)
    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal String email
    ) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(booking);
    }
}
