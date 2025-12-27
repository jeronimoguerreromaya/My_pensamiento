package com.mypensamiento.mypensamiento.infrastructure.dto;

import java.time.LocalDateTime;

public record TokenResponse(
        String token,
        LocalDateTime expirationDate
) {
}
