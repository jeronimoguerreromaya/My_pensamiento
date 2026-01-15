package com.mypensamiento.mypensamiento.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.PasswordEncoderPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class UpdatePasswordUseCaseTest {

    @Mock
    private UserPort userPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private HashPort hashPort;

    @Mock
    private RefreshTokenPort refreshTokenPort;

    private UpdatePasswordUseCase updatePasswordUseCase;

    @Test
    void updatePassword_whenPasswordIsCorrect_shouldUpdateSuccessfully() {
       /* //Arrange
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

        when(userPort.getById(id)).thenReturn(user);
        when(passwordEncoderPort.matches(request.password(), encodedPassword)).thenReturn(true);
        when(passwordEncoderPort.encode("<PASSWORD>")).thenReturn(encodedPassword);

        //Act
        updatePasswordUseCase.execute(request, id);

        //Assert
        verify(userPort,times(1)).getById(id);
        verify(userPort).save(user);*/
    }

}
