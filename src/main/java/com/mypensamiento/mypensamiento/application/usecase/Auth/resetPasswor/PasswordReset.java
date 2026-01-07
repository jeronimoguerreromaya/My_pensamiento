package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPasswor;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

import java.time.LocalDateTime;

public class PasswordReset {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    TokenPort tokenPort;
    HashPort hashPort;
    RefreshTokenPort refreshTokenPort;

    public PasswordReset(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenPort tokenPort,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort
    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;
        this.refreshTokenPort = refreshTokenPort;
    }

    public AuthResponse execute (String newPass, String token){

        if(newPass == null || newPass.isEmpty()){
            throw new FieldValidationException("Password is required");
        }
        if(!tokenPort.isResetTokenValid(token)){
            throw new UnauthorizedException("invalid token");
        }
        String email = tokenPort.extractUsername(token);

        User user = userPort.findByEmail(email);

        user.setPassword(passwordEncoderPort.encode(newPass));
        User userSave = userPort.save(user);

        LocalDateTime transactionTime = LocalDateTime.now();

        TokenResponse accessToken = tokenPort.generateToken(userSave,transactionTime);
        TokenResponse refreshToken = tokenPort.generateRefreshToken(userSave,transactionTime);

        RefreshToken refreshTokenDomain = new RefreshToken();
        refreshTokenDomain.setUser_id(userSave.getId());
        refreshTokenDomain.setToken(hashPort.hash(refreshToken.token()));
        refreshTokenDomain.setCreated_at(transactionTime);
        refreshTokenDomain.setExpires_at(refreshToken.expirationDate());
        refreshTokenDomain.setRevoked(false);
        refreshTokenDomain.setValid(true);

        refreshTokenPort.save(refreshTokenDomain);

        return  new AuthResponse(accessToken.token(),refreshToken.token(),accessToken.expirationDate());

    }
}
