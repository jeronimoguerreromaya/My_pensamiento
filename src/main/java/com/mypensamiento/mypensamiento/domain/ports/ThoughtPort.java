package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.Thought;

public interface ThoughtPort {

    void save(Thought thought);

    Boolean existsById(Long id);

}
