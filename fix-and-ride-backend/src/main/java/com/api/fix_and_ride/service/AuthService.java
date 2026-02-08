package com.api.fix_and_ride.service;


import com.api.fix_and_ride.config.exceptions.InvalidCredentialsException;
import com.api.fix_and_ride.config.exceptions.RefreshTokenException;
import com.api.fix_and_ride.dto.AuthResponseDTO;
import com.api.fix_and_ride.dto.SignupRequestDTO;
import com.api.fix_and_ride.dto.UserDTO;
import com.api.fix_and_ride.entity.RefreshTokenEntity;
import com.api.fix_and_ride.entity.UserEntity;
import com.api.fix_and_ride.repository.UserRepository;
import com.api.fix_and_ride.security.JwtService;
import com.api.fix_and_ride.security.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final CookieUtil cookieUtil;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private static final String USER_ROLE = "USER";

    public void signup(SignupRequestDTO req) {
        if (userRepo.existsByEmail(req.email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        userRepo.save(
                new UserEntity(
                        req.firstName + " " + req.lastName,
                        req.address,
                        req.houseNo,
                        req.post,
                        req.city,
                        req.email,
                        encoder.encode(req.password)
                )
        );
    }

    public AuthResponseDTO login(String email, String password, HttpServletResponse res) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), USER_ROLE);

        RefreshTokenEntity rt = refreshTokenService.createRefreshToken(user.getEmail());
        cookieUtil.addRefreshTokenCookie(res, rt.getToken());

        return new AuthResponseDTO(accessToken, UserDTO.fromEntityToDTO(user), null);
    }

    public AuthResponseDTO refresh(String refreshToken, HttpServletResponse res) {
        RefreshTokenEntity rt = refreshTokenService.validateRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));

        UserEntity user = userRepo.findByEmail(rt.getUserEmail())
                .orElseThrow(() -> new RefreshTokenException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), USER_ROLE);
        RefreshTokenEntity newRt = refreshTokenService.createRefreshToken(user.getEmail());

        cookieUtil.addRefreshTokenCookie(res, newRt.getToken());
        return new AuthResponseDTO(newAccessToken, UserDTO.fromEntityToDTO(user), null);
    }

    public void logout(String refreshToken, HttpServletResponse res) {
        RefreshTokenEntity rt = refreshTokenService.validateRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));

        refreshTokenService.deleteTokensForUser(rt.getUserEmail());
        cookieUtil.clearRefreshTokenCookie(res);
    }

    public void get(){}
}
