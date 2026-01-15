package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LoginUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {
    @Mock
    UserPort userPort;

    @Mock
    PasswordEncoderPort passwordEncoderPort;

    @Mock
    RefreshTokenPort refreshTokenPort;

    @Mock
    AuthenticationPort authenticationPort;

    @Mock
    HashPort hashPort;

    @Mock
    ServiceToken serviceToken;

    @InjectMocks
    LoginUseCase loginUseCase;

    @Test
    void login_whenEmailAndPasswordAreValid_shouldReturnTokens(){
        //Arrange
        String email="example@example.com";
        String password="Example123;";

        LoginRequest request = new LoginRequest(
                email,
                password
        );

        when(userPort.existsByEmail(anyString())).thenReturn(true);
        doNothing().when(authenticationPort).authenticate(anyString(), anyString());

        User saveUser = new User();
        saveUser.setId(1L);

        when(userPort.findByEmail(anyString())).thenReturn(saveUser);

        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        when(serviceToken.generateAuth(eq(saveUser), any(LocalDateTime.class))).thenReturn(authResponse);

        when(hashPort.hash(refreshToken)).thenReturn("hashed-refresh-token");

        //Act
        AuthResponse response = loginUseCase.execute(request);

        //Assert
        assertNotNull(response);
        assertEquals("access-token", response.access());
        assertEquals("refresh-token", response.refresh());

        verify(authenticationPort,times(1)).authenticate(anyString(), anyString());
        verify(userPort,times(1)).findByEmail(anyString());
        verify(hashPort,times(1)).hash(anyString());
        verify(refreshTokenPort,times(1)).save(any(com.mypensamiento.mypensamiento.domain.model.RefreshToken.class));

    }
    @Test
    void login_whenEmailNoExist_shouldReturnEmailNotExistsException(){
        //Arrage
        LoginRequest request = new LoginRequest(
                " example@example.com",
                "Example123;"

        );

        doThrow(new NotFoundException("Email no found"))
                .when(userPort).existsByEmail(anyString());

        //Act - Assert
        assertThrows(NotFoundException.class, () -> {
            loginUseCase.execute(request);
        });

        verify(authenticationPort,never()).authenticate(anyString(), anyString());
        verify(hashPort,never()).hash(anyString());
        verify(refreshTokenPort,never()).save(any(com.mypensamiento.mypensamiento.domain.model.RefreshToken.class));

    }
    @Test
    void login_whenCredentialsAreInvalid_thenAuthenticationFails(){
        //Arrage
        LoginRequest request = new LoginRequest(
                " example@example.com",
                "Example123;"

        );

        when(userPort.existsByEmail(anyString())).thenReturn(true);
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationPort).authenticate(anyString(), anyString());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            loginUseCase.execute(request);
        });

    }
}
