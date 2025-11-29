package com.mypensamiento.mypensamiento.domain.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long thought_id;
    private Long users_id;
    private String comment_text;
    private LocalDateTime created_at;

    public Comment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThought_id() {
        return thought_id;
    }

    public void setThought_id(Long thought_id) {
        this.thought_id = thought_id;
    }

    public Long getUsers_id() {
        return users_id;
    }

    public void setUsers_id(Long users_id) {
        this.users_id = users_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
