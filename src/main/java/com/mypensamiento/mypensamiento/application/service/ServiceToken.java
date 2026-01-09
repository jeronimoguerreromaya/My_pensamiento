package com.mypensamiento.mypensamiento.application.service;

import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;

import java.time.LocalDateTime;

public class ServiceToken {

    private final TokenPort tokenPort;

    public ServiceToken(TokenPort tokenPort) {
        this.tokenPort = tokenPort;
    }

    public AuthResponse generateAuth(User user, LocalDateTime transactionTime) {
        TokenResponse newAccessToken = tokenPort.generateToken(user,transactionTime);
        TokenResponse newRefreshToken = tokenPort.generateRefreshToken(user,transactionTime);

        return new AuthResponse(
                newAccessToken.token(),
                newRefreshToken.token(),
                newAccessToken.expirationDate(),
                newRefreshToken.expirationDate()
        );
    }

}
