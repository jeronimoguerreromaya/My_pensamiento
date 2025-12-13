package com.mypensamiento.mypensamiento.infrastructure.jpa.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class LikeEntity {

    @EmbeddedId
    private LikeId id;

    @MapsId("userId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @MapsId("thoughtId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "thought_id", nullable = false)
    private ThoughtEntity thought;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public LikeEntity() {
    }

    public LikeId getId() {
        return id;
    }

    public LikeEntity setId(LikeId id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public LikeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ThoughtEntity getThought() {
        return thought;
    }

    public LikeEntity setThought(ThoughtEntity thought) {
        this.thought = thought;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LikeEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}