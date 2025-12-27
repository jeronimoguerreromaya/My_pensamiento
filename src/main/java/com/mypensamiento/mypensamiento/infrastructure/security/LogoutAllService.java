package com.mypensamiento.mypensamiento.infrastructure.security;

import com.mypensamiento.mypensamiento.application.usecase.Auth.LogoutAllUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class LogoutAllService implements LogoutHandler {

    private final LogoutAllUseCase logoutAllUseCase;

    public LogoutAllService(LogoutAllUseCase logoutAllUseCase) {
        this.logoutAllUseCase = logoutAllUseCase;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        logoutAllUseCase.execute(authHeader);
        SecurityContextHolder.clearContext();
    }
}
