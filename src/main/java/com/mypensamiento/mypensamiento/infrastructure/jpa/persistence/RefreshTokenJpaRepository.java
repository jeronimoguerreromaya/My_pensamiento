package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
}