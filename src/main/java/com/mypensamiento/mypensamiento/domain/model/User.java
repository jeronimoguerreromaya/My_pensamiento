package com.mypensamiento.mypensamiento.domain.model;

import com.mypensamiento.mypensamiento.domain.model.categorie.Role;

import java.time.LocalDateTime;


public class User {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String full_name;
    private String bio;
    private String profile_picture;
    private LocalDateTime created_at;
    private Role role;
    private Boolean status;

    public User(String nickname, String email, String password, String full_name, String bio, String profile_picture, Role role) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.bio = bio;
        this.profile_picture = profile_picture;
        this.role = role;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFull_name() {
        return full_name;
    }

    public User setFull_name(String full_name) {
        this.full_name = full_name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public User setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public User setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
        return this;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public User setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public User setStatus(Boolean status) {
        this.status = status;
        return this;
    }
}
