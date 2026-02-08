package com.api.fix_and_ride.security;

import com.api.fix_and_ride.repository.UserRepository;
import com.api.fix_and_ride.security.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                // .cors(Customizer.withDefaults())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/user/auth/**").permitAll()
                                                .requestMatchers("/api/admin/auth/login").permitAll()
                                                .requestMatchers("/api/admin/login").permitAll()
                                                .requestMatchers("/admin/**").permitAll() // Allow static admin pages
                                                // Admin-protected endpoints
                                                .requestMatchers("/api/admin/booking/**", "/api/admin/booking").hasRole("ADMIN")
                                                .requestMatchers("/api/admin/users/**", "/api/admin/users").hasRole("ADMIN")
                                                // Regular user endpoints
                                                .requestMatchers("/api/user/booking/**").hasRole("USER")
                                                // .requestMatchers("/api/user/service/**").hasRole("USER")
                                                .requestMatchers("/api/user/service/**").permitAll()
                                                .requestMatchers("/api/services/**").permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        /*
         * @Bean
         * public CorsFilter corsFilter() {
         * // This ensures Spring handles OPTIONS requests properly BEFORE your
         * controllers
         * return new CorsFilter(corsConfigurationSource());
         * }
         */

        // allow your frontend to call the API during dev (adjust origins/ports!)
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(
                                List.of(
                                                "http://localhost:5500",
                                                "https://localhost:5500",
                                                "http://127.0.0.1:5500",
                                                "https://127.0.0.1:5500",
                                                "http://192.168.1.125:5500",
                                                "https://192.168.1.125:5500",
                                                "http://192.168.1.118:5500",
                                                "https://nazmos-sakib.github.io/fix-and-ride/",
                                                "https://nazmos-sakib.github.io/fix-and-ride",
                                                "https://nazmos-sakib.github.io",
                                                "https://*.github.io",
                                                "https://*.io",
                                                "https://thallous-pellucidly-delilah.ngrok-free.dev",
                                                "http://thallous-pellucidly-delilah.ngrok-free.dev",
                                                "https://*.ngrok.io",
                                                "https://*.ngrok.io",
                                                "http://*.ngrok-free.dev",
                                                "https://*.ngrok-free.dev"));
                cfg.setAllowedMethods(List.of("*"));
                cfg.setAllowedHeaders(List.of("*"));
                cfg.setExposedHeaders(List.of("Set-Cookie", "Authorization")); // optional
                cfg.setAllowCredentials(true);// Must allow cookies for refresh-token httpOnly cookie to work
                cfg.setMaxAge(3600L);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", cfg);

                return source;
        }
}
