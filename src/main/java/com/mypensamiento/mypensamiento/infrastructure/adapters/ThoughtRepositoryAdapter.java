package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.Thought;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.ThoughMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.ThoughtJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ThoughtRepositoryAdapter implements ThoughtPort {

    private final ThoughtJpaRepository thoughtJpaRepository;

    public ThoughtRepositoryAdapter(ThoughtJpaRepository thoughtJpaRepository) {
        this.thoughtJpaRepository = thoughtJpaRepository;
    }

    @Override
    public void save(Thought thought) {
        ThoughtEntity entity = new ThoughMapper().toEntity(thought);
        this.thoughtJpaRepository.save(entity);

    }

    @Override
    public Boolean existsById(Long id) {
        return thoughtJpaRepository.existsById(id);
    }

}