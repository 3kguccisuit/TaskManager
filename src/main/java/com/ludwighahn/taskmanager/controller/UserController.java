package com.ludwighahn.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ludwighahn.taskmanager.dto.UserDTO;
import com.ludwighahn.taskmanager.model.User;
import com.ludwighahn.taskmanager.service.AuthenticationService;
import com.ludwighahn.taskmanager.service.UserService;
import com.ludwighahn.taskmanager.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        userService.registerNewUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        Optional<User> foundUser = userService.findByUsername(user.getUsername());

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(foundUser.get().getUsername());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')") // Only admins can access this method
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/username")
    public ResponseEntity<String> updateUsername(@RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        String newUsername = request.get("username");

        // Get the authenticated user
        User user = authenticationService.getAuthenticatedUser(httpRequest);

        // Check if the new username is already taken
        if (userService.findByUsername(newUsername).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        userService.updateUsername(user, newUsername);
        return ResponseEntity.ok("Username updated successfully to (" + newUsername + ") , please log in again");
    }

    @PutMapping("/update/email")
    public ResponseEntity<String> updateEmail(@RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        String newEmail = request.get("email");

        // Get the authenticated user
        User user = authenticationService.getAuthenticatedUser(httpRequest);

        userService.updateEmail(user, newEmail);
        return ResponseEntity.ok("Email updated successfully to (" + newEmail + ")");
    }

    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        String newPassword = request.get("password");

        // Get the authenticated user
        User user = authenticationService.getAuthenticatedUser(httpRequest);

        userService.updatePassword(user, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/role")
    public ResponseEntity<String> updateRole(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        User user = authenticationService.getAuthenticatedUser(httpRequest);
        if (!user.getRole().equals("ADMIN")) {
            return new ResponseEntity<>("Only admins can update roles", HttpStatus.UNAUTHORIZED);
        }
        String newRole = request.get("role");
        String usernameUpdateRole = request.get("username");
        userService.updateRole(userService.findByUsername(usernameUpdateRole).get(), newRole);
        return ResponseEntity.ok("Role updated successfully to (" + newRole + ")");
    }

}
