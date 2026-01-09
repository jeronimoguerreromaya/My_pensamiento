package com.mypensamiento.mypensamiento.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record AuthResponse (
        String access,
        String refresh,
        LocalDateTime accessExpiresIn,
        LocalDateTime refreshExpiresIn
){
}
