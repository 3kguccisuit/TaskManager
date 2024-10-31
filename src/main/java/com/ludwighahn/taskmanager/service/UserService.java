package com.ludwighahn.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ludwighahn.taskmanager.dto.UserDTO;
import com.ludwighahn.taskmanager.model.User;
import com.ludwighahn.taskmanager.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerNewUser(User user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        return userRepository.save(user);
    }

        public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(user -> new UserDTO(user.getUsername(), user.getEmail(), user.getRole()))
            .collect(Collectors.toList());
    }
}
