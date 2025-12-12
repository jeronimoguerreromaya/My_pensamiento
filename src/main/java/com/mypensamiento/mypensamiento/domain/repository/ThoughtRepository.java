package com.mypensamiento.mypensamiento.domain.repository;

import com.mypensamiento.mypensamiento.domain.model.Thought;

public interface ThoughtRepository {

    void save(Thought thought);

    Boolean existsById(Long id);

}
