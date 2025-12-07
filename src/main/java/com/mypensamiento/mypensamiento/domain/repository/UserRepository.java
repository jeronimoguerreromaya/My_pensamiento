package com.mypensamiento.mypensamiento.domain.repository;

import com.mypensamiento.mypensamiento.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    User getById(Long id);

    Boolean existsById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);



}
