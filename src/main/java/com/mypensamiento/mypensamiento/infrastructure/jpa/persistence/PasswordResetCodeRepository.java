package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.domain.model.PasswordResetCode;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.PasswordResetCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCodeEntity, Long> {

    @Modifying
    @Query(
            "UPDATE PasswordResetCodeEntity pr " +
                    "SET pr.used = true " +
                    "WHERE pr.userEmail = :email"
    )
    void markAllAsUsedByEmail(@Param("email") String email);

    Optional<PasswordResetCodeEntity> findByHashedCode(String code);

    @Query(
            "SELECT pr FROM PasswordResetCodeEntity pr WHERE pr.used = false AND pr.userEmail = :email"
    )
    Optional<PasswordResetCodeEntity> findByUserEmail(@Param("email") String email);
}
