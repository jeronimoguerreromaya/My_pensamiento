package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
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
    TokenPort tokenPort;
    HashPort hashPort;


    public RegisterUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort,
            HashPort hashPort

    ) {
        this.userPort = userPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.refreshTokenPort = refreshTokenPort;
        this.tokenPort = tokenPort;
        this.hashPort = hashPort;

    }
    public AuthResponse execute(RegisterUserRequest request) {

        if (request.nickname() == null || request.nickname().isEmpty() ||
                request.email() == null || request.email().isEmpty() ||
                request.password() == null || request.password().isEmpty()) {
            throw new FieldValidationException("Some Fields are required");
        }



        if(userPort.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if(userPort.existsByNickname(request.nickname())){
            throw new NickNameAlreadyExistsException("Nickname already exists");
        }

        LocalDateTime transactionTime = LocalDateTime.now();

        User user = new User()
                .setNickname(request.nickname())
                .setEmail(request.email())
                .setPassword(passwordEncoderPort.encode(request.password()))
                .setStatus(Boolean.TRUE)
                .setRole(Role.USER)
                .setCreated_at(transactionTime);

        if (request.full_name() != null && !request.full_name().isEmpty()) {
            user.setFull_name(request.full_name());
        }
        if (request.bio() != null && !request.bio().isEmpty()) {
            user.setBio(request.bio());
        }
        if (request.profile_picture() != null && !request.profile_picture().isEmpty() && !"null".equals(request.profile_picture())) {
            user.setProfile_picture(request.profile_picture());
        }

        User userSave = userPort.save(user);

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
