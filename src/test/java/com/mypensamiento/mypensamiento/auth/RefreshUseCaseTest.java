package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UnauthorizedException;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RefreshUseCase;
import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshUseCaseTest {

    @Mock
    RefreshTokenPort refreshTokenPort;

    @Mock
    UserPort userPort;

    @Mock
    TokenPort tokenPort;

    @Mock
    HashPort hashPort;

    @InjectMocks
    RefreshUseCase refreshUseCase;

    @Test
    void refresh_whenRefreshTokenIsValid_shouldReturnNewAccessTokenAndRefreshToken(){
        //Arrange
        String rawToken = "validToken";
        String header = "Bearer " + rawToken;
        String email = "test@example.com";
        String hashedCurrentToken = "hashed-old-token";

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        when(userPort.findByEmail(email)).thenReturn(user);

        when(tokenPort.validateToken(rawToken, user)).thenReturn(true);
        when(hashPort.hash(rawToken)).thenReturn(hashedCurrentToken);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setValid(true);
        storedToken.setRevoked(false);
        storedToken.setExpires_at(LocalDateTime.now().plusDays(1));
        when(refreshTokenPort.findByTokenHash(hashedCurrentToken)).thenReturn(storedToken);

        TokenResponse mockAccess = new TokenResponse("new-access", LocalDateTime.now().plusHours(1));
        TokenResponse mockRefresh = new TokenResponse("new-refresh", LocalDateTime.now().plusDays(1));
        when(tokenPort.generateToken(eq(user), any(LocalDateTime.class))).thenReturn(mockAccess);
        when(tokenPort.generateRefreshToken(eq(user), any(LocalDateTime.class))).thenReturn(mockRefresh);
        when(hashPort.hash("new-refresh")).thenReturn("hashed-new-refresh");

        //Act
        AuthResponse response = refreshUseCase.execute(header);

        //Assert
        assertNotNull(response);
        assertEquals("new-access", response.accestoken());
        assertEquals("new-refresh", response.refreshToken());

        assertFalse(storedToken.isValid());
        assertEquals("hashed-new-refresh", storedToken.getReplaced_by_hash());

        verify(refreshTokenPort, times(2)).save(any(RefreshToken.class));

    }

    @Test
    void refresh_whenHeaderIsInvalid_shouldThrowException(){
        // Arrange
        String authHeader = "Basic dXNlcjpwYXNz";

        // Act & Assert
        FieldValidationException exception = assertThrows(FieldValidationException.class, () -> {
            refreshUseCase.execute(authHeader);
        });

        assertEquals("Invalid Bearer token", exception.getMessage());

    }

    @Test
    void refresh_whenUserNotFound_shouldThrowUnauthorizedException() {
        //Arrange
        String rawToken = "validToken";
        String header = "Bearer " + rawToken;
        String email = "test@example.com";

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(false);

        //Act-Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshUseCase.execute(header);
        });

        assertEquals("Invalid token request", exception.getMessage());

        verify(userPort).existsByEmail(email);
        verify(userPort, never()).findByEmail(anyString());
        verifyNoInteractions(refreshTokenPort, hashPort);

    }
    @Test
    void refresh_whenTokenIsRevoked_shouldThrowUnauthorizedException() {
        // Arrange
        String rawToken = "old-token";
        String header = "Bearer " + rawToken;
        String email = "user@example.com";
        String hashedToken = "hashed-old-token";
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setRevoked(true);

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);
        when(userPort.findByEmail(email)).thenReturn(user);
        when(tokenPort.validateToken(rawToken, user)).thenReturn(true);
        when(hashPort.hash(rawToken)).thenReturn(hashedToken);
        when(refreshTokenPort.findByTokenHash(hashedToken)).thenReturn(storedToken);

        //Act
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshUseCase.execute(header);
        });
        //Assert
        assertEquals("Security alert: Token reuse detected",exception.getMessage());

        verify(refreshTokenPort, times(1)).revokrevokeAllByUserIdeAll(userId);

        verify(tokenPort, never()).generateToken(any(), any());
        verify(tokenPort, never()).generateRefreshToken(any(), any());

        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
    }

    @Test
    void refresh_whenRefreshTokenIsInvalid_shouldThrowUnauthorizedException() {
        //Arrange
        String rawToken = "old-token";
        String header = "Bearer " + rawToken;
        String email = "user@example.com";
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        RefreshToken storedToken = new RefreshToken();

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);
        when(userPort.findByEmail(email)).thenReturn(user);

        when(tokenPort.validateToken(rawToken, user)).thenReturn(false);

        //Act
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshUseCase.execute(header);
        });

        //Assert
        assertEquals("Invalid token request",exception.getMessage());

        verify(tokenPort).validateToken(rawToken, user);

        verifyNoInteractions(hashPort);
        verifyNoInteractions(refreshTokenPort);
    }

    @Test
    void refresh_whenTokenIsInvalid_shouldThrowUnauthorizedException() {
        // Arrange
        String rawToken = "old-token";
        String header = "Bearer " + rawToken;
        String email = "user@example.com";
        String hashedToken = "hashed-old-token";
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setValid(false);

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);
        when(userPort.findByEmail(email)).thenReturn(user);
        when(tokenPort.validateToken(rawToken, user)).thenReturn(true);
        when(hashPort.hash(rawToken)).thenReturn(hashedToken);
        when(refreshTokenPort.findByTokenHash(hashedToken)).thenReturn(storedToken);

        //Act
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshUseCase.execute(header);
        });
        //Assert
        assertEquals("Invalid token request",exception.getMessage());


        verify(tokenPort, never()).generateToken(any(), any());
        verify(tokenPort, never()).generateRefreshToken(any(), any());

        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
    }

    @Test
    void refresh_whenInvalidTokenWithoutHash_shouldThrowUnauthorizedException() {
        // Arrange
        String rawToken = "old-token";
        String header = "Bearer " + rawToken;
        String email = "user@example.com";
        String hashedToken = "hashed-old-token";
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setValid(false);
        storedToken.setToken_hash("any-hash");

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);
        when(userPort.findByEmail(email)).thenReturn(user);
        when(tokenPort.validateToken(rawToken, user)).thenReturn(true);
        when(hashPort.hash(rawToken)).thenReturn(hashedToken);
        when(refreshTokenPort.findByTokenHash(hashedToken)).thenReturn(storedToken);

        //Act
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            refreshUseCase.execute(header);
        });
        //Assert
        assertEquals("Invalid token request",exception.getMessage());

        verify(refreshTokenPort, times(1)).revokrevokeAllByUserIdeAll(userId);

        verify(tokenPort, never()).generateToken(any(), any());
        verify(tokenPort, never()).generateRefreshToken(any(), any());

        verify(refreshTokenPort, never()).save(any(RefreshToken.class));
    }
    @Test
    void refresh_whenTransactionTimeIsExpired_shouldThrowUnauthorizedException(){
        //Arrange
        String rawToken = "validToken";
        String header = "Bearer " + rawToken;
        String email = "test@example.com";
        String hashedCurrentToken = "hashed-old-token";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        RefreshToken storedToken = new RefreshToken();
        storedToken.setValid(true);
        storedToken.setRevoked(false);
        storedToken.setExpires_at(LocalDateTime.now().minusDays(1));

        when(tokenPort.extractUsername(rawToken)).thenReturn(email);
        when(userPort.existsByEmail(email)).thenReturn(true);
        when(userPort.findByEmail(email)).thenReturn(user);
        when(tokenPort.validateToken(rawToken, user)).thenReturn(true);
        when(hashPort.hash(rawToken)).thenReturn(hashedCurrentToken);
        when(refreshTokenPort.findByTokenHash(hashedCurrentToken)).thenReturn(storedToken);

        //Act
        UnauthorizedException exception = assertThrows(UnauthorizedException.class , () -> {
            refreshUseCase.execute(header);
        });

        //Assert
        assertEquals("Invalid token request",exception.getMessage());

        verify(refreshTokenPort,times(1)).revokrevokeAllByUserIdeAll(user.getId());

        verify(tokenPort,never()).generateToken(any(), any());
        verify(tokenPort,never()).generateRefreshToken(any(), any());
        verify(refreshTokenPort,never()).save(any(RefreshToken.class));


    }



}



//validar timpo de token

