package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.Like;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.LikeEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.LikeId;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;

public class LikeMapper {

    public Like toDomain(LikeEntity entity){
        return new Like()
                .setUser_id(entity.getId().getUserId())
                .setThought_id(entity.getId().getThoughtId())
                .setCreated_at(entity.getCreatedAt());
    }

    public LikeEntity toEntity(Like domain, UserEntity user , ThoughtEntity thought){

        if(thought == null || user == null || domain == null) {
            return null;
        }

        LikeId id = new LikeId()
                .setUserId(user.getId())
                .setThoughtId(thought.getId());

        return new LikeEntity()
                .setId(id)
                .setUser(user)
                .setThought(thought)
                .setCreatedAt(domain.getCreated_at());

    }

}
