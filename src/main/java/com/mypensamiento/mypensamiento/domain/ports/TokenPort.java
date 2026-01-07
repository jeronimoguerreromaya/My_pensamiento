package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

import java.time.LocalDateTime;

public interface TokenPort {

    TokenResponse generateToken(User user, LocalDateTime transactionTime);
    TokenResponse generateRefreshToken(User user,LocalDateTime transactionTime);
    TokenResponse generatePasswordResetToken(User user,LocalDateTime transactionTime);
    String extractUsername(String token);
    boolean validateToken(String token, User user);
    boolean isResetTokenValid (String token);
}
