package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UserRequest;
import com.mypensamiento.mypensamiento.application.exception.NotFoundException;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.ports.PasswordEncoderPort;
import com.mypensamiento.mypensamiento.domain.ports.UserPort;

public class UpdateUserPatchUseCase {

    UserPort userRepository;
    PasswordEncoderPort passwordEncoderRepository;

    public UpdateUserPatchUseCase(UserPort userRepository, PasswordEncoderPort passwordEncoderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoderRepository = passwordEncoderRepository;
    }

    public void execute(UserRequest request, Long id) {

        User uptdate = userRepository.getById(id);

        if (uptdate == null) {
            throw new NotFoundException("User not found");
        }

        if (request.nickname() != null && !request.nickname().isEmpty()) {
            if (!userRepository.existsByNickname(request.nickname())) {
                uptdate.setNickname(request.nickname());
            }
        }
        if (request.email() != null && !request.email().isEmpty()) {
            if (!userRepository.existsByEmail(request.email())) {
                uptdate.setEmail(request.email());
            }
        }

        if(request.full_name() != null && !request.full_name().isEmpty()){
            uptdate.setFull_name(request.full_name());
        }

        if(request.bio() != null && !request.bio().isEmpty()){
            uptdate.setBio(request.bio());
        }
        if(request.profile_picture() != null && !request.profile_picture().isEmpty() && !"null".equals(request.profile_picture())){
            uptdate.setProfile_picture(request.profile_picture());
        }

        userRepository.save(uptdate);

    }
}

