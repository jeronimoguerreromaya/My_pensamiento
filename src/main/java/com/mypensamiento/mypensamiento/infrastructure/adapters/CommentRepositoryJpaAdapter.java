package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.CommentRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.CommentJpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class CommentRepositoryJpaAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    public CommentRepositoryJpaAdapter(CommentJpaRepository commentJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
    }

    // TODO: implementar métodos de CommentRepository cuando se definan en el dominio.
}