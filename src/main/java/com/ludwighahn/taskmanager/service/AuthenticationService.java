package com.ludwighahn.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ludwighahn.taskmanager.model.User;
import com.ludwighahn.taskmanager.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public Long getAuthenticatedUserId(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
    
        // Now fetch the user just once
        User user = userDetailsService.getUserByUsername(username);
        
        // Return the user ID
        return user != null ? user.getId() : null;
    }

    public <Optional>User getAuthenticatedUser(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
    
        // Now fetch the user just once
        User user = userDetailsService.getUserByUsername(username);
        
        // Return the user
        return user;
    }
    
}
