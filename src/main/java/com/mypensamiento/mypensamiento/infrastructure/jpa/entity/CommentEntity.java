package com.mypensamiento.mypensamiento.infrastructure.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "thought_id", nullable = false)
    private ThoughtEntity thought;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "comment_text", nullable = false, length = 1000)
    private String commentText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CommentEntity() {
    }

    public Long getId() {
        return id;
    }

    public CommentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public ThoughtEntity getThought() {
        return thought;
    }

    public CommentEntity setThought(ThoughtEntity thought) {
        this.thought = thought;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public CommentEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public String getCommentText() {
        return commentText;
    }

    public CommentEntity setCommentText(String commentText) {
        this.commentText = commentText;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CommentEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}