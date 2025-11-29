package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.LikeEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeEntity, LikeId> {
}