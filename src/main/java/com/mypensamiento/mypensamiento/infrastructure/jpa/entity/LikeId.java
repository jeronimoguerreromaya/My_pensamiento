package com.mypensamiento.mypensamiento.infrastructure.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class LikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "thought_id")
    private Long thoughtId;

    public LikeId() {
    }

    public LikeId(Long userId, Long thoughtId) {
        this.userId = userId;
        this.thoughtId = thoughtId;
    }

    public Long getUserId() {
        return userId;
    }

    public LikeId setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getThoughtId() {
        return thoughtId;
    }

    public LikeId setThoughtId(Long thoughtId) {
        this.thoughtId = thoughtId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LikeId likeId = (LikeId) o;
        return Objects.equals(userId, likeId.userId) &&
               Objects.equals(thoughtId, likeId.thoughtId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, thoughtId);
    }
}