package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;

public class LogoutUseCase {

    private final RefreshTokenPort refreshTokenRepository;
    private final TokenPort tokenProvider;



    public LogoutUseCase(RefreshTokenPort refreshTokenRepository, TokenPort tokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;

    }

    public void execute(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);

        String userEmail = tokenProvider.extractUsername(jwt);

        if (userEmail != null) {
         refreshTokenRepository.revokeByEmail(userEmail);
         }
    }
}