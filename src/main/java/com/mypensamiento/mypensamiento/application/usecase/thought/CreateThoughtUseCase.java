package com.mypensamiento.mypensamiento.application.usecase.thought;

import com.mypensamiento.mypensamiento.application.dto.request.CreateThoughtRequest;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.Thought;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;

import java.time.LocalDateTime;

public class CreateThoughtUseCase {

    ThoughtRepository thoughtRepository;
    UserRepository userRepository;

    public CreateThoughtUseCase(ThoughtRepository thoughtRepository, UserRepository userRepository) {
        this.thoughtRepository = thoughtRepository;
        this.userRepository = userRepository;
    }
    public void execute(CreateThoughtRequest request, Long id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found");
        }
        if(request.content().isEmpty()){
            throw new FieldValidationException("Thought content is required");
        }

        if(request.content().trim().isEmpty()) {
            throw new FieldValidationException("Thought content is required");
        }
            LocalDateTime localT = LocalDateTime.now();
        Thought thought = new Thought()
                .setUsers_id(id)
                .setThought_text(request.content())
                .setCreated_at(localT);
        thoughtRepository.save(thought);

    }

}
