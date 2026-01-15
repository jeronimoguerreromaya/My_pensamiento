package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.model.categorie.Role;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class RegisterUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    RefreshTokenPort refreshTokenPort;
    HashPort hashPort;
    ServiceToken serviceToken;

    public RegisterUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            HashPort hashPort,
            ServiceToken serviceToken

    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.refreshTokenPort = refreshTokenPort;
        this.hashPort = hashPort;
        this.serviceToken = serviceToken;

    }
    public AuthResponse execute(RegisterUserRequest request) {

        if (request.nickname() == null || request.nickname().isEmpty() ||
                request.email() == null || request.email().isEmpty() ||
                request.password() == null || request.password().isEmpty()) {
            throw new FieldValidationException("Some Fields are required");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        if(userPort.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if(userPort.existsByNickname(request.nickname())){
            throw new NickNameAlreadyExistsException("Nickname already exists");
        }

        User user = new User(
                request.nickname(),
                request.email(),
                passwordEncoderPort.encode(request.password()),
                request.full_name(),
                request.bio(),
                request.profile_picture()
        );

        User userSave = userPort.save(user);

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
