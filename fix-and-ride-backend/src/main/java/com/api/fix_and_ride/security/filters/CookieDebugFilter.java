package com.api.fix_and_ride.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CookieDebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var cookies = request.getCookies();

        if (cookies == null) {
            System.out.println("No cookies received by Spring.");
        } else {
            System.out.println("Cookies received by Spring:");
            for (Cookie c : cookies) {
                System.out.println("- " + c.getName() + ": " + c.getValue());
            }
        }

        filterChain.doFilter(request, response);
    }
}

