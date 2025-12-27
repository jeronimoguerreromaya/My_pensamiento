package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.domain.ports.RefreshTokenPort;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.RefreshTokenMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.RefreshTokenEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.RefreshTokenJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryAdapter implements RefreshTokenPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Override
    public RefreshToken findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash)
                .map(new RefreshTokenMapper()::toDomain)
                .orElse(null);
    }


    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = new RefreshTokenMapper().toEntity(refreshToken);
        refreshTokenJpaRepository.save(entity);
    }

    @Override
    public void revokeByUserId(Long id) {

    }

    @Transactional
    @Override
    public void revokeByEmail(String email) {
        refreshTokenJpaRepository.revokeByEmail(email);
    }

}