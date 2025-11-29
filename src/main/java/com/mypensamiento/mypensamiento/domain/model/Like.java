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

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getThought_id() {
        return thought_id;
    }

    public void setThought_id(Long thought_id) {
        this.thought_id = thought_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
