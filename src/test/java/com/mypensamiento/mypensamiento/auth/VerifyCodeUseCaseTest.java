package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.ValidateCodeRequest;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;
import com.mypensamiento.mypensamiento.application.exception.InvalidCodeException;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.VerifyCodeUseCase;
import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.PasswordResetCodePort;
import com.mypensamiento.mypensamiento.domain.ports.TokenPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyCodeUseCaseTest {
    @Mock
    PasswordResetCodePort passwordResetCodePort;

    @Mock
    HashPort hashPort;

    @Mock
    UserPort userPort;

    @Mock
    TokenPort tokenPort;

    @InjectMocks
    VerifyCodeUseCase verifyCodeUseCase;

    @Test
    void verifyCode_whenCodeIsValid_shouldReturnTemporaryResetToken() {
        // Arrange
        String email = "example@email.com";
        String otp = "123456";
        String otpHash = "hash_123456";
        String temporaryToken = "token_final";

        ValidateCodeRequest request = new ValidateCodeRequest(otp,email);

        PasswordResetCode otpSave = new PasswordResetCode(
                email,
                otpHash,
                15
        );

        User user = new User();
        user.setEmail(email);

        TokenResponse expectedResponse = new TokenResponse(temporaryToken, LocalDateTime.now().plusMinutes(15));


        when(hashPort.hash(otp)).thenReturn(otpHash);
        when(passwordResetCodePort.getByUserEmail(email)).thenReturn(otpSave);
        when(userPort.findByEmail(email)).thenReturn(user);

        when(tokenPort.generatePasswordResetToken(eq(user), any(LocalDateTime.class)))
                .thenReturn(expectedResponse);

        // Act
        TokenResponse actualResponse = verifyCodeUseCase.execute(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("token_final", actualResponse.token());

        verify(passwordResetCodePort).save(otpSave);
        verify(tokenPort).generatePasswordResetToken(eq(user), any(LocalDateTime.class));

    }

    @Test
    void verifyCode_whenCodeIsUsed_shouldThrowInvalidCodeException() {
        // Arrange
        String email = "example@email.com";
        String otp = "123456";
        String otpHash = "hash_123456";

        ValidateCodeRequest request = new ValidateCodeRequest(otp,email);

        PasswordResetCode otpSave = new PasswordResetCode(
                email,
                otpHash,
                15
        );
        otpSave.markAsUsed();

        when(passwordResetCodePort.getByUserEmail(email)).thenReturn(otpSave);

        // Act
        InvalidCodeException exception = assertThrows(InvalidCodeException.class, () -> {
            verifyCodeUseCase.execute(request);
        });

        // Assert

        assertEquals("The provided code is incorrect", exception.getMessage());

        verify(passwordResetCodePort, never()).save(any());
        verifyNoInteractions(tokenPort, userPort);

    }

    @Test
    void verifyCode_whenCodeIsExpired_shouldThrowInvalidCodeException() {
        // Arrange
        String email = "example@email.com";
        String otp = "123456";
        ValidateCodeRequest request = new ValidateCodeRequest(otp, email);

        PasswordResetCode expiredCode = new PasswordResetCode(
                1L,
                email,
                "any_hash",
                LocalDateTime.now().minusMinutes(10),
                0,
                false
        );

        when(passwordResetCodePort.getByUserEmail(email)).thenReturn(expiredCode);
        // Act & Assert
        assertThrows(InvalidCodeException.class, () -> verifyCodeUseCase.execute(request));

        verifyNoInteractions(tokenPort);
    }

    @Test
    void verifyCode_whenCodeDoesNotMatch_shouldRegisterAttemptAndThrowInvalidCodeException() {
        // Arrange
        String email = "example@email.com";
        String inputOtp = "123456";
        String inputOtpHash = "hash_123456";
        String differentStoredHash = "hash_999999";

        ValidateCodeRequest request = new ValidateCodeRequest(inputOtp,email);

        PasswordResetCode storeOtp = new PasswordResetCode(
                email,
                differentStoredHash,
                15
        );

        when(hashPort.hash(inputOtp)).thenReturn(inputOtpHash);
        when(passwordResetCodePort.getByUserEmail(email)).thenReturn(storeOtp);

        // Act
        InvalidCodeException exception = assertThrows(InvalidCodeException.class, () -> {
            verifyCodeUseCase.execute(request);
        });

        // Assert
        assertEquals("The provided code is incorrect", exception.getMessage());

        assertEquals(1,storeOtp.getAttempts(),"Attempts should have increased to 1");

        verify(passwordResetCodePort, times(1)).save(storeOtp);

        verifyNoInteractions(tokenPort, userPort);
    }

}
