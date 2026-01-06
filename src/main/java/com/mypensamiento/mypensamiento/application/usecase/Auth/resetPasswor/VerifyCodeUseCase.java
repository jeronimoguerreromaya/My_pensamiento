package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPasswor;

import com.mypensamiento.mypensamiento.application.exception.InvalidCodeException;
import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.dto.TokenResponse;

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

    public TokenResponse execute(String opt, String email) {
        if(opt==null || email==null){
            throw new InvalidCodeException("Fields are required");
        }

        String optHash = hashPort.hash(opt);

        PasswordResetCode saveCode = passwordResetCodePort.getByUserEmail(email);

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

        if(!email.equals(saveCode.getUserEmail())){
            throw new InvalidCodeException("not match");
        }

        User user = userPort.findByEmail(email);

        return tokenPort.generatePasswordResetToken(user, LocalDateTime.now());

    }
}
