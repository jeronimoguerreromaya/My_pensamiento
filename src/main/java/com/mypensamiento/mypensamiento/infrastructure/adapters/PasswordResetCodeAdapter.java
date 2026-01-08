package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.domain.ports.PasswordResetCodePort;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.PasswordResetCodeMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.PasswordResetCodeEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.PasswordResetCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordResetCodeAdapter implements PasswordResetCodePort {

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    public PasswordResetCodeAdapter(PasswordResetCodeRepository passwordResetCodeRepository) {
        this.passwordResetCodeRepository = passwordResetCodeRepository;
    }

    @Override
    public void save(PasswordResetCode passwordResetCode) {
        PasswordResetCodeEntity entity = new PasswordResetCodeMapper().toEntity(passwordResetCode);
        passwordResetCodeRepository.save(entity);
    }

    @Override
    @Transactional
    public void markUsedAllByEmail(String email) {
        passwordResetCodeRepository.markAllAsUsedByEmail(email);
    }

    @Override
    public PasswordResetCode getByCode(String code) {

        System.out.println(code);
        PasswordResetCodeEntity entity =
                passwordResetCodeRepository.findByHashedCode(code)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Entity not found"));

        return new PasswordResetCodeMapper().toDomain(entity);
    }

    @Override
    public PasswordResetCode getByUserEmail(String email) {

        PasswordResetCodeEntity entity =
                passwordResetCodeRepository.findByUserEmail(email)
                        .orElseThrow(()->
                                new EntityNotFoundException("Entity not found"));
        return new PasswordResetCodeMapper().toDomain(entity);
    }
}
