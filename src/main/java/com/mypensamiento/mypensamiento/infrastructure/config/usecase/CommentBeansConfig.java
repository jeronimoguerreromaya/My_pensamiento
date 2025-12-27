package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.comment.CommentThoughtUseCase;
import com.mypensamiento.mypensamiento.domain.ports.CommentPort;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentBeansConfig {

    @Bean
    public CommentThoughtUseCase commentThoughtUseCase(
            CommentPort commentRepository,
            UserPort userRepository,
            ThoughtPort thoughtRepository
    ){
        return new CommentThoughtUseCase(commentRepository,userRepository,thoughtRepository);
    }

}
