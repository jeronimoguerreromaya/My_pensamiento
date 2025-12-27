package com.mypensamiento.mypensamiento.application.usecase.Auth;

import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

import java.time.LocalDateTime;

public class LoginUseCase {

    UserPort userRepository;
    PasswordEncoderPort passwordEncoderRepository;
    RefreshTokenPort refreshTokenRepository;
    TokenPort tokenProvider;
    AuthenticationPort authenticationRepository;
    HashPort hashProvider;

    public LoginUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            RefreshTokenPort refreshTokenRepository,
            TokenPort tokenProvider,
            AuthenticationPort authenticationRepository,
            HashPort hashProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoderRepository = passwordEncoderRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationRepository = authenticationRepository;
        this.hashProvider = hashProvider;
    }

    public AuthResponse execute(LoginRequest request){

        if(request.email() == null || request.email().isEmpty() || request.password() == null || request.password().isEmpty()){
            throw new FieldValidationException("Email and Password are required" + " to login");
        }

        if(!userRepository.existsByEmail(request.email())){
            throw new NotFoundException("Email no found");
        }

        authenticationRepository.authenticate(
                request.email(),
                request.password()
        );

        LocalDateTime transactionTime = LocalDateTime.now();

        User userSave = userRepository.findByEmail(request.email());

        TokenResponse accessToken = tokenProvider.generateToken(userSave,transactionTime);
        TokenResponse refreshToken = tokenProvider.generateRefreshToken(userSave,transactionTime);

        String refreshTokenHash = hashProvider.hash(accessToken.token());

        RefreshToken refreshTokenSave = new RefreshToken();
            refreshTokenSave.setUser_id(userSave.getId());
            refreshTokenSave.setToken(refreshTokenHash);
            refreshTokenSave.setCreated_at(transactionTime);
            refreshTokenSave.setExpires_at(refreshToken.expirationDate());
            refreshTokenSave.setRevoked(false);

        refreshTokenRepository.save(refreshTokenSave);

        return new AuthResponse(accessToken.token(),refreshToken.token(),accessToken.expirationDate());
    }

}
