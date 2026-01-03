package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;

public class LogoutUseCase {

    private final RefreshTokenPort refreshTokenPort;
    private final TokenPort tokenPort;

    public LogoutUseCase(RefreshTokenPort refreshTokenPort, TokenPort tokenPort) {
        this.refreshTokenPort = refreshTokenPort;
        this.tokenPort = tokenPort;
    }

    public void execute(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new FieldValidationException("Invalid Bearer token");
        }

        String jwt = authHeader.substring(7);

        String userEmail = tokenPort.extractUsername(jwt);

        if (userEmail != null) {
         refreshTokenPort.revokeByEmail(userEmail);
         }
    }
}