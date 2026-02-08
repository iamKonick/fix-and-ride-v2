package com.api.fix_and_ride.security.filters;

import com.api.fix_and_ride.repository.AdminUserRepository;
import com.api.fix_and_ride.repository.UserRepository;
import com.api.fix_and_ride.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String cookieHeader = request.getHeader("Cookie");
        System.out.println("JwtAuthFilter->Raw Cookie header: " + cookieHeader);

        // 1) No Authorization header → let request continue unauthenticated
        if (header == null || !header.startsWith("Bearer ")) {
            // No token → anonymous request allowed
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // 2) Invalid token → continue without authentication
            if (!jwtService.isTokenValid(token)) {
                chain.doFilter(request, response);
                return;
            }
            // 3) Extract email + role
            String email = jwtService.extractUserEmail(token);
            String role  = jwtService.getRole(token);

            if (email == null || role == null) {
                chain.doFilter(request, response);
                return;
            }

            // 4) Skip if already authenticated
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                chain.doFilter(request, response);
                return;
            }

            // 5) Check DB user exists
            boolean exists = role.equals("ADMIN")
                    ? adminUserRepository.findByEmail(email).isPresent()
                    : userRepository.findByEmail(email).isPresent();

            if (!exists) {
                chain.doFilter(request, response);
                return;
            }

            // 6) Build authentication
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }
        // 7) Always continue filter chain
        chain.doFilter(request, response);

    }
}
