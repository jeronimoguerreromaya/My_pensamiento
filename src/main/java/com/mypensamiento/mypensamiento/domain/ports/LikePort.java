package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.Like;

public interface LikePort {

    void save(Like like);
}
