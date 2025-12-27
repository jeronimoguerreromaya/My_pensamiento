package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

import java.time.LocalDateTime;

public class RefreshUseCase {

    RefreshTokenPort refreshTokenRepository;
    UserPort userRepository;
    TokenPort tokenProvider;
    HashPort hashProvider;
    public RefreshUseCase(
            RefreshTokenPort refreshTokenRepository,
            UserPort userRepository,
            TokenPort tokenProvider,
            HashPort hashProvider
    ){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.hashProvider = hashProvider;
    }

    public AuthResponse execute(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new FieldValidationException("Invalid Bearer token");
        }

        String refreshToken = authHeader.substring(7);
        String userEmail = tokenProvider.extractUsername(refreshToken);

        if(userEmail == null || !userRepository.existsByEmail(userEmail)){
            throw new UnauthorizedException("Invalid token request");
        }

        User user = userRepository.findByEmail(userEmail);

        if(!tokenProvider.validateToken(refreshToken, user)){
            throw new UnauthorizedException("Invalid token request");
        }

        String refreshTokenHash = hashProvider.hash(refreshToken);

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(refreshTokenHash);
        if (storedToken == null) {
            throw new UnauthorizedException("Invalid token request");
        }

        if (storedToken.isRevoked()) {
            refreshTokenRepository.revokeByUserId(user.getId());
            throw new UnauthorizedException("Security alert: Token reuse detected");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        LocalDateTime transactionTime = LocalDateTime.now();

        TokenResponse accessToken = tokenProvider.generateToken(user, transactionTime);
        TokenResponse refreshTokenSave = tokenProvider.generateRefreshToken(user, transactionTime);

        String refreshTokenHashSave = hashProvider.hash(refreshTokenSave.token());

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser_id(user.getId());
        newRefreshToken.setToken(refreshTokenHashSave);
        newRefreshToken.setCreated_at(transactionTime);
        newRefreshToken.setExpires_at(refreshTokenSave.expirationDate());
        newRefreshToken.setRevoked(false);

        refreshTokenRepository.save(newRefreshToken);

        return new AuthResponse(accessToken.token(), refreshTokenSave.token(), accessToken.expirationDate());
    }

}
