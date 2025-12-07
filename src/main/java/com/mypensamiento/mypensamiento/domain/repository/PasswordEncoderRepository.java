package com.mypensamiento.mypensamiento.domain.repository;

public interface PasswordEncoderRepository {

    String encode(String rawPassword);
   boolean matches(String rawPassword, String encodedPassword);

}
