package com.mypensamiento.mypensamiento.infrastructure.controllers;

import com.mypensamiento.mypensamiento.application.dto.request.CreateThoughtRequest;
import com.mypensamiento.mypensamiento.application.usecase.thought.CreateThoughtUseCase;
import com.mypensamiento.mypensamiento.infrastructure.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypensamiento")
public class ThoughtController {


    private static final Logger logger = LoggerFactory.getLogger(ThoughtController.class);

    @Autowired
    private CreateThoughtUseCase createThoughtUseCase;


    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> createThought(
            @Validated @RequestBody CreateThoughtRequest request,
            @PathVariable Long id
    ){
        logger.info("Starting createThought for user id: {}", id);
        this.createThoughtUseCase.execute(request,id);

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Thought created successfully"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
