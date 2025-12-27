package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.User;

public interface UserPort {

    User save(User user);

    User getById(Long id);

    User findByEmail(String email);

    Boolean existsById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);


}
