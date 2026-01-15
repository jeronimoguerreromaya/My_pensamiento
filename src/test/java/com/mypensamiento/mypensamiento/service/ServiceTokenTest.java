package com.mypensamiento.mypensamiento.service;

import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;
import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ServiceTokenTest {

    @Mock
    TokenPort tokenPort;

    @InjectMocks
    ServiceToken serviceToken;

    @Test
    void generateAuth_whenUserIsValid_shouldGenerateToken() {
        //Arrange
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        TokenResponse accessMock = new TokenResponse("access-123", now.plusHours(1));
        TokenResponse refreshMock = new TokenResponse("refresh-456", now.plusDays(7));

        when(tokenPort.generateToken(user, now)).thenReturn(accessMock);
        when(tokenPort.generateRefreshToken(user, now)).thenReturn(refreshMock);

        // Act
        AuthResponse response = serviceToken.generateAuth(user, now);

        // Assert
        assertNotNull(response);
        assertEquals("access-123", response.access());
        assertEquals("refresh-456", response.refresh());
        assertEquals(accessMock.expirationDate(), response.accessExpiresIn());
        assertEquals(refreshMock.expirationDate(), response.refreshExpiresIn());

        verify(tokenPort, times(1)).generateToken(user, now);
        verify(tokenPort, times(1)).generateRefreshToken(user, now);
    }
}
