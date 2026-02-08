package com.api.fix_and_ride.config;

import com.api.fix_and_ride.entity.AdminUserEntity;
import com.api.fix_and_ride.entity.BookingEntity;
import com.api.fix_and_ride.entity.BookingStatus;
import com.api.fix_and_ride.entity.UserEntity;
import com.api.fix_and_ride.repository.AdminUserRepository;
import com.api.fix_and_ride.repository.BookingRepository;
import com.api.fix_and_ride.repository.ServiceItemRepository;
import com.api.fix_and_ride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final AdminUserRepository adminRepo;
    private final ServiceItemRepository serviceRepo;
    private final BookingRepository bookingRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        // ---- USER CREATION ----
        if (userRepo.count() == 0) {
            // Demo user 1 - anyone@gmail.com (the main test user)
            userRepo.save(new UserEntity(
                    "Anyone User",
                    "123 Street",
                    "1",
                    "12345",
                    "City",
                    "anyone@gmail.com",
                    encoder.encode("sinbad123")));

            // Demo user 2
            userRepo.save(new UserEntity(
                    "John Cena",
                    "Street 1",
                    "12B",
                    "1000",
                    "CityOne",
                    "cena@fix.com",
                    encoder.encode("password123")));

            // Demo user 3
            userRepo.save(new UserEntity(
                    "Jane Wick",
                    "Street 2",
                    "44",
                    "2000",
                    "CityTwo",
                    "wick@fix.com",
                    encoder.encode("password123")));

            // Demo user 4
            userRepo.save(new UserEntity(
                    "Alice Smith",
                    "Main Avenue",
                    "99",
                    "3000",
                    "Downtown",
                    "alice@example.com",
                    encoder.encode("password123")));
        }

        // ---- ADMIN CREATION ----
        if (adminRepo.count() == 0) {
            AdminUserEntity admin = new AdminUserEntity(
                    "admin@fix-and-ride.com",
                    encoder.encode("admin123"));

            adminRepo.save(admin);
        }

        // ---------- BOOKINGS ----------
        if (bookingRepo.count() == 0) {

            var anyoneUser = userRepo.findByEmail("anyone@gmail.com").orElseThrow();
            var user1 = userRepo.findByEmail("cena@fix.com").orElseThrow();
            var user2 = userRepo.findByEmail("wick@fix.com").orElseThrow();
            var user3 = userRepo.findByEmail("alice@example.com").orElseThrow();

            var taxi = serviceRepo.findByKeyName("taxi").orElseThrow();
            var repairs = serviceRepo.findByKeyName("repairs").orElseThrow();
            var carLabor = serviceRepo.findByKeyName("car-labor").orElseThrow();
            var toolLending = serviceRepo.findByKeyName("tool-lending").orElseThrow();

            // Booking 1: anyone@gmail.com - tomorrow 10:00 - 11:00, PENDING
            BookingEntity booking1 = new BookingEntity();
            booking1.setUser(anyoneUser);
            booking1.setService(carLabor);
            booking1.setStartDateTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            booking1.setEndDateTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
            booking1.setCost(new BigDecimal("35.00"));
            booking1.setStatus(BookingStatus.PENDING);
            bookingRepo.save(booking1);

            // Booking 2: John Cena - tomorrow 14:00 - 16:00, ACCEPTED
            BookingEntity booking2 = new BookingEntity();
            booking2.setUser(user1);
            booking2.setService(taxi);
            booking2.setStartDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
            booking2.setEndDateTime(LocalDateTime.now().plusDays(1).withHour(16).withMinute(0));
            booking2.setCost(new BigDecimal("50.00"));
            booking2.setStatus(BookingStatus.ACCEPTED);
            bookingRepo.save(booking2);

            // Booking 3: Jane Wick - in 3 days 14:00 - 16:30, ACCEPTED
            BookingEntity booking3 = new BookingEntity();
            booking3.setUser(user2);
            booking3.setService(repairs);
            booking3.setStartDateTime(LocalDateTime.now().plusDays(3).withHour(14).withMinute(0));
            booking3.setEndDateTime(LocalDateTime.now().plusDays(3).withHour(16).withMinute(30));
            booking3.setCost(new BigDecimal("120.50"));
            booking3.setStatus(BookingStatus.ACCEPTED);
            bookingRepo.save(booking3);

            // Booking 4: Alice Smith - yesterday (completed), DONE
            BookingEntity booking4 = new BookingEntity();
            booking4.setUser(user3);
            booking4.setService(toolLending);
            booking4.setStartDateTime(LocalDateTime.now().minusDays(1).withHour(9).withMinute(0));
            booking4.setEndDateTime(LocalDateTime.now().minusDays(1).withHour(10).withMinute(0));
            booking4.setCost(new BigDecimal("15.00"));
            booking4.setStatus(BookingStatus.DONE);
            bookingRepo.save(booking4);

            // Booking 5: anyone@gmail.com - in 5 days, PENDING
            BookingEntity booking5 = new BookingEntity();
            booking5.setUser(anyoneUser);
            booking5.setService(repairs);
            booking5.setStartDateTime(LocalDateTime.now().plusDays(5).withHour(11).withMinute(0));
            booking5.setEndDateTime(LocalDateTime.now().plusDays(5).withHour(13).withMinute(0));
            booking5.setCost(new BigDecimal("85.00"));
            booking5.setStatus(BookingStatus.PENDING);
            bookingRepo.save(booking5);
        }

        System.out.println("[DataInitializer] Initial data loaded.");
    }
}
