package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class UpdatePasswordUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    HashPort hashPort;
    RefreshTokenPort refreshTokenPort;
    ServiceToken serviceToken;

    public UpdatePasswordUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort,
            ServiceToken serviceToken
    ) {
        this.userPort = userRepository;
        this.passwordEncoderPort = passwordEncoderRepository;
        this.hashPort = hashPort;
        this.refreshTokenPort = refreshTokenPort;
        this.serviceToken = serviceToken;
    }

    public AuthResponse execute(UpdatePasswordRequest request , Long id){

        User user = userPort.getById(id);

        LocalDateTime transactionTime = LocalDateTime.now();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (request.password() == null || request.password().isEmpty()
                || request.newPassword() == null || request.newPassword().isEmpty()
                || request.confirmPassword() == null || request.confirmPassword().isEmpty()) {
            throw new FieldValidationException("All password fields are required");
        }

        if (!passwordEncoderPort.matches(request.password(), user.getPassword())) {
            throw new FieldValidationException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new FieldValidationException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoderPort.encode(request.newPassword()));
        User userSave = userPort.save(user);

        refreshTokenPort.revokrevokeAllByUserIdeAll(id);


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
