package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class LoginUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    RefreshTokenPort refreshTokenPort;
    TokenPort tokenPort;
    AuthenticationPort authenticationPort;
    HashPort hashPort;

    public LoginUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort,
            AuthenticationPort authenticationPort,
            HashPort hashPort
    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.refreshTokenPort = refreshTokenPort;
        this.tokenPort = tokenPort;
        this.authenticationPort = authenticationPort;
        this.hashPort = hashPort;
    }

    public AuthResponse execute(LoginRequest request){

        if(request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new FieldValidationException("Email and Password are required" + " to login");
        }

        if(!userPort.existsByEmail(request.email())){
            throw new NotFoundException("Email no found");
        }

        authenticationPort.authenticate(
                request.email(),
                request.password()
        );

        LocalDateTime transactionTime = LocalDateTime.now();

        User userSave = userPort.findByEmail(request.email());

        TokenResponse accessToken = tokenPort.generateToken(userSave,transactionTime);
        TokenResponse refreshToken = tokenPort.generateRefreshToken(userSave,transactionTime);

        String refreshTokenHash = hashPort.hash(refreshToken.token());

        RefreshToken refreshTokenSave = new RefreshToken();
            refreshTokenSave.setUser_id(userSave.getId());
            refreshTokenSave.setToken(refreshTokenHash);
            refreshTokenSave.setCreated_at(transactionTime);
            refreshTokenSave.setExpires_at(refreshToken.expirationDate());
            refreshTokenSave.setRevoked(false);
            refreshTokenSave.setValid(true);

        refreshTokenPort.save(refreshTokenSave);

        return new AuthResponse(accessToken.token(),refreshToken.token(),accessToken.expirationDate());
    }

}
