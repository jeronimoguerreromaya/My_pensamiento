package com.mypensamiento.mypensamiento.application.dto.request;


public record RegisterUserRequest(

        String nickname,
        String email,
        String password,
        String full_name,
        String bio,
        String profile_picture
) {
}
