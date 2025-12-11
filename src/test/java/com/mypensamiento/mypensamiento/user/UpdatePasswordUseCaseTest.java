package com.mypensamiento.mypensamiento.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @InjectMocks
    private UpdatePasswordUseCase updatePasswordUseCase;

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
