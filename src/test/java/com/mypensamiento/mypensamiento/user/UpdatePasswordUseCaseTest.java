package com.mypensamiento.mypensamiento.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.PasswordEncoderPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

public class UpdatePasswordUseCaseTest {

    @Mock
    private UserPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderRepository;

    @Mock
    private TokenPort tokenPort;

    @Mock
    private HashPort hashPort;

    @Mock
    private RefreshTokenPort refreshTokenPort;

    private UpdatePasswordUseCase updatePasswordUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserPort.class);
        passwordEncoderRepository = mock(PasswordEncoderPort.class);
        tokenPort = mock(TokenPort.class);
        hashPort = mock(HashPort.class);
        refreshTokenPort = mock(RefreshTokenPort.class);

        updatePasswordUseCase = new UpdatePasswordUseCase(
                userRepository,
                passwordEncoderRepository,
                tokenPort,
                hashPort,
                refreshTokenPort
        );
    }

    @Test
    void updatePassword_whenPasswordIsCorrect_shouldUpdateSuccessfully() {
        //Arrange
        Long id = 1L;
        UpdatePasswordRequest request = new UpdatePasswordRequest(
                "password",
                "<PASSWORD>",
                "<PASSWORD>"
        );

        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setId(id);
        user.setPassword(encodedPassword);

        when(userRepository.getById(id)).thenReturn(user);
        when(passwordEncoderRepository.matches(request.password(), encodedPassword)).thenReturn(true);
        when(passwordEncoderRepository.encode("<PASSWORD>")).thenReturn(encodedPassword);


        //Act
        updatePasswordUseCase.execute(request, id);

        //Assert
        verify(userRepository,times(1)).getById(id);
        verify(userRepository).save(user);
    }

}
