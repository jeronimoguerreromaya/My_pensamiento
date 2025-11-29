package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.UserJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryJpaAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    // TODO: implementar métodos de UserRepository cuando se definan en el dominio.
}