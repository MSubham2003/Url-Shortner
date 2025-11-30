package com.url.shortner.controller;

import com.google.gson.Gson;
import com.url.shortner.dto.LoginRequest;
import com.url.shortner.dto.RegisterRequest;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private UserService userService;
    private Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Request Hit in controller for Login User {}", gson.toJson(loginRequest));
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Request Hit in controller for Register User {}", gson.toJson(registerRequest));
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("User registered successfully!!!");
    }
}
