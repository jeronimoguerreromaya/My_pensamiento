package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.Like;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.repository.LikeRepository;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.LikeMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.LikeEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.LikeJpaRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.ThoughtJpaRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.UserJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ThoughtJpaRepository thoughtJpaRepository;
    public LikeRepositoryJpaAdapter(LikeJpaRepository likeJpaRepository, UserJpaRepository userJpaRepository, ThoughtJpaRepository thoughtJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.thoughtJpaRepository = thoughtJpaRepository;
    }

    @Override
    public void save(Like like) {
        UserEntity userEntity = userJpaRepository.findById(like.getUser_id()).orElse(null);
        ThoughtEntity thoughtEntity = thoughtJpaRepository.findById(like.getThought_id()).orElse(null);
        LikeEntity entity = new LikeMapper().toEntity(like, userEntity, thoughtEntity);
        likeJpaRepository.save(entity);
    }


}