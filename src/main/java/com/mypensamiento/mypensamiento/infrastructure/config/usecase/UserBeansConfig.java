package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.domain.ports.PasswordEncoderPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeansConfig {

    @Bean
    public UpdateUserPatchUseCase updateUserPatchUseCase(UserPort userRepository, PasswordEncoderPort passwordEncoderRepository){
        return new UpdateUserPatchUseCase(userRepository, passwordEncoderRepository);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase(UserPort userRepository, PasswordEncoderPort passwordEncoderRepository){
        return new UpdatePasswordUseCase(userRepository, passwordEncoderRepository);
    }
}
