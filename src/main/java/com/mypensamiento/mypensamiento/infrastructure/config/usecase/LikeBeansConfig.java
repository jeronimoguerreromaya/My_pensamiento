package com.mypensamiento.mypensamiento.infrastructure.config.usecase;


import com.mypensamiento.mypensamiento.application.usecase.like.LikeUseCase;
import com.mypensamiento.mypensamiento.domain.ports.LikePort;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LikeBeansConfig {

    @Bean
    public LikeUseCase LikeUseCase(
            LikePort LikeRepository,
            UserPort userRepository,
            ThoughtPort thoughtRepository
    ){
        return new LikeUseCase(LikeRepository,userRepository,thoughtRepository);
    }

}
