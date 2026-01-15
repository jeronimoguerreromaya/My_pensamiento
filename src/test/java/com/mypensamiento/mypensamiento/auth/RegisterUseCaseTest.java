package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RegisterUseCase;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;
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

    @Mock
    ServiceToken serviceToken;

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

        String myAccessToken = "new-access";
        String myRefreshToken = "new-refresh";

        AuthResponse authResponse = new AuthResponse(
                myAccessToken,
                myRefreshToken,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        when(serviceToken.generateAuth(eq(savedUser), any(LocalDateTime.class))).thenReturn(authResponse);

        when(hashPort.hash(myRefreshToken)).thenReturn("hashed-refresh-token");

        //Act
        AuthResponse response = registerUseCase.execute(request);

        //Assert
        assertNotNull(response);
        assertEquals("new-access", response.access());
        assertEquals("new-refresh", response.refresh());

        verify(userPort, times(1)).save(any(User.class));
        verify(refreshTokenPort, times(1)).save(any(RefreshToken.class));
        verify(passwordEncoderPort).encode(request.password());

    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowEmailAlreadyExistException (){
        //Arrage
        String email = "example@example.com";
        RegisterUserRequest request = new RegisterUserRequest(
                "nickName",
                email,
                "password",
                "full_name",
                "bio",
                "profile_picture"
        );

        when(userPort.existsByEmail(email)).thenReturn(true);

        //Act-Assert
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> registerUseCase.execute(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userPort, never()).save(any(User.class));
        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(hashPort, never()).hash(anyString());
        verify(tokenPort, never()).generateToken(any(User.class), any(LocalDateTime.class));

    }

    @Test
    void register_whenNickNameAlreadyExists_shouldThrowNickNameAlreadyExistsException (){
        //Arrage

        String nickName = "nickName";

        RegisterUserRequest request = new RegisterUserRequest(
                nickName,
                "example@example.com",
                "password",
                "full_name",
                "bio",
                "profile_picture"
        );

        when(userPort.existsByNickname(nickName)).thenReturn(true);

        //Act-Assert
        NickNameAlreadyExistsException exception = assertThrows(NickNameAlreadyExistsException.class, () -> registerUseCase.execute(request));

        assertEquals("Nickname already exists", exception.getMessage());

        verify(userPort, never()).save(any(User.class));
        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
        verify(passwordEncoderPort, never()).encode(anyString());
        verify(hashPort, never()).hash(anyString());
        verify(tokenPort, never()).generateToken(any(User.class), any(LocalDateTime.class));

    }

}