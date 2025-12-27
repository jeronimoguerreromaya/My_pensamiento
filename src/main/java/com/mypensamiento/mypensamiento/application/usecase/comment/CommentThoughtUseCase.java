package com.mypensamiento.mypensamiento.application.usecase.comment;

import com.mypensamiento.mypensamiento.application.dto.request.CommentThoughtRequest;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.Comment;
import com.mypensamiento.mypensamiento.domain.ports.CommentPort;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;

import java.time.LocalDateTime;

public class CommentThoughtUseCase {
    private CommentPort commentRepository;
    private UserPort userRepository;
    private ThoughtPort thoughtRepository;

    public CommentThoughtUseCase(CommentPort commentRepository, UserPort userRepository, ThoughtPort thoughtRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.thoughtRepository = thoughtRepository;
    }

    public void execute(CommentThoughtRequest request, Long id_user){

        if(request.content().isEmpty()){
            throw new FieldValidationException("Thought content is required");
        }

        if(request.content().trim().isEmpty()) {
            throw new FieldValidationException("Thought content is required");
        }

        if(!userRepository.existsById(id_user)){
            throw new NotFoundException("User not found");
        }

        if(!thoughtRepository.existsById(request.id_thought())){
            throw new RuntimeException("Thought not found");
        }

        LocalDateTime localDateTime = LocalDateTime.now();

        Comment comment = new Comment();
            comment.setComment_text(request.content());
            comment.setThought_id(request.id_thought());
            comment.setUsers_id(id_user);
            comment.setCreated_at(localDateTime);

        commentRepository.save(comment);

    }

}
