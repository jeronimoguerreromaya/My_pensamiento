package com.mypensamiento.mypensamiento.infrastructure.controllers;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.dto.request.RegisterUserRequest;
import com.mypensamiento.mypensamiento.application.dto.request.UpdateUserProfileRequest;
import com.mypensamiento.mypensamiento.application.dto.response.AuthResponse;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.infrastructure.dto.ApiResponse;
import com.mypensamiento.mypensamiento.infrastructure.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UpdateUserPatchUseCase updateUserPatchUseCase;

    @Autowired
    private UpdatePasswordUseCase updatePasswordUseCase;

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("update/password")
    public ResponseEntity<AuthResponse> updatePassword (
            @Validated @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        Long id = userPrincipal.getUserId();

        logger.info("Starting updatePassword for user id: {}", id);
        AuthResponse authResponse = this.updatePasswordUseCase.execute(request,id);
        logger.info("Password updated successfully for user id: {}", id);

        return ResponseEntity.ok(authResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/me")
    public ResponseEntity<ApiResponse<String>> updateUserAccount (
            @Validated @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        Long userId = userPrincipal.getUserId();

        logger.info("User authenticated with ID: {}", userId);

        this.updateUserPatchUseCase.execute(request, userId);
        logger.info("User updated successfully");

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario actualizado correctamente",
                request.nickname()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
