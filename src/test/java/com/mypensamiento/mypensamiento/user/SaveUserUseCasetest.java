package com.mypensamiento.mypensamiento.user;

import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.model.categorie.Role;
import com.mypensamiento.mypensamiento.domain.ports.PasswordEncoderPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveUserUseCasetest {

    @Mock
    private UserPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderRepository;

    @Test
        //shouldThrowExceptionWhenNicknameIsEmpty
    void saveUser_WhenNickNameIsEmpty_ShouldThrowExceptionn() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "",
                "test@example.com",
                "password123",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        // Act & Assert
        assertThrows(FieldValidationException.class, () -> saveUserUseCase.execute(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @InjectMocks
    private SaveUserUseCase saveUserUseCase;

    @Test
    void saveUser_WhenUserIsNew_ShouldSaveSuccessfully() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoderRepository.encode(request.password())).thenReturn("encodedPassword");

        // Act
        saveUserUseCase.execute(request);

        // Assert
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        verify(passwordEncoderRepository, times(1)).encode(request.password());

        User captorUser = userArgumentCaptor.getValue();

        assert captorUser.getNickname().equals("testuser");
        assert captorUser.getEmail().equals("test@example.com");
        assert captorUser.getRole().equals(Role.USER);
        assert captorUser.getStatus().equals(Boolean.TRUE);

    }

    @Test
    void saveUser_WhenEmailIsEmpty_ShouldThrowExceptionn() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "testuser",
                "",
                "password123",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        // Act & Assert
        assertThrows(FieldValidationException.class, () -> saveUserUseCase.execute(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUser_WhenPasswordIsEmpty_ShouldThrowExceptionn() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "testuser",
                "test@example.com",
                "",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        // Act & Assert
        assertThrows(FieldValidationException.class, () -> saveUserUseCase.execute(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUSer_WhenNickNameAlreadyExists_ShouldNickNameAlreadyExistsException() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "existinguser",
                "test@example.com",
                "password123",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        when(userRepository.existsByNickname(request.nickname())).thenReturn(true);

        // Act & Assert
        assertThrows(NickNameAlreadyExistsException.class, () -> saveUserUseCase.execute(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUSer_WhenEmailAlreadyExists_ShouldEmailAlreadyExistsException() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest(
                "testuser",
                "existing@example.com",
                "password123",
                "Test User",
                "Bio",
                "profile.jpg"
        );

        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> saveUserUseCase.execute(request));
        verify(userRepository, never()).save(any(User.class));
    }
}
