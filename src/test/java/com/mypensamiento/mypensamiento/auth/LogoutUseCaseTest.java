package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LogoutUseCase;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LogoutUseCaseTest {

    @Mock
    RefreshTokenPort refreshTokenPort;

    @Mock
    TokenPort tokenPort;

    @InjectMocks
    LogoutUseCase logoutUseCase;

    @Test
    void execute_whenHeaderIsValid_shouldInvalidateRefreshToken() {
        // Arrange
        String jwt = "token12345";
        String authHeader = "Bearer " + jwt;
        String userEmail = "test@gmail.com";

        when(tokenPort.extractUsername(jwt)).thenReturn(userEmail);

        //Act-Assert
        logoutUseCase.execute(authHeader);

        verify(tokenPort,times(1)).extractUsername(jwt);
        verify(refreshTokenPort,times(1)).revokeByEmail(userEmail);

    }

    @Test
    void execute_whenHeaderIsInvalid_shouldThrowFieldValidationException() {
        // Arrange
        String authHeader = "Basic dXNlcjpwYXNz";

        // Act & Assert
        FieldValidationException exception = assertThrows(FieldValidationException.class, () -> {
            logoutUseCase.execute(authHeader);
        });

        assertEquals("Invalid Bearer token", exception.getMessage());

        verifyNoInteractions(tokenPort);
        verifyNoInteractions(refreshTokenPort);
    }

}

