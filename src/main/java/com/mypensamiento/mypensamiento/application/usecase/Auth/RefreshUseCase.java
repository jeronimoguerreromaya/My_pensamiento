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

    RefreshTokenPort refreshTokenPort;
    UserPort userPort;
    TokenPort tokenPort;
    HashPort hashPort;
    public RefreshUseCase(
            RefreshTokenPort refreshTokenPort,
            UserPort userPort,
            TokenPort tokenPort,
            HashPort hashPort
    ){
        this.refreshTokenPort = refreshTokenPort;
        this.userPort = userPort;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;
    }

    public AuthResponse execute(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new FieldValidationException("Invalid Bearer token");
        }

        String currentRefreshToken = authHeader.substring(7);
        String userEmail = tokenPort.extractUsername(currentRefreshToken);

        if(userEmail == null || !userPort.existsByEmail(userEmail)){
            throw new UnauthorizedException("Invalid token request");
        }

        User user = userPort.findByEmail(userEmail);


        if(!tokenPort.validateToken(currentRefreshToken, user)){
            throw new UnauthorizedException("Invalid token request");
        }

        String currentRefreshTokenHash = hashPort.hash(currentRefreshToken);

        RefreshToken storedToken = refreshTokenPort.findByTokenHash(currentRefreshTokenHash);

        if (storedToken == null) {
            throw new UnauthorizedException("Invalid token request");
        }

        if (storedToken.isRevoked()) {
            refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
            throw new UnauthorizedException("Security alert: Token reuse detected");
        }

        if(!storedToken.isValid()){

            if (storedToken.getToken_hash() != null) {
                refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
                throw new UnauthorizedException("Invalid token request");
            }
            throw new UnauthorizedException("Invalid token request");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        if(transactionTime.isAfter(storedToken.getExpires_at())){
            refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
            throw new UnauthorizedException("Invalid token request");
        }

        TokenResponse accessToken = tokenPort.generateToken(user, transactionTime);
        TokenResponse refreshTokenSave = tokenPort.generateRefreshToken(user, transactionTime);

        String refreshTokenHashSave = hashPort.hash(refreshTokenSave.token());

        storedToken.setValid(false);
        storedToken.setReplaced_by_hash(refreshTokenHashSave);
        refreshTokenPort.save(storedToken);

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser_id(user.getId());
        newRefreshToken.setToken(refreshTokenHashSave);
        newRefreshToken.setCreated_at(transactionTime);
        newRefreshToken.setExpires_at(refreshTokenSave.expirationDate());
        newRefreshToken.setRevoked(false);
        newRefreshToken.setValid(true);
        newRefreshToken.setReplaced_by_hash(currentRefreshTokenHash);

        refreshTokenPort.save(newRefreshToken);

        return new AuthResponse(accessToken.token(), refreshTokenSave.token(), accessToken.expirationDate());
    }

}
