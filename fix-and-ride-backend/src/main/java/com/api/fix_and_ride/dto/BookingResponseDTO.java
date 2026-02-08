package com.api.fix_and_ride.dto;

import com.api.fix_and_ride.entity.BookingEntity;

public record BookingResponseDTO(String bookingID,
                                 String serviceId,
                                 String serviceName,
                                 Long userId,
                                 String userEmail,
                                 String startDateTime,
                                 String endDateTime,
                                 String cost
) {


    public static BookingResponseDTO mapToDTO(BookingEntity booking) {
        return new BookingResponseDTO(
                booking.getBookingId().toString(),
                booking.getService().getKeyName(),
                booking.getService().getName(),
                booking.getUser().getId(),
                booking.getUser().getEmail(),
                booking.getStartDateTime().toString(),
                booking.getEndDateTime().toString(),
                booking.getCost().toString()
                );
    }

}