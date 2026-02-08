package com.api.fix_and_ride.service;

import com.api.fix_and_ride.config.exceptions.RefreshTokenException;
import com.api.fix_and_ride.controller.UserServiceController;
import com.api.fix_and_ride.dto.BookingRequestDTO;
import com.api.fix_and_ride.dto.BookingResponseDTO;
import com.api.fix_and_ride.dto.PriceResponseDTO;
import com.api.fix_and_ride.dto.ServiceItemResponseDTO;
import com.api.fix_and_ride.entity.*;
import com.api.fix_and_ride.repository.BookingRepository;
import com.api.fix_and_ride.repository.ServiceItemRepository;
import com.api.fix_and_ride.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor

public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepo;

    private final RefreshTokenService refreshTokenService;

    private static final String USER_ROLE = "USER";


    public ServiceItemEntity getServiceEntityByKeyName(String keyName) {
        return serviceItemRepository.findByKeyName(keyName)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public UserEntity getUserEntityByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() ->new RuntimeException("User not found"));
    }

    public ServiceItemResponseDTO getServiceDetails(String key){
        ServiceItemEntity entity = this.getServiceEntityByKeyName(key);

        return new ServiceItemResponseDTO(
                entity.getKeyName(),
                entity.getName(),
                entity.getPrice(),
                entity.getDescription()
        );
    }

    public PriceResponseDTO getServicePrice(
            String keyName,
            String refreshToken,
            HttpServletResponse res
    ){
        RefreshTokenEntity rt = refreshTokenService.validateRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));
        UserEntity user = userRepo.findByEmail(rt.getUserEmail())
                .orElseThrow(() -> new RefreshTokenException("User not found"));

        ServiceItemEntity serviceItem = this.getServiceEntityByKeyName(keyName);



        return new PriceResponseDTO(
                true,
                calculateNewPrice(user, serviceItem.getPrice()),
                "Price calculated successfully"
        );
    }

    // Discount logic
    public double calculateNewPrice(UserEntity user, double originalPrice) {


        return originalPrice;
    }

    public BookingResponseDTO confirmBooking(BookingRequestDTO reqDTO) {
        // TODO: Save booking to database (JPA repository)


        ServiceItemEntity serviceItemEntity = this.getServiceEntityByKeyName(reqDTO.serviceId());
        UserEntity user = getUserEntityByEmail(reqDTO.userEmail());


        LocalDateTime startDateTime = creteLocalDateTimeFromTimeDate(reqDTO.startDate(), reqDTO.startTime());
        LocalDateTime endDateTime = creteLocalDateTimeFromTimeDate(reqDTO.endDate(), reqDTO.endTime());

        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        booking.setService(serviceItemEntity);
        booking.setStartDateTime(startDateTime);
        booking.setEndDateTime(endDateTime);
        booking.setCost(parseCost(reqDTO.cost()));
        booking.setStatus(BookingStatus.PENDING);

        BookingEntity savedBooking = bookingRepository.save(booking);
        Long generatedId = savedBooking.getBookingId();

        System.out.println("Generated booking ID: " + generatedId);

        return BookingResponseDTO.mapToDTO(
                savedBooking
        );
    }

    public LocalDateTime creteLocalDateTimeFromTimeDate(String date, String time){
        return LocalDateTime.of(
                LocalDate.parse(date),
                LocalTime.parse(time)
        );
    }

    private BigDecimal parseCost(String cost) {
        if (cost == null || cost.isBlank()) {
            throw new IllegalArgumentException("Cost is required");
        }

        // Remove currency symbols and spaces (€, $, £, etc.)
        String sanitized = cost.replaceAll("[^0-9.]", "");

        try {
            return new BigDecimal(sanitized);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cost format: " + cost);
        }
    }
}
