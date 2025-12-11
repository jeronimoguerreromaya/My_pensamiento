package com.mypensamiento.mypensamiento.user;

import com.mypensamiento.mypensamiento.application.dto.request.UserRequest;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.model.categorie.Role;
import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateUserPatchUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @InjectMocks
    private UpdateUserPatchUseCase updateUserPatchUseCase;

    @Test
    void updateUser_whenUserExist_shouldUpdateSuccessfully() {
        // Arrange
        Long id = 1L;
        UserRequest request = new UserRequest(
                "nickNameUpdate",
                "upt@gmail.com",
                "1234",
                "full_name_upt",
                "update bio",
                "upt.jpg"
        );

        User mockUser = new User();
            mockUser.setId(id);
            mockUser.setNickname("nickName");
            mockUser.setEmail("example@gmail.com");
            mockUser.setPassword("<PASSWORD>");
            mockUser.setFull_name("full_name");
            mockUser.setBio("bio");
            mockUser.setProfile_picture("profile.jpg");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.getById(id)).thenReturn(mockUser);

        // Act
        updateUserPatchUseCase.execute(request, 1L);

        // Assert
        verify(userRepository, times(1)).getById(id);

        verify(userRepository).save(userArgumentCaptor.capture());

        User captorUser = userArgumentCaptor.getValue();

        assert captorUser.getNickname().equals("nickNameUpdate");
        assert captorUser.getEmail().equals("upt@gmail.com");
        assert captorUser.getFull_name().equals("full_name_upt");
        assert captorUser.getBio().equals("update bio");
        assert captorUser.getProfile_picture().equals("upt.jpg");

    }

}
