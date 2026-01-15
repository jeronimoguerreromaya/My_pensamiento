package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.PasswordChangeRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class PasswordChangeUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    TokenPort tokenPort;
    HashPort hashPort;
    RefreshTokenPort refreshTokenPort;
    ServiceToken serviceToken;

    public PasswordChangeUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenPort tokenPort,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort,
            ServiceToken serviceToken
    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;
        this.refreshTokenPort = refreshTokenPort;
        this.serviceToken = serviceToken;
    }

    public AuthResponse execute (PasswordChangeRequest request){

        if(request.password() == null || request.password().isEmpty()){
            throw new FieldValidationException("Password is required");
        }
        if(!tokenPort.isResetTokenValid(request.token())){
            throw new UnauthorizedException("invalid token");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        String email = tokenPort.extractUsername(request.token());

        User user = userPort.findByEmail(email);

        user.setPassword(passwordEncoderPort.encode(request.password()));
        User userSave = userPort.save(user);

        AuthResponse authResponse = serviceToken.generateAuth(userSave,transactionTime);

        RefreshToken refreshTokenDomain = new RefreshToken(
            user.getId(),
            hashPort.hash(authResponse.refresh()),
            authResponse.refreshExpiresIn()
        );
        refreshTokenPort.save(refreshTokenDomain);

        return  authResponse;

    }
}
