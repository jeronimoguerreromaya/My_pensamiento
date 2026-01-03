package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.LoginRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LoginUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
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
    TokenPort tokenPort;

    @Mock
    AuthenticationPort authenticationPort;

    @Mock
    HashPort hashPort;

    @InjectMocks
    LoginUseCase loginUseCase;

    @Test
    void login_whenEmailAndPasswordAreValid_shouldReturnTokens(){
        //Arrage
        LoginRequest request = new LoginRequest(
          " example@example.com",
                "Example123;"

        );

        when(userPort.existsByEmail(anyString())).thenReturn(true);
        doNothing().when(authenticationPort).authenticate(anyString(), anyString());

        User saveUser = new User();
        saveUser.setId(1L);

        when(userPort.findByEmail(anyString())).thenReturn(saveUser);

        TokenResponse mockToken = new TokenResponse("access-token", LocalDateTime.now().plusHours(1));
        TokenResponse mockRefreshToken = new TokenResponse("refresh-token", LocalDateTime.now().plusDays(1));

        when(tokenPort.generateToken(any(User.class), any(LocalDateTime.class))).thenReturn(mockToken);
        when(tokenPort.generateRefreshToken(any(User.class), any(LocalDateTime.class))).thenReturn(mockRefreshToken);

        when(hashPort.hash("refresh-token")).thenReturn("hashed-refresh-token");

        //Act
        AuthResponse response = loginUseCase.execute(request);

        //Assert
        assertNotNull(response);
        assertEquals("access-token", response.accestoken());
        assertEquals("refresh-token", response.refreshToken());

        verify(authenticationPort,times(1)).authenticate(anyString(), anyString());
        verify(userPort,times(1)).findByEmail(anyString());
        verify(tokenPort,times(1)).generateToken(any(User.class), any(LocalDateTime.class));
        verify(tokenPort,times(1)).generateRefreshToken(any(User.class), any(LocalDateTime.class));
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
        verify(tokenPort,never()).generateToken(any(User.class), any(LocalDateTime.class));
        verify(tokenPort,never()).generateRefreshToken(any(User.class), any(LocalDateTime.class));
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

        verify(tokenPort,never()).generateToken(any(User.class), any(LocalDateTime.class));
        verify(tokenPort,never()).generateRefreshToken(any(User.class), any(LocalDateTime.class));

    }
}
