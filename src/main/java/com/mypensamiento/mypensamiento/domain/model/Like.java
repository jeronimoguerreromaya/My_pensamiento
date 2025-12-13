package com.mypensamiento.mypensamiento.domain.model;

import java.time.LocalDateTime;

public class Like {

    private Long user_id;
    private Long thought_id;
    private LocalDateTime created_at;

    public Like() {}

    public Long getUser_id() {
        return user_id;
    }

    public Like setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }

    public Long getThought_id() {
        return thought_id;
    }

    public Like setThought_id(Long thought_id) {
        this.thought_id = thought_id;
        return this;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public Like setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
        return this;
    }
}
