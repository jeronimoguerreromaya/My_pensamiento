package com.mypensamiento.mypensamiento.application.dto.request.resetPassword;

public record PasswordChangeRequest (
        String token,
        String password
){
}
