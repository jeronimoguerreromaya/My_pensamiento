package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

import java.time.LocalDateTime;

public class UpdatePasswordUseCase {

    UserPort userPort;
    PasswordEncoderPort passwordEncoderPort;
    TokenPort tokenPort;
    HashPort hashPort;
    RefreshTokenPort refreshTokenPort;

    public UpdatePasswordUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            TokenPort tokenPort,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort
    ) {
        this.userPort = userRepository;
        this.passwordEncoderPort = passwordEncoderRepository;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;
        this.refreshTokenPort = refreshTokenPort;

    }

    public AuthResponse execute(UpdatePasswordRequest request , Long id){

        User user = userPort.getById(id);

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
