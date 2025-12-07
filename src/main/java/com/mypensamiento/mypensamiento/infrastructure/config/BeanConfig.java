package com.mypensamiento.mypensamiento.infrastructure.config;

import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.infrastructure.adapters.BCryptPasswordEncoderJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoderRepository passwordEncoderPort() {
        return new BCryptPasswordEncoderJpaAdapter();
    }
}