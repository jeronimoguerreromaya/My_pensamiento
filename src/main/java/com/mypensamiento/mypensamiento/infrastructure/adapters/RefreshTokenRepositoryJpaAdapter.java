package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.RefreshTokenRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryJpaAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenRepositoryJpaAdapter(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    // TODO: implementar métodos de RefreshTokenRepository cuando se definan en el dominio.
}