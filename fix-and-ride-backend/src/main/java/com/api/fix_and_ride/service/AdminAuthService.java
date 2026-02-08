package com.api.fix_and_ride.service;

import com.api.fix_and_ride.dto.LoginRequestDTO;
import com.api.fix_and_ride.entity.AdminUserEntity;
import com.api.fix_and_ride.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserRepository adminRepo;
    private final BCryptPasswordEncoder encoder;

    public AdminUserEntity validateAdminLogin(LoginRequestDTO req) {
        var admin = adminRepo.findByEmail(req.email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!encoder.matches(req.password, admin.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return admin;
    }
}
