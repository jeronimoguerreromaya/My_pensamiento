package com.mypensamiento.mypensamiento.application.usecase.like;

import com.mypensamiento.mypensamiento.application.dto.request.LikeRequest;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.Like;
import com.mypensamiento.mypensamiento.domain.ports.LikePort;
import com.mypensamiento.mypensamiento.domain.ports.ThoughtPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;

import java.time.LocalDateTime;

public class LikeUseCase {
    private final LikePort likeRepository;
    private final UserPort userRepository;
    private final ThoughtPort thoughtRepository;

    public LikeUseCase(LikePort likeRepository, UserPort userRepository, ThoughtPort thoughtRepository) {
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
