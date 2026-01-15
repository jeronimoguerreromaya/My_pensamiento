package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.domain.ports.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeansConfig {

    @Bean
    public UpdateUserPatchUseCase updateUserPatchUseCase(UserPort userRepository, PasswordEncoderPort passwordEncoderRepository){
        return new UpdateUserPatchUseCase(userRepository, passwordEncoderRepository);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort,
            ServiceToken serviceToken
    ){
        return new UpdatePasswordUseCase(
                userRepository,
                passwordEncoderRepository,
                hashPort,
                refreshTokenPort,
                serviceToken
        );
    }
}
