package com.api.fix_and_ride.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PriceResponseDTO(boolean success, double price, String message){}