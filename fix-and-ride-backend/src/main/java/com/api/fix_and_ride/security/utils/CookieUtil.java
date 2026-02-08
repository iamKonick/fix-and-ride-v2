package com.api.fix_and_ride.security.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CookieUtil {

    @Autowired
    private Environment env;
    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        System.out.println("CookieUtil: cookie size in byte: -- "+token.getBytes(StandardCharsets.UTF_8).length);
        ResponseCookie cookie = ResponseCookie.from("refreshtoken", token)
                .httpOnly(true)
                .secure(true)
                //.sameSite("Lax")
                .sameSite("None")
                .path("/api/user")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString()+"; Partitioned");
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshtoken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                //.sameSite("Lax")
                .path("/api/user")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString()+"; Partitioned");
    }
}
