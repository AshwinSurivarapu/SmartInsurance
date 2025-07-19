package com.smartinsure.config.jwt;

import com.smartinsure.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Mark this as a Spring component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Your JwtUtil for token operations

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Your UserDetailsService

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    public JwtAuthFilter() {
        logger.info("JwtAuthFilter instance created."); // Add this line
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); // Extract JWT from Authorization header
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) { // Validate the token
                String username = jwtUtil.getUserNameFromJwtToken(jwt); // Get username from token

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load user details

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // Credentials are not needed after token validation
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response); // Continue the filter chain
        if (jwtUtil == null) {
            logger.error("jwtUtil is NULL inside JwtAuthFilter!");
            filterChain.doFilter(request, response);
            return;
        }
        if (userDetailsService == null) {
            logger.error("userDetailsService is NULL inside JwtAuthFilter!");
            filterChain.doFilter(request, response);
            return;
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Remove "Bearer " prefix
        }

        return null;
    }
}