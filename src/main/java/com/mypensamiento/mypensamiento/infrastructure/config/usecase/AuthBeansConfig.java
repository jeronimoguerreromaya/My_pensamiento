package com.mypensamiento.mypensamiento.infrastructure.config.usecase;

import com.mypensamiento.mypensamiento.application.service.ServiceToken;
import com.mypensamiento.mypensamiento.application.usecase.Auth.*;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.PasswordChangeUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.SendCodeUseCase;
import com.mypensamiento.mypensamiento.application.usecase.Auth.resetPassword.VerifyCodeUseCase;
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
    public PasswordChangeUseCase PasswordReset(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenPort tokenPort,
            HashPort hashPort,
            RefreshTokenPort refreshTokenPort,
            ServiceToken serviceToken
    ){
        return new PasswordChangeUseCase(
                userPort,
                passwordEncoderPort,
                tokenPort,
                hashPort,
                refreshTokenPort,
                serviceToken
        );
    }

    @Bean
    public VerifyCodeUseCase verifyCodeUseCase(
            PasswordResetCodePort passwordResetCodePort,
            HashPort hashPort,
            UserPort userPort,
            TokenPort tokenPort
    ){
        return new VerifyCodeUseCase(passwordResetCodePort, hashPort, userPort, tokenPort);
    }

    @Bean
    public SendCodeUseCase requestPasswordResetUseCase(
            EmailPort emailPort,
            PasswordResetCodePort passwordResetCodePort,
            HashPort hashPort,
            UserPort userPort
    ){
        return new SendCodeUseCase(emailPort,passwordResetCodePort,hashPort,userPort);
    }

    @Bean
    public RegisterUseCase registerUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            HashPort hashPort,
            ServiceToken serviceToken


    ){
        return new RegisterUseCase(userPort,passwordEncoderPort,refreshTokenPort,hashPort,serviceToken);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserPort userPort,
            PasswordEncoderPort passwordEncoderPort,
            RefreshTokenPort refreshTokenPort,
            TokenPort tokenPort,
            AuthenticationPort authenticationPort,
            HashPort hashPort,
            ServiceToken serviceToken

    ){
        return new LoginUseCase(
                userPort,
                passwordEncoderPort,
                refreshTokenPort,
                tokenPort,
                authenticationPort,
                hashPort,
                serviceToken

        );
    }
    @Bean
    public RefreshUseCase refreshUseCase(
            RefreshTokenPort refreshTokenPort,
            UserPort userPort,
            TokenPort tokenPort,
            HashPort hashPort,
            ServiceToken serviceToken
    ){
        return new RefreshUseCase(refreshTokenPort,userPort,tokenPort,hashPort,serviceToken);
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
    public ServiceToken serviceToken(TokenPort tokenPort){
        return new ServiceToken(tokenPort);
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
