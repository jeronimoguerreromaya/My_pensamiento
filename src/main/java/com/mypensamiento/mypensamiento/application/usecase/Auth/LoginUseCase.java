package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class LoginUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    RefreshTokenPort refreshTokenPort;
    AuthenticationPort authenticationPort;
    HashPort hashPort;
    ServiceToken serviceToken;

    public LoginUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            AuthenticationPort authenticationPort,
            HashPort hashPort,
            ServiceToken serviceToken
    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.refreshTokenPort = refreshTokenPort;
        this.authenticationPort = authenticationPort;
        this.hashPort = hashPort;
        this.serviceToken = serviceToken;
    }

    public AuthResponse execute(LoginRequest request){

        if(request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new FieldValidationException("Email and Password are required" + " to login");
        }

        if(!userPort.existsByEmail(request.email())){
            throw new NotFoundException("Email no found");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        authenticationPort.authenticate(
                request.email(),
                request.password()
        );

        User userSave = userPort.findByEmail(request.email());

        AuthResponse authResponse = serviceToken.generateAuth(userSave,transactionTime);

        RefreshToken refreshTokenDomain = new RefreshToken(
                userSave.getId(),
                hashPort.hash(authResponse.refresh()),
                authResponse.refreshExpiresIn()
        );

        refreshTokenPort.save(refreshTokenDomain);

        return authResponse;
    }

}
