package com.mypensamiento.mypensamiento.application.usecase.Auth.resetPasswor;

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
    public SendCodeUseCase(EmailPort emailPort, PasswordResetCodePort passwordResetCodePort, HashPort hashPort, UserPort userPort) {
        this.emailPort = emailPort;
        this.passwordResetCodePort = passwordResetCodePort;
        this.hashPort = hashPort;
        this.userPort = userPort;
    }

    public void execute (String email){
        if(!userPort.existsByEmail(email)){
            return;
        }

        passwordResetCodePort.markUsedByEmail(email);

        String opt = generateSixDigitCode();
        PasswordResetCode passwordResetCode = new PasswordResetCode(
                email,
                hashPort.hash(opt),
                5
        );

        passwordResetCodePort.save(passwordResetCode);

        emailPort.send(email, "Reset Password", "Your reset code is: " + opt);

    }

    public static String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
