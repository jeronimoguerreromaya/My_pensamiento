package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.user.SaveUserUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;
import com.mypensamiento.mypensamiento.infrastructure.adapters.BCryptPasswordEncoderJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeansConfig {

    @Bean
    public SaveUserUseCase saveUserUseCase(
            UserRepository userRepository,
            PasswordEncoderRepository passwordEncoderRepository
    ){
        return new SaveUserUseCase(userRepository, passwordEncoderRepository);
    }

    @Bean
    public UpdateUserPatchUseCase updateUserPatchUseCase(UserRepository userRepository, PasswordEncoderRepository passwordEncoderRepository){
        return new UpdateUserPatchUseCase(userRepository, passwordEncoderRepository);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase(UserRepository userRepository, PasswordEncoderRepository passwordEncoderRepository){
        return new UpdatePasswordUseCase(userRepository, passwordEncoderRepository);
    }
}
