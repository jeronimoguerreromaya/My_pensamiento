package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.Thought;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThoughMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThoughMapper.class);

    public Thought toDomain(ThoughtEntity entity) {
        if (entity == null) {
            LOGGER.warn("ThoughMapper.toDomain called with null entity");
            return null;
        }

        Thought thought = new Thought();
        thought.setId(entity.getId());

        UserEntity userEntity = entity.getUser();
        if (userEntity != null && userEntity.getId() != null) {
            thought.setUsers_id(userEntity.getId());
        } else {
            LOGGER.warn("ThoughtEntity has null user or user id when mapping to domain, thoughtEntityId={}", entity.getId());
        }

        thought.setThought_text(entity.getThoughtText());
        thought.setCreated_at(entity.getCreatedAt());
        return thought;
    }

    public ThoughtEntity toEntity(Thought thought) {
        if (thought == null) {
            LOGGER.warn("ThoughMapper.toEntity called with null thought");
            return null;
        }

        ThoughtEntity entity = new ThoughtEntity();
        entity.setId(thought.getId());

        // Map user relationship using only the user id from the domain model
        if (thought.getUsers_id() != 0L) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(thought.getUsers_id());
            entity.setUser(userEntity);
        } else {
            LOGGER.warn("Thought has users_id=0 when mapping to entity, thoughtId={}", thought.getId());
        }

        entity.setThoughtText(thought.getThought_text());
        entity.setCreatedAt(thought.getCreated_at());
        return entity;
    }
}
