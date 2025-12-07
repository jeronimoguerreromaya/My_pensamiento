package com.mypensamiento.mypensamiento.infrastructure.controllers;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.dto.request.UserRequest;
import com.mypensamiento.mypensamiento.application.usecase.user.SaveUserUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdatePasswordUseCase;
import com.mypensamiento.mypensamiento.application.usecase.user.UpdateUserPatchUseCase;
import com.mypensamiento.mypensamiento.infrastructure.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SaveUserUseCase saveUserUseCase;

    @Autowired
    private UpdateUserPatchUseCase updateUserPatchUseCase;

    @Autowired
    private UpdatePasswordUseCase updatePasswordUseCase;

    @PatchMapping("updatepassword/{id}")
    public ResponseEntity<ApiResponse<String>> updatePassword (
            @Validated @RequestBody UpdatePasswordRequest request,
            @PathVariable Long id
    ){

        logger.info("Starting updatePassword for user id: {}", id);
        this.updatePasswordUseCase.execute(request,id);
        logger.info("Password updated successfully for user id: {}", id);

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Contraseña actualizada correctamente"
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateUserAccount (
            @Validated @RequestBody UserRequest request,
            @PathVariable Long id
    ){

        logger.info("Starting updateUser: {} " , request.nickname());
        this.updateUserPatchUseCase.execute(request, id);
        logger.info("User updated successfully");

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario actualizado correctamente",
                request.nickname()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<ApiResponse<String>> createUserAccount (
            @Validated @RequestBody UserRequest request
            ){
        try {
            logger.info("Starting saveUser with request: {}", request);
            this.saveUserUseCase.execute(request);
            logger.info("User saved successfully");
        } catch (Exception e) {
            logger.error("Error in saveUser: ", e);
            throw e;
        }

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Usuario registrado correctamente",
                request.nickname()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
