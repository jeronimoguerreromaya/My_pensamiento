package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.comment.CommentThoughtUseCase;
import com.mypensamiento.mypensamiento.domain.repository.CommentRepository;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentBeansConfig {

    @Bean
    public CommentThoughtUseCase commentThoughtUseCase(
            CommentRepository commentRepository,
            UserRepository userRepository,
            ThoughtRepository thoughtRepository
    ){
        return new CommentThoughtUseCase(commentRepository,userRepository,thoughtRepository);
    }

}
