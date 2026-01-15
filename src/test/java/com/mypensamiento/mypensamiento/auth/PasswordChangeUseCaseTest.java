package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.PasswordChangeRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.PasswordChangeUseCase;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordChangeUseCaseTest {
    @Mock
    UserPort userPort;

    @Mock
    PasswordEncoderPort passwordEncoderPort;

    @Mock
    TokenPort tokenPort;

    @Mock
    HashPort hashPort;

    @Mock
    RefreshTokenPort refreshTokenPort;

    @Mock
    ServiceToken serviceToken;

    @InjectMocks
    PasswordChangeUseCase passwordChangeUseCase;

    @Test
    void passwordChange_whenTokenIsValid_shouldChangePassword(){
        // Arrange
        String token = "token12345";
        String newPassword = "newSecurePassword";
        String encodedPassword = "encoded_newSecurePassword";
        String email = "user@email.com";
        String refreshTokenHash = "hashed_refresh_token";

        PasswordChangeRequest request = new PasswordChangeRequest(token, newPassword);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(tokenPort.isResetTokenValid(request.token())).thenReturn(true);
        when(tokenPort.extractUsername(request.token())).thenReturn(email);
        when(userPort.findByEmail(email)).thenReturn(user);

        when(passwordEncoderPort.encode(newPassword)).thenReturn(encodedPassword);
        when(userPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse expectedResponse = new AuthResponse(
                "access-token",
                "refresh-token",
                LocalDateTime.now().plusMinutes(15),
                LocalDateTime.now().plusDays(7)
        );

        when(serviceToken.generateAuth(any(User.class), any(LocalDateTime.class)))
                .thenReturn(expectedResponse);

        when(hashPort.hash(expectedResponse.refresh())).thenReturn(refreshTokenHash);

        //Act
        AuthResponse result = passwordChangeUseCase.execute(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.access(), result.access());
        assertEquals(expectedResponse.refresh(), result.refresh());

        verify(userPort).save(argThat(u -> u.getPassword().equals(encodedPassword)));
        verify(refreshTokenPort).save(any(RefreshToken.class));
        verify(hashPort).hash(expectedResponse.refresh());


    }

    @Test
    void passwordChange_whenTokenIsInvalid_shouldThrowUnauthorizedException() {
        // Arrange
        String token = "token12345";
        String newPassword = "newSecurePassword";
        PasswordChangeRequest request = new PasswordChangeRequest(token, newPassword);

        when(tokenPort.isResetTokenValid(request.token())).thenReturn(false);

        // Act
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> passwordChangeUseCase.execute(request)
        );

        // Assert
        verifyNoInteractions(userPort,serviceToken,hashPort,refreshTokenPort);
    }


}
