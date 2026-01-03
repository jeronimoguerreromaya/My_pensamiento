package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RegisterUseCase;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class RegisterUseCaseTest {

    @Mock
    UserPort userPort;

    @Mock
    PasswordEncoderPort passwordEncoderPort;

    @Mock
    RefreshTokenPort refreshTokenPort;

    @Mock
    TokenPort tokenPort;

    @Mock
    HashPort hashPort;

    @InjectMocks
    RegisterUseCase registerUseCase;

    @Test
    void register_whenUserIsValid_shouldCreateUserAndReturnTokens(){
        //Arrage
        RegisterUserRequest request = new RegisterUserRequest(
                "nickName",
                "email",
                "password",
                "full_name",
                "bio",
                "profile_picture"
        );

        when(passwordEncoderPort.encode(request.password())).thenReturn("PasswordEncoded");

        User savedUser = new User();
        savedUser.setId(1L);
        when(userPort.save(any(User.class))).thenReturn(savedUser);

        TokenResponse mockToken = new TokenResponse("access-token", LocalDateTime.now().plusHours(1));
        TokenResponse mockRefreshToken = new TokenResponse("refresh-token", LocalDateTime.now().plusDays(1));

        when(tokenPort.generateToken(any(User.class), any(LocalDateTime.class))).thenReturn(mockToken);
        when(tokenPort.generateRefreshToken(any(User.class), any(LocalDateTime.class))).thenReturn(mockRefreshToken);

        when(hashPort.hash("refresh-token")).thenReturn("hashed-refresh-token");

        //Act
        AuthResponse response = registerUseCase.execute(request);

        //Assert
        assertNotNull(response);
        assertEquals("access-token", response.accestoken());
        assertEquals("refresh-token", response.refreshToken());

        verify(userPort, times(1)).save(any(User.class));
        verify(refreshTokenPort, times(1)).save(any(RefreshToken.class));
        verify(passwordEncoderPort).encode(request.password());

    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowEmailAlreadyExistException (){
        //Arrage
        RegisterUserRequest request = new RegisterUserRequest(
                "nickName",
                "example@example.com",
                "password",
                "full_name",
                "bio",
                "profile_picture"
        );



        //Act-Assert
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> registerUseCase.execute(request));

        assertEquals("Email example@example.com already exists", exception.getMessage());

        verify(userPort, never()).save(any(User.class));
        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(hashPort, never()).hash(anyString());
        verify(tokenPort, never()).generateToken(any(User.class), any(LocalDateTime.class));

    }

    @Test
    void register_whenNickNameAlreadyExists_shouldThrowNickNameAlreadyExistsException (){
        //Arrage
        RegisterUserRequest request = new RegisterUserRequest(
                "nickName",
                "example@example.com",
                "password",
                "full_name",
                "bio",
                "profile_picture"
        );


        //Act-Assert
        NickNameAlreadyExistsException exception = assertThrows(NickNameAlreadyExistsException.class, () -> registerUseCase.execute(request));

        assertEquals("Nickname nickName already exists", exception.getMessage());

        verify(userPort, never()).save(any(User.class));
        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(hashPort, never()).hash(anyString());
        verify(tokenPort, never()).generateToken(any(User.class), any(LocalDateTime.class));

    }

}