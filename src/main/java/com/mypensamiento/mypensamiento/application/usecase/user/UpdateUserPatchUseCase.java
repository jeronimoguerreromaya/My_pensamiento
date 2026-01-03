package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdateUserProfileRequest;
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

    public void execute(UpdateUserProfileRequest request, Long id) {

        User update = userRepository.getById(id);

        if (update == null) {
            throw new NotFoundException("User not found");
        }

        if (request.nickname() != null && !request.nickname().isEmpty()) {
            if (!update.getNickname().equals(request.nickname())) {
                update.setNickname(request.nickname());
            }
        }
        if(request.full_name() != null && !request.full_name().isEmpty()){
            update.setFull_name(request.full_name());
        }

        if(request.bio() != null && !request.bio().isEmpty()){
            update.setBio(request.bio());
        }
        if(request.profile_picture() != null && !request.profile_picture().isEmpty() && !"null".equals(request.profile_picture())){
            update.setProfile_picture(request.profile_picture());
        }

        userRepository.save(update);

    }
}

