package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.PasswordResetCodeEntity;

public class PasswordResetCodeMapper {

    public PasswordResetCode toDomain(PasswordResetCodeEntity entity){
        return new PasswordResetCode(
                entity.getId(),
                entity.getUserEmail(),
                entity.getHashedCode(),
                entity.getExpiryDate(),
                entity.getAttempts(),
                entity.isUsed()
        );
    }

    public PasswordResetCodeEntity toEntity(PasswordResetCode domain){
        PasswordResetCodeEntity entity = new PasswordResetCodeEntity();
        entity.setId(domain.getId());
        entity.setUserEmail(domain.getUserEmail());
        entity.setHashedCode(domain.getHashedCode());
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setAttempts(domain.getAttempts());
        entity.setUsed(domain.isUsed());
        return entity;
    }

}
