package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.LikeRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.LikeJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    public LikeRepositoryJpaAdapter(LikeJpaRepository likeJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
    }

    // TODO: implementar métodos de LikeRepository cuando se definan en el dominio.
}