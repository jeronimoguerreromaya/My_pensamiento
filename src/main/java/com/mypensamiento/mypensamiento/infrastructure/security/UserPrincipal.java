package com.mypensamiento.mypensamiento.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mypensamiento.mypensamiento.infrastructure.jpa.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private  Long userId;
    private  String userEmail;
    private String userPassword;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long userId, String userEmail, String userPassword, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.authorities = authorities;
    }

    public static  UserPrincipal build (UserEntity userEntity){
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())
        );
        return  new UserPrincipal(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }

    public Long getUserId(){
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

}
