package com.mypensamiento.mypensamiento.infrastructure.controllers;

import com.mypensamiento.mypensamiento.application.dto.request.CommentThoughtRequest;
import com.mypensamiento.mypensamiento.application.usecase.comment.CommentThoughtUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mipensamiento/comment")
public class CommnetController {

    private static final Logger logger = LoggerFactory.getLogger(CommnetController.class);

    @Autowired
    private CommentThoughtUseCase commentThoughtUseCase;

    @PostMapping("/{idUser}")
    public void commentThought(
            @Validated @RequestBody CommentThoughtRequest request ,
            @PathVariable Long idUser
            ){
       commentThoughtUseCase.execute(request,idUser);
    }

}
