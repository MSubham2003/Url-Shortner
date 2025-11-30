package com.url.shortner.service;

import com.url.shortner.controller.AuthController;
import com.url.shortner.dto.LoginRequest;
import com.url.shortner.dto.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.repository.UserRepository;
import com.url.shortner.security.jwt.JwtAuthenticationResponse;
import com.url.shortner.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void registerUser(RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setRole("ROLE_USER");
            user.setEmail(registerRequest.getEmail());
            logger.info("Setting password in Hash");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            logger.info("User saved with details: {}", user);
        } catch (Exception e) {
            logger.info("An error occured while User Registration {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public JwtAuthenticationResponse loginUser(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        logger.info("Generated JWT is {}", jwt);
        return new JwtAuthenticationResponse(jwt);
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElse(null);
    }
}
