package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class RefreshUseCase {

    RefreshTokenPort refreshTokenPort;
    UserPort userPort;
    TokenPort tokenPort;
    HashPort hashPort;
    ServiceToken serviceToken;
    public RefreshUseCase(
            RefreshTokenPort refreshTokenPort,
            UserPort userPort,
            TokenPort tokenPort,
            HashPort hashPort,
            ServiceToken serviceToken
    ){
        this.refreshTokenPort = refreshTokenPort;
        this.userPort = userPort;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;
        this.serviceToken = serviceToken;
    }

    public AuthResponse execute(String authHeader){

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new FieldValidationException("Invalid Bearer token");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        String oldRefreshToken = authHeader.substring(7);
        String userEmail = tokenPort.extractUsername(oldRefreshToken);

        if(userEmail == null || !userPort.existsByEmail(userEmail)){
            throw new UnauthorizedException("Invalid token request");
        }

        User user = userPort.findByEmail(userEmail);

        if(!tokenPort.validateToken(oldRefreshToken, user)){
            throw new UnauthorizedException("Invalid token request");
        }

        String currentRefreshTokenHash = hashPort.hash(oldRefreshToken);

        RefreshToken refreshTokenSaved = refreshTokenPort.findByTokenHash(currentRefreshTokenHash);

        if (refreshTokenSaved == null) {
            throw new UnauthorizedException("Invalid token request");
        }

        if (refreshTokenSaved.isRevoked()) {
            refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
            throw new UnauthorizedException("Security alert: Token reuse detected");
        }

        if(!refreshTokenSaved.isValid()){

            if (refreshTokenSaved.getToken_hash() != null) {
                refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
                throw new UnauthorizedException("Invalid token request");
            }
            throw new UnauthorizedException("Invalid token request");
        }

        if(transactionTime.isAfter(refreshTokenSaved.getExpires_at())){
            refreshTokenPort.revokrevokeAllByUserIdeAll(user.getId());
            throw new UnauthorizedException("Invalid token request");
        }

        AuthResponse authResponse = serviceToken.generateAuth(user,transactionTime);

        String newRefreshTokenHash = hashPort.hash(authResponse.refresh());

        refreshTokenSaved.setValid(false);
        refreshTokenSaved.setReplaced_by_hash(newRefreshTokenHash);
        refreshTokenPort.save(refreshTokenSaved);

        RefreshToken refreshTokenDomain = new RefreshToken(
                user.getId(), 
                newRefreshTokenHash,
                authResponse.refreshExpiresIn()
        );

        refreshTokenPort.save(refreshTokenDomain);

        return authResponse;
    }

}
