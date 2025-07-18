package com.smartinsure;

import com.smartinsure.config.jwt.JwtAuthEntryPoint;
import com.smartinsure.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Replaced @EnableGlobalMethodSecurity with @EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthEntryPoint unauthorizedHandler; // Autowire your new entry point
        // (Future: @Autowired JwtAuthEntryPoint unauthorizedHandler; // For handling unauthorized access)
        // (Future: @Bean public JwtAuthFilter authenticationJwtTokenFilter() { ... } // Your JWT filter)

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
        }

        // Explicit CORS Configuration Bean (Crucial for allowing React to talk to Java)
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            // Allow your React app's origin during development
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
            // Allow common methods for REST APIs
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            // Allow all headers, including Authorization
            configuration.setAllowedHeaders(Arrays.asList("*"));
            // Allow sending credentials (like cookies or Authorization headers)
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            // Apply this CORS configuration to all paths
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())

                    // CONFIGURE EXCEPTION HANDLING HERE!
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/test/**").permitAll()
                            .anyRequest().authenticated()
                    );

            http.authenticationProvider(authenticationProvider());
            // (Future: http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);)

            return http.build();
        }
    }
