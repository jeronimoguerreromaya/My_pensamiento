package com.mypensamiento.mypensamiento.infrastructure.security;

import com.mypensamiento.mypensamiento.application.usecase.Auth.LogoutUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final LogoutUseCase logoutUseCase;

    public LogoutService(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        logoutUseCase.execute(authHeader);
        SecurityContextHolder.clearContext();
    }
}