package com.mypensamiento.mypensamiento.application.dto.request.resetPassword;

public record ValidateCodeRequest(
        String code,
        String email
) {
}
