package com.mypensamiento.mypensamiento.domain.model;

import java.time.LocalDateTime;

public class RefreshToken {

    private Long id;
    private Long user_id;
    private String token_hash;
    private LocalDateTime created_at;
    private LocalDateTime expires_at;
    private boolean revoked;
    private String replaced_by_hash;

    public RefreshToken() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token_hash;
    }

    public void setToken(String token) {
        this.token_hash = token;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(LocalDateTime expires_at) {
        this.expires_at = expires_at;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
