package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;

public class LogoutAllUseCase {

    private final RefreshTokenPort refreshTokenPort;
    private final TokenPort tokenPort;

    public LogoutAllUseCase(RefreshTokenPort refreshTokenPort, TokenPort tokenPort) {
        this.refreshTokenPort = refreshTokenPort;
        this.tokenPort = tokenPort;
    }

    public void execute(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);

        String userEmail = tokenPort.extractUsername(jwt);

        if (userEmail != null) {
         refreshTokenPort.revokrevokeAllByUserEmailAll(userEmail);
         }
    }
}