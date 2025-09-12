package com.pm.backend.controller;

import com.pm.backend.dto.LoginUserDTO;
import com.pm.backend.dto.RegisterUserDTO;
import com.pm.backend.dto.VerifiyUserDTO;
import com.pm.backend.model.User;
import com.pm.backend.responses.LoginResponse;
import com.pm.backend.service.AuthenticationService;
import com.pm.backend.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        try {
            logger.info("Registration attempt for email: {}", registerUserDTO.getEmail());
            User registeredUser = authenticationService.signup(registerUserDTO);
            logger.info("User registered successfully with ID: {}", registeredUser.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful. Please check your email for verification code.");
            response.put("email", registeredUser.getEmail());
            response.put("userId", registeredUser.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", registerUserDTO.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDTO loginUserDTO) {
        try {
            logger.info("Login attempt for email: {}", loginUserDTO.getEmail());
            User authenticatedUser = authenticationService.authenticate(loginUserDTO);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
            logger.info("Login successful for user ID: {}", authenticatedUser.getId());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            logger.error("Login failed for email: {}", loginUserDTO.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifiyUserDTO verifiyUserDTO) {
        try {
            logger.info("Verification attempt for email: {}", verifiyUserDTO.getEmail());
            authenticationService.verifyUser(verifiyUserDTO);
            logger.info("Verification successful for email: {}", verifiyUserDTO.getEmail());
            return ResponseEntity.ok(Map.of("message", "Account verified successfully"));
        } catch (Exception e) {
            logger.error("Verification failed for email: {}", verifiyUserDTO.getEmail(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            logger.info("Resend verification code attempt for email: {}", email);
            authenticationService.resendVerificationCode(email);
            logger.info("Verification code resent successfully for email: {}", email);
            return ResponseEntity.ok(Map.of("message", "Verification code resent successfully"));
        } catch (Exception e) {
            logger.error("Resend verification failed for email: {}", email, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
