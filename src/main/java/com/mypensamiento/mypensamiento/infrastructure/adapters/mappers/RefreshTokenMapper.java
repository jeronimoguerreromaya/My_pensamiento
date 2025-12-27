package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.RefreshToken;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.RefreshTokenEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;

public class RefreshTokenMapper {

    public RefreshTokenEntity toEntity(RefreshToken domain) {
        if (domain == null) return null;
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setCreatedAt(domain.getCreated_at());
        entity.setExpiresAt(domain.getExpires_at());
        entity.setRevoked(domain.isRevoked());

        // Nota: Aquí asumimos que asignas el usuario en el Adapter o que el dominio tiene el objeto User completo
        // Para simplificar, solo mapeamos el ID si es necesario o lo manejamos en el adapter
        UserEntity userEntity = new UserEntity();
        userEntity.setId(domain.getUser_id());
        entity.setUser(userEntity);

        return entity;
    }

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) return null;
        RefreshToken domain = new RefreshToken();
        domain.setId(entity.getId());
        domain.setUser_id(entity.getUser().getId());
        domain.setToken(entity.getToken());
        domain.setCreated_at(entity.getCreatedAt());
        domain.setExpires_at(entity.getExpiresAt());
        domain.setRevoked(entity.isRevoked());
        return domain;
    }

}
