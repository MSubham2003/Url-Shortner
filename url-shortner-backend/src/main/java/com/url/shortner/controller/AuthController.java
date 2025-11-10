package com.url.shortner.controller;

import com.google.gson.Gson;
import com.url.shortner.dto.LoginRequest;
import com.url.shortner.dto.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    private Gson gson;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        if (logger.isInfoEnabled()) {
            logger.info("Request Hit in controller for Register User {}", gson.toJson(registerRequest));
        }
        userService.registerUser(registerRequest);
        return ResponseEntity.ok("User registered successfully!!!");
    }
}
