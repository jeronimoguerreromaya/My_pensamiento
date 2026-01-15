package com.mypensamiento.mypensamiento.auth;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.PasswordResetRequest;
import com.mypensamiento.mypensamiento.application.service.ServiceRandomCode;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.SendCodeUseCase;
import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.ports.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendCodeUseCaseTest {

    @Mock
    EmailPort emailPort;

    @Mock
    PasswordResetCodePort passwordResetCodePort;

    @Mock
    HashPort hashPort;

    @Mock
    UserPort userPort;

    @Mock
    ServiceRandomCode serviceRandomCode;

    @InjectMocks
    SendCodeUseCase sendCodeUseCase;

    @Test
    void sendCode_whenEmailIsValid_shouldSendCode(){
        // Arrange
        String email = "usuario@ejemplo.com";
        String rawCode = "123456";
        String hashedCode = "hash_123456";
        PasswordResetRequest request = new PasswordResetRequest(email);


        when(userPort.existsByEmail(email)).thenReturn(true);

        when(serviceRandomCode.generateSixDigitCode()).thenReturn(rawCode);

        when(hashPort.hash(rawCode)).thenReturn(hashedCode);

        //Act
        sendCodeUseCase.execute(request);

        //Assert
        verify(passwordResetCodePort).markUsedAllByEmail(email);

        ArgumentCaptor<PasswordResetCode> codeCaptor = ArgumentCaptor.forClass(PasswordResetCode.class);
        verify(passwordResetCodePort).save(codeCaptor.capture());

        PasswordResetCode savedCode = codeCaptor.getValue();
        assertEquals(email, savedCode.getUserEmail());
        assertEquals(hashedCode, savedCode.getHashedCode());

        verify(emailPort).send(
                eq(email),
                eq("Reset Password"),
                contains(rawCode)
        );

        verifyNoMoreInteractions(passwordResetCodePort, emailPort);

    }

    @Test
    void sendCode_whenUserDoesNotExist_shouldDoNothing() {
        // Arrange
        String email = "usuario@ejemplo.com";

        PasswordResetRequest request = new PasswordResetRequest(email);

        when(userPort.existsByEmail(email)).thenReturn(false);

        // Act
        sendCodeUseCase.execute(request);

        // Assert
        verifyNoInteractions(passwordResetCodePort, emailPort,serviceRandomCode);
    }

}
