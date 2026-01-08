package com.mypensamiento.mypensamiento.application.dto.response;

import java.time.LocalDateTime;

public record TokenResponse(
        String token,
        LocalDateTime expirationDate
) {
}
