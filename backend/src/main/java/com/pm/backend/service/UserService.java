package com.pm.backend.service;

import com.pm.backend.model.User;
import com.pm.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = userRepository.findAll();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}
