package com.mypensamiento.mypensamiento.application.dto.request;


public record UpdateUserProfileRequest(
        String nickname,
        String full_name,
        String bio,
        String profile_picture
) {
}
