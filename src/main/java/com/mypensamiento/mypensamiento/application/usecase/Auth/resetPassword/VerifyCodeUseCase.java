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
        if(request.code()==null || request.email()==null){
            throw new InvalidCodeException("Fields are required");
        }

        String optHash = hashPort.hash(request.code());

        PasswordResetCode saveCode = passwordResetCodePort.getByUserEmail( request.email());

        if (saveCode == null || !saveCode.canTryAgain()) {
            throw new InvalidCodeException("Invalid Code, please try again or request a new one");
        }

        if(!saveCode.getHashedCode().equals(optHash)){
            saveCode.registerFailedAttempt();
            passwordResetCodePort.save(saveCode);
            throw new InvalidCodeException("The provided code is incorrect");
        }

        saveCode.markAsUsed();

        passwordResetCodePort.save(saveCode);

        if(! request.email().equals(saveCode.getUserEmail())){
            throw new InvalidCodeException("not match");
        }

        User user = userPort.findByEmail( request.email());

        return tokenPort.generatePasswordResetToken(user, LocalDateTime.now());

    }
}
