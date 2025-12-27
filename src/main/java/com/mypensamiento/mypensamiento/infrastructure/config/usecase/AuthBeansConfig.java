package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.usecase.Auth.*;
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
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort,
            HashPort hashPort

    ){
        return new RegisterUseCase(userPort,passwordEncoderPort,refreshTokenPort,tokenPort,hashPort);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort,
            AuthenticationPort authenticationPort,
            HashPort hashPort

    ){
        return new LoginUseCase(
                userPort,
                passwordEncoderPort,
                refreshTokenPort,
                tokenPort,
                authenticationPort,
                hashPort

        );
    }
    @Bean
    public RefreshUseCase refreshUseCase(
            RefreshTokenPort refreshTokenPort,
            UserPort userPort,
            TokenPort tokenPort,
            HashPort hashPort
    ){
        return new RefreshUseCase(refreshTokenPort,userPort,tokenPort,hashPort);
    }

    @Bean
    public LogoutUseCase logoutUseCase(
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort
    ){
        return new LogoutUseCase(refreshTokenPort, tokenPort);
    }

    @Bean
    public LogoutAllUseCase logoutAllUseCase(
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort
    ){
        return new LogoutAllUseCase(refreshTokenPort, tokenPort);
    }


    @Bean
    public TokenPort tokenPort(JwtService jwtService){
        return new JwtAdapte(jwtService);
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort() {
        return new BCryptPasswordEncoderAdapter();
    }

    @Bean
    public AuthenticationPort authenticationPort(AuthenticationManager authenticationManager){
        return new com.mypensamiento.mypensamiento.infrastructure.adapters.AuthenticationAdapter(authenticationManager);
    }

}
