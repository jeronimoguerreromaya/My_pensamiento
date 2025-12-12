package com.mypensamiento.mypensamiento.infrastructure.adapters.mappers;

import com.mypensamiento.mypensamiento.domain.model.Comment;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.CommentEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.ThoughtEntity;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;

import javax.swing.text.html.parser.Entity;

public class CommentMapper {

    public Comment toDomain (CommentEntity entity){
        return new Comment();
    }

    public CommentEntity toEntity(Comment domain, UserEntity user , ThoughtEntity thought){
        return new CommentEntity()
                .setCommentText(domain.getComment_text())
                .setCreatedAt(domain.getCreated_at())
                .setUser(user)
                .setThought(thought);
    }
}
