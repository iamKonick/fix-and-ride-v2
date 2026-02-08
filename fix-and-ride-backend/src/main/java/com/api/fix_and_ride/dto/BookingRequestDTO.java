package com.api.fix_and_ride.dto;

public record BookingRequestDTO (
     String serviceId,
     String serviceName,
     String userEmail,
     String startDate,
     String startTime,
     String endDate,
     String endTime,
     String cost
    ){

}