package com.mypensamiento.mypensamiento.application.usecase.like;

import com.mypensamiento.mypensamiento.application.dto.request.LikeRequest;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.Like;
import com.mypensamiento.mypensamiento.domain.repository.LikeRepository;
import com.mypensamiento.mypensamiento.domain.repository.ThoughtRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;

import java.time.LocalDateTime;

public class LikeUseCase {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ThoughtRepository thoughtRepository;

    public LikeUseCase(LikeRepository likeRepository, UserRepository userRepository, ThoughtRepository thoughtRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.thoughtRepository = thoughtRepository;
    }

    public void execute(LikeRequest request, Long id_thougth){

        if(!userRepository.existsById(id_thougth)){
            throw new NotFoundException("User not found");
        }
        if(!thoughtRepository.existsById(request.id_user())){
            throw new NotFoundException("Thought not found");
        }

        LocalDateTime localDateTime = LocalDateTime.now();

        Like like = new Like()
                .setUser_id(id_thougth)
                .setThought_id(request.id_user())
                .setCreated_at(localDateTime);

        likeRepository.save(like);

    }
}
