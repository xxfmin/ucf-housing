package com.pm.backend.service;

import com.pm.backend.dto.LoginUserDTO;
import com.pm.backend.dto.RegisterUserDTO;
import com.pm.backend.dto.VerifiyUserDTO;
import com.pm.backend.model.User;
import com.pm.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiration(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByEmail(loginUserDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(loginUserDTO.getEmail()));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDTO.getEmail(),
                        loginUserDTO.getPassword()
                )
        );

        return user;
    }

    public void verifyUser(VerifiyUserDTO verifiyUserDTO) {
        logger.info("Looking for user with email: {}", verifiyUserDTO.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(verifiyUserDTO.getEmail());
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            logger.info("User found with ID: {}, enabled: {}", user.getId(), user.isEnabled());
            logger.info("Verification code from user: {}, from request: {}", 
                       user.getVerificationCode(), verifiyUserDTO.getVerificationCode());
            logger.info("Expiration: {}, current time: {}", 
                       user.getVerificationExpiration(), LocalDateTime.now());
            
            if (user.getVerificationExpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Account verification expired. Please try again.");
            }

            if (user.getVerificationCode().equals(verifiyUserDTO.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationExpiration(null);
                User savedUser = userRepository.save(user);
                logger.info("User verified and saved with ID: {}, enabled: {}", savedUser.getId(), savedUser.isEnabled());
            } else {
                throw new RuntimeException("Invalid verification code. Expected: " + user.getVerificationCode() + 
                                         ", Got: " + verifiyUserDTO.getVerificationCode());
            }
        } else {
            logger.error("User not found for email: {}", verifiyUserDTO.getEmail());
            // Let's also check if user exists by username
            logger.info("Checking all users in database...");
            userRepository.findAll().forEach(u -> 
                logger.info("User in DB - ID: {}, Email: {}, Username: {}, Enabled: {}", 
                           u.getId(), u.getEmail(), u.getUsername(), u.isEnabled())
            );
            throw new RuntimeException("User not found. Please try again.");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified.");
            }

            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiration(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found. Please try again.");
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our Knight Housing!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
