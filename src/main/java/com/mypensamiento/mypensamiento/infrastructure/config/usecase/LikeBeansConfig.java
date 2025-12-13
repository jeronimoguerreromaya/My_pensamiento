package com.mypensamiento.mypensamiento.infrastructure.config.usecase;


import com.mypensamiento.mypensamiento.application.usecase.like.LikeUseCase;
import com.mypensamiento.mypensamiento.domain.repository.CommentRepository;
import com.mypensamiento.mypensamiento.domain.repository.LikeRepository;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LikeBeansConfig {

    @Bean
    public LikeUseCase LikeUseCase(
            LikeRepository LikeRepository,
            UserRepository userRepository,
            ThoughtRepository thoughtRepository
    ){
        return new LikeUseCase(LikeRepository,userRepository,thoughtRepository);
    }

}
