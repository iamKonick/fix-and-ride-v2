package com.api.fix_and_ride.controller;

import com.api.fix_and_ride.dto.AuthResponseDTO;
import com.api.fix_and_ride.dto.LoginRequestDTO;
import com.api.fix_and_ride.dto.SignupRequestDTO;
import com.api.fix_and_ride.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid SignupRequestDTO req) {
        authService.signup(req);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginRequestDTO req,
                                 HttpServletResponse res) {
        return authService.login(req.email, req.password, res);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(
            @CookieValue("refreshtoken") String refreshToken,
            HttpServletResponse res) {
        return authService.refresh(refreshToken, res);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(
            @CookieValue("refreshtoken") String refreshToken,
            HttpServletResponse res) {
        authService.logout(refreshToken, res);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of("username", auth.getName());
    }
}

