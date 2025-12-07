package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.thought.CreateThoughtUseCase;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThoughtBeansConfig {

    @Bean
    public CreateThoughtUseCase createThoughtUseCase(
            ThoughtRepository thoughtRepository,
            UserRepository userRepository

    ){
        return new CreateThoughtUseCase(thoughtRepository,userRepository);
    }

}
