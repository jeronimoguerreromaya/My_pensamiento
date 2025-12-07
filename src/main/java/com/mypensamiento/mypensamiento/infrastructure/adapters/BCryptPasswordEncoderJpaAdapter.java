package com.mypensamiento.mypensamiento.infrastructure.adapters;

import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderJpaAdapter implements PasswordEncoderRepository {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
