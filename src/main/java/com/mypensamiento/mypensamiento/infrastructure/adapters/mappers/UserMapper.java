package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapper.class);

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            LOGGER.warn("UserMapper.toDomain called with null entity");
            return null;
        }

        LOGGER.debug("Mapping UserEntity to User: id={}, nickname={}", entity.getId(), entity.getNickname());

        return new User()
                .setId(entity.getId())
                .setNickname(entity.getNickname())
                .setEmail(entity.getEmail())
                .setPassword(entity.getPassword())
                .setFull_name(entity.getFullName())
                .setBio(entity.getBio())
                .setProfile_picture(entity.getProfilePicture())
                .setCreated_at(entity.getCreatedAt())
                .setRole(entity.getRole())
                .setStatus(entity.getStatus());
    }

    public UserEntity toEntity(User user){
        return new UserEntity()
                .setId(user.getId())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFullName(user.getFull_name())
                .setBio(user.getBio())
                .setProfilePicture(user.getProfile_picture())
                .setCreatedAt(user.getCreated_at())
                .setRole(user.getRole())
                .setStatus(user.getStatus());
    }
}
