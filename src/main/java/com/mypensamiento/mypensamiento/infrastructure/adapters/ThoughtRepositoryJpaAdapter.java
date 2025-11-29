package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.ThoughtJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ThoughtRepositoryJpaAdapter implements ThoughtRepository {

    private final ThoughtJpaRepository thoughtJpaRepository;

    public ThoughtRepositoryJpaAdapter(ThoughtJpaRepository thoughtJpaRepository) {
        this.thoughtJpaRepository = thoughtJpaRepository;
    }

    // TODO: implementar métodos de ThoughtRepository cuando se definan en el dominio.
}