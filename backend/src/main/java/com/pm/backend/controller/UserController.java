package com.pm.backend.controller;

import com.pm.backend.dto.UserResponseDTO;
import com.pm.backend.model.User;
import com.pm.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(currentUser.getId());
        userResponseDTO.setUsername(currentUser.getUsername());
        userResponseDTO.setEmail(currentUser.getEmail());
        userResponseDTO.setEnabled(currentUser.isEnabled());
        userResponseDTO.setCreatedAt(currentUser.getCreatedAt());
        userResponseDTO.setUpdatedAt(currentUser.getUpdatedAt());

        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }
}
