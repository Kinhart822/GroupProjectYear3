package com.spring.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String jwt, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String generateResetToken(UserDetails userDetails);
    long getExpirationTime();
}
