package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.RefreshToken;

public interface RefreshTokenPort {

    RefreshToken findByTokenHash(String tokenHash);

    void save(RefreshToken refreshToken);

    void revokeByUserId(Long id);

    void revokeByEmail(String email);

    void revokeByTokenHash(String tokenHash);

    void revokrevokeAllByUserIdeAll(Long userId);

    void revokrevokeAllByUserEmailAll(String userEmail);
}
