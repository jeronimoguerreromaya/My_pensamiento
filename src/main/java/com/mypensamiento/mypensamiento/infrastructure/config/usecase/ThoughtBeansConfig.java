package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.thought.CreateThoughtUseCase;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThoughtBeansConfig {

    @Bean
    public CreateThoughtUseCase createThoughtUseCase(
            ThoughtPort thoughtRepository,
            UserPort userRepository

    ){
        return new CreateThoughtUseCase(thoughtRepository,userRepository);
    }

}
