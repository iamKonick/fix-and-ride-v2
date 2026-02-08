package com.api.fix_and_ride.controller;


import com.api.fix_and_ride.dto.BookingRequestDTO;
import com.api.fix_and_ride.dto.BookingResponseDTO;
import com.api.fix_and_ride.dto.PriceResponseDTO;
import com.api.fix_and_ride.dto.ServiceItemResponseDTO;
import com.api.fix_and_ride.service.ServiceItemService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/service")
@RequiredArgsConstructor
public class UserServiceController {

    private final ServiceItemService serviceItemService;

    // --------------------------------------------------
    // 1️⃣ Get FULL service details by key
    // --------------------------------------------------
    @GetMapping("/{keyName}")
    public ServiceItemResponseDTO getServiceDetails(
            @PathVariable String keyName) {
        return  serviceItemService.getServiceDetails(keyName);
    }

    // --------------------------------------------------
    // 2️⃣ Get discounted price using cookie + service key
    // --------------------------------------------------
    @GetMapping("/{keyName}/price")
    public PriceResponseDTO getServicePrice(
            @PathVariable String keyName,
            @CookieValue("refreshtoken") String refreshToken,
            HttpServletResponse request) {
        return serviceItemService.getServicePrice(
                keyName,refreshToken,request
        );
    }

    @PostMapping("/confirm-booking")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO confirmBooking(@RequestBody @Valid BookingRequestDTO reqDTO) {
        return serviceItemService.confirmBooking(reqDTO);
    }


}