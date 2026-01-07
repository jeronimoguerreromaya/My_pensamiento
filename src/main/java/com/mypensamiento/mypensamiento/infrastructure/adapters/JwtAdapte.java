package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
import com.mypensamiento.mypensamiento.infrastructure.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class JwtAdapte  implements TokenPort {

    private final JwtService jwtService;
    public JwtAdapte(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse generateToken(User user, LocalDateTime transactionTime) {

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
        return jwtService.generateToken(userDetails,transactionTime);
    }

    @Override
    public TokenResponse generateRefreshToken(User user,LocalDateTime transactionTime) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
        return jwtService.generateRefreshToken(userDetails,transactionTime);
    }

    @Override
    public TokenResponse generatePasswordResetToken(User user, LocalDateTime transactionTime) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .build();
        return jwtService.generatePasswordResetToken(userDetails,transactionTime);
    }

    @Override
    public String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }

    @Override
    public boolean validateToken(String token, User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
        return jwtService.isTokenValid(token,userDetails);
    }

    @Override
    public boolean isResetTokenValid (String token){
        return jwtService.isResetTokenValid(token);
    }

}
