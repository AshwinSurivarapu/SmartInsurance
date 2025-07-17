package com.smartinsure.controller;

import com.smartinsure.domain.User;
import com.smartinsure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/auth") // Base path for all endpoints in this controller
@CrossOrigin(origins="http://localhost:3000")
public class UserController {

    @Autowired // Injects the UserRepository
    private UserRepository userRepository;

    // We need an instance of BCryptPasswordEncoder to hash passwords
    // For now, we can create it directly. Later, we'll configure it as a Spring Bean.
   @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // DTO for user registration request (optional but good practice)
    static class RegisterRequest {
        public String username;
        public String password;
        public String email;
    }

    @PostMapping("/register") // Handles POST requests to /api/auth/register
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.username).isPresent()) {
            return new ResponseEntity<>(
                    Map.of("message", "Username already exists!"),
                    HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        // Check if email already exists
        if (userRepository.findByUsername(request.email).isPresent()) { // Reusing for email check, adjust if you need a specific findByEmail
            return new ResponseEntity<>(
                    Map.of("message", "Email already registered!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Hash the password before saving
        String hashedPassword = bCryptPasswordEncoder.encode(request.password);

        // Create a new User entity
        User newUser = new User(request.username, hashedPassword, request.email);

        // Save the user to the database
        userRepository.save(newUser);

        return new ResponseEntity<>(
                Map.of("message", "User registered successfully!"),
                HttpStatus.CREATED); // 201 Created
    }
}