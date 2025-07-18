package com.smartinsure.controller;

import com.smartinsure.config.jwt.JwtUtil;
import com.smartinsure.service.UserDetailsServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
class LoginRequest{
    public String username;
    public String password;
}
class JwtResponse{
    public String token;
    public String type = "Bearer";
    public String id;
    public String username;
    public String email;
    public List<String>roles;

    public JwtResponse(String accessToken, String id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}

@CrossOrigin(origins = "*", maxAge = 3600) // Allow CORS for development
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService; // To get email from username after login


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Set the authenticated user in the SecurityContext (important for later checks)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get UserDetails (this is org.springframework.security.core.userdetails.User)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Fetch actual User object from DB to get ID and Email (if needed for response)
        // Assuming your User entity has an 'id' and 'email' field
        // --- CORRECTED LINE HERE ---
        // Use the new public method from UserDetailsServiceImpl to get the User entity
        com.smartinsure.domain.User userEntity =
                userDetailsService.loadUserEntityByUsername(userDetails.getUsername());

        // Extract roles/authorities
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Return JWT response
        return ResponseEntity.ok(new JwtResponse(jwt,
                userEntity.getId(), // Assuming your User model has getId()
                userEntity.getUsername(),
                userEntity.getEmail(), // Assuming your User model has getEmail()
                roles));
    }

    // Keep your /register endpoint here as well if it was in a separate controller,
    // or ensure it's still accessible.
}