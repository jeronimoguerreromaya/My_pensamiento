package com.mypensamiento.mypensamiento.infrastructure.controllers;

import com.mypensamiento.mypensamiento.application.dto.request.LikeRequest;
import com.mypensamiento.mypensamiento.application.usecase.like.LikeUseCase;
import com.mypensamiento.mypensamiento.infrastructure.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thoughts/{thoughtId}/likes")
public class LikeController {

    @Autowired
    private LikeUseCase likeUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> likeThought(
            @PathVariable Long thoughtId,
            @RequestBody LikeRequest request
    ){
        this.likeUseCase.execute(request, thoughtId);


        return new ResponseEntity<>( HttpStatus.OK);
    }

}
