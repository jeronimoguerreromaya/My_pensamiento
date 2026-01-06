package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;

public interface PasswordResetCodePort {

    void save(PasswordResetCode passwordResetCode);

    void markUsedByEmail(String email);

    PasswordResetCode getByCode(String code);

    PasswordResetCode getByUserEmail(String email);
}
