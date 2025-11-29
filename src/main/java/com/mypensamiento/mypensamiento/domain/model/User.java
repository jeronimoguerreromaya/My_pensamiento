package com.mypensamiento.mypensamiento.domain.model;

import com.mypensamiento.mypensamiento.domain.model.categorie.Role;

import java.time.LocalDateTime;


public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String full_name;
    private String bio;
    private String profile_picture;
    private LocalDateTime created_at;
    private Role role;

    public User(String username, String email, String password, String full_name, String bio, String profile_picture, Role role) {
        this.username = username;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role rele) {
        this.role = rele;
    }
}
