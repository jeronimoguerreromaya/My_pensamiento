package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword;

import com.mypensamiento.mypensamiento.application.dto.request.resetPassword.PasswordResetRequest;
import com.mypensamiento.mypensamiento.application.service.ServiceRandomCode;
import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.ports.EmailPort;
import com.mypensamiento.mypensamiento.domain.ports.HashPort;
import com.mypensamiento.mypensamiento.domain.ports.PasswordResetCodePort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;

import java.security.SecureRandom;

public class SendCodeUseCase {

    private final EmailPort emailPort;
    private final PasswordResetCodePort passwordResetCodePort;
    private final HashPort hashPort;
    private final UserPort userPort;
    private final ServiceRandomCode serviceRandomCode;
    public SendCodeUseCase(
            EmailPort emailPort,
            PasswordResetCodePort passwordResetCodePort,
            HashPort hashPort,
            UserPort userPort,
            ServiceRandomCode serviceRandomCode
    ) {
        this.emailPort = emailPort;
        this.passwordResetCodePort = passwordResetCodePort;
        this.hashPort = hashPort;
        this.userPort = userPort;
        this.serviceRandomCode = serviceRandomCode;
    }

    public void execute (PasswordResetRequest request){
        if(!userPort.existsByEmail(request.email())){
            return;
        }

        passwordResetCodePort.markUsedAllByEmail(request.email());

        String opt = serviceRandomCode.generateSixDigitCode();

        PasswordResetCode passwordResetCode = new PasswordResetCode(
                request.email(),
                hashPort.hash(opt),
                5
        );

        passwordResetCodePort.save(passwordResetCode);

        emailPort.send(request.email(), "Reset Password", "Your reset code is: " + opt);

    }

}
