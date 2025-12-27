package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Modifying
    @Query("""
    UPDATE RefreshTokenEntity rt
    SET rt.revoked = true
    WHERE rt.user.email = :email
    """)
    void revokeByEmail(@Param("email") String email);

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshTokenEntity r SET r.valid = false, r.revoked = true " +
            "WHERE r.user.id = :userId AND (r.valid = true OR r.revoked = false)")
    void revokeAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE RefreshTokenEntity r SET r.valid = false, r.revoked = true " +
            "WHERE r.user.email = :userEmail AND (r.valid = true OR r.revoked = false)")
    void revokeAllByUserEmail(@Param("userEmail") String userEmail);

}