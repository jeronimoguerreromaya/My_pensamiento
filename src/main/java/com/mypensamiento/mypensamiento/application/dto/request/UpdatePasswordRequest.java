package com.mypensamiento.mypensamiento.application.dto.request;

public record UpdatePasswordRequest(
        String password,
        String newPassword,
        String confirmPassword
) {
}
