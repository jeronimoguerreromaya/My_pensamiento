package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.ValidateCodeRequest;
import com.mypensamiento.mypensamiento.application.exception.InvalidCodeException;
import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.application.dto.response.TokenResponse;

import java.time.LocalDateTime;

public class VerifyCodeUseCase {
    private final PasswordResetCodePort passwordResetCodePort;
    private final HashPort hashPort;
    private final UserPort userPort;
    private final TokenPort tokenPort;


    public VerifyCodeUseCase(PasswordResetCodePort passwordResetCodePort, HashPort hashPort, UserPort userPort, TokenPort tokenPort) {
        this.passwordResetCodePort = passwordResetCodePort;
        this.hashPort = hashPort;
        this.userPort = userPort;
        this.tokenPort = tokenPort;
    }

    public TokenResponse execute(ValidateCodeRequest request) {
        validateRequest(request);

        PasswordResetCode saveCode = passwordResetCodePort.getByUserEmail( request.email());

        if (saveCode == null || !saveCode.canTryAgain()) {
            throw new InvalidCodeException("The provided code is incorrect");
        }

        String otpHash = hashPort.hash(request.code());
        if(!saveCode.getHashedCode().equals(otpHash)){
            saveCode.registerFailedAttempt();
            passwordResetCodePort.save(saveCode);
            throw new InvalidCodeException("The provided code is incorrect");
        }

        User user = userPort.findByEmail( request.email());
        if (user == null) {
            throw new InvalidCodeException("The provided code is incorrect");
        }

        saveCode.markAsUsed();
        passwordResetCodePort.save(saveCode);

        if(! request.email().equals(saveCode.getUserEmail())){
            throw new InvalidCodeException("The provided code is incorrect");
        }

        return tokenPort.generatePasswordResetToken(user, LocalDateTime.now());
    }

    private void validateRequest(ValidateCodeRequest request) {
        if (request.code() == null || request.email() == null || request.email().isBlank()) {
            throw new InvalidCodeException("Fields are required");
        }
    }
}
