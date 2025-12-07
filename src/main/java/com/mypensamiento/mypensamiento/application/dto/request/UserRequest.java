package com.mypensamiento.mypensamiento.application.dto.request;


public record UserRequest(

        String nickname,
        String email,
        String password,
        String full_name,
        String bio,
        String profile_picture
) {
}
