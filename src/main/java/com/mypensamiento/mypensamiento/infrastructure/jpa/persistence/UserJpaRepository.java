package com.mypensamiento.mypensamiento.infrastructure.jpa.persistence;

import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

}