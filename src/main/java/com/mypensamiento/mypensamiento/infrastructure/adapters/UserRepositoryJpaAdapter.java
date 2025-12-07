package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.UserMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryJpaAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }



    @Override
    public void save(User user) {
        UserEntity entity = new UserMapper().toEntity(user);
        userJpaRepository.save(entity);
    }

    @Override
    public User getById(Long id) {
       UserEntity entity = userJpaRepository.findById(id).orElse(null);
        return new UserMapper().toDomain(entity);
    }

    @Override
    public Boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }


}