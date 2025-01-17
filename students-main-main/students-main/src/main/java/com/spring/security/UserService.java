package com.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
@Service
public class UserService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    /**
     * Registers a new user by encoding their password and saving them to the repository.
     * Ensures roles are set if not provided.
     */
    public Userinfo registerUser(Userinfo user) {
        // Encrypt password before saving to the DB
        user.setPassword(encoder.encode(user.getPassword()));
        // Default role as USER if not provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Arrays.asList("ROLE_USER"));
        }
        // Save and return the newly created user
        return userRepo.save(user);
    }

    public String verify(Userinfo user) {
        // Authenticate user credentials using AuthenticationManager
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if (auth.isAuthenticated()) {
            // Retrieve user from DB using the username
            Userinfo foundUser = userRepo.findByUsername(user.getUsername());
            if (foundUser != null) {
                // Generate JWT token with roles
                return jwtService.generateToken(foundUser.getUsername(), foundUser.getRoles());
            }
        }
        return "Authentication Failed";
    }
}
