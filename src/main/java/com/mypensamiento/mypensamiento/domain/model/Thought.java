package com.mypensamiento.mypensamiento.domain.model;

import java.time.LocalDateTime;

public class Thought {
    private Long id;
    private long users_id;
    private String thought_text;
    private LocalDateTime created_at;

    public Thought() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUsers_id() {
        return users_id;
    }

    public void setUsers_id(long users_id) {
        this.users_id = users_id;
    }

    public String getThought_text() {
        return thought_text;
    }

    public void setThought_text(String thought_text) {
        this.thought_text = thought_text;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
