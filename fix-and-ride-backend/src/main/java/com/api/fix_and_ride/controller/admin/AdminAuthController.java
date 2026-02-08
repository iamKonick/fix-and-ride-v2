package com.api.fix_and_ride.controller.admin;


import com.api.fix_and_ride.dto.AuthResponseDTO;
import com.api.fix_and_ride.dto.LoginRequestDTO;
import com.api.fix_and_ride.entity.AdminUserEntity;
import com.api.fix_and_ride.repository.BookingRepository;
import com.api.fix_and_ride.security.JwtService;
import com.api.fix_and_ride.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;
    private final BookingRepository bookingRepo;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO req) {

        AdminUserEntity admin = adminAuthService.validateAdminLogin(req);

        // Generate token with admin role encoded
        String token = jwtService.generateAccessToken(admin.getEmail(),"ADMIN");

        return ResponseEntity.ok(new AuthResponseDTO(token, null,null));
    }
}
