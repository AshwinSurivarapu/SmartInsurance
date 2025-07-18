package com.smartinsure.service;

import com.smartinsure.domain.User;
import com.smartinsure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
// find by username
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not found with username: "+username));

        // Build Spring Security's UserDetails object from your User entity
        // (You might have roles/authorities on your User entity that you'd map here)
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                .password(user.getPasswordHash()).authorities("ROLE_USER").build();
    }
    // --- NEW METHOD ADDED HERE ---
    // Public method to retrieve the actual User entity from the repository
    public User loadUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }
}
