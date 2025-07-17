package com.smartinsure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
public class SecurityConfig {

    // Configure BCryptPasswordEncoder as a Spring Bean
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure the Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure CSRF. For stateless APIs, we often disable it or manage it differently.
        // For now, let's disable it for simplicity in development, but be aware of its purpose.
        // In a production setup with cookies/sessions, you'd want proper CSRF protection.
        http
                .csrf(csrf -> csrf.disable()); // Temporarily disable CSRF for ease of testing in development.
        // For production, you'd likely use .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        // and ensure your frontend sends the token.

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register").permitAll() // Allow unauthenticated access to /api/auth/register
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Our APIs will be stateless (no sessions on server side)

        // Optional: For JWT-based auth later, you'd add filters here.
        // For now, we are just allowing /register publicly.

        return http.build();
    }
}