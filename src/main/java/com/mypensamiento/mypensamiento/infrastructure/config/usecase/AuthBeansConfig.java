package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.Auth.LoginUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.LogoutUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RefreshUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.RegisterUseCase;
import com.mypensamiento.mypensamiento.domain.ports.*;
import com.mypensamiento.mypensamiento.infrastructure.adapters.BCryptPasswordEncoderAdapter;
import com.mypensamiento.mypensamiento.infrastructure.adapters.JwtAdapte;
import com.mypensamiento.mypensamiento.infrastructure.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;


@Configuration
public class AuthBeansConfig {


    @Bean
    public RegisterUseCase registerUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            RefreshTokenPort refreshTokenRepository,
            TokenPort tokenProvider,
            HashPort hashProvider

    ){
        return new RegisterUseCase(userRepository,passwordEncoderRepository,refreshTokenRepository,tokenProvider,hashProvider);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserPort userRepository,
            PasswordEncoderPort passwordEncoderRepository,
            RefreshTokenPort refreshTokenRepository,
            TokenPort tokenProvider,
            AuthenticationPort authenticationRepository,
            HashPort hashProvider

    ){
        return new LoginUseCase(
                userRepository,
                passwordEncoderRepository,
                refreshTokenRepository,
                tokenProvider,
                authenticationRepository,
                hashProvider

        );
    }
    @Bean
    public RefreshUseCase refreshUseCase(
            RefreshTokenPort refreshTokenRepository,
            UserPort userRepository,
            TokenPort tokenProvider,
            HashPort hashProvider
    ){
        return new RefreshUseCase(refreshTokenRepository,userRepository,tokenProvider,hashProvider);
    }

    @Bean
    public LogoutUseCase logoutUseCase(
            RefreshTokenPort refreshTokenRepository,
            TokenPort tokenProvider
    ){
        return new LogoutUseCase(refreshTokenRepository, tokenProvider);
    }


    @Bean
    public TokenPort tokenProvider(JwtService jwtService){
        return new JwtAdapte(jwtService);
    }

    @Bean
    public PasswordEncoderPort passwordEncoderRepository() {
        return new BCryptPasswordEncoderAdapter();
    }

    @Bean
    public AuthenticationPort authenticationRepository(AuthenticationManager authenticationManager){
        return new com.mypensamiento.mypensamiento.infrastructure.adapters.AuthenticationAdapter(authenticationManager);
    }

}
