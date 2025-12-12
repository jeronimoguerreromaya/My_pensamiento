package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.model.Comment;
import com.mypensamiento.mypensamiento.domain.repository.CommentRepository;
import com.mypensamiento.mypensamiento.infrastructure.adapters.mappers.CommentMapper;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.CommentEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.CommentJpaRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.ThoughtJpaRepository;
import com.mypensamiento.mypensamiento.infrastructure.jpa.persistence.UserJpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class CommentRepositoryJpaAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ThoughtJpaRepository thoughtJpaRepository;


    public CommentRepositoryJpaAdapter(CommentJpaRepository commentJpaRepository, UserJpaRepository userJpaRepository, ThoughtJpaRepository thoughtJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.thoughtJpaRepository = thoughtJpaRepository;
    }

    @Override
    public void save(Comment comment) {

        UserEntity userEntity = userJpaRepository.findById(comment.getUsers_id()).orElse(null);
        ThoughtEntity thoughtEntity = thoughtJpaRepository.findById(comment.getThought_id()).orElse(null);

        CommentEntity entity = new CommentMapper().toEntity(comment, userEntity, thoughtEntity );
        commentJpaRepository.save(entity);
    }

}