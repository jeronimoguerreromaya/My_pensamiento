package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UserRequest;
import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.model.categorie.Role;
import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;

import java.time.LocalDateTime;

public class SaveUserUseCase {

    UserRepository repository;
    PasswordEncoderRepository passwordEncoderRepository;

    public SaveUserUseCase(UserRepository repository, PasswordEncoderRepository passwordEncoderRepository) {
        this.repository = repository;
        this.passwordEncoderRepository = passwordEncoderRepository;
    }

    public void execute(UserRequest request) {
        //Add validation to email and nickname is no exits yet

        if(request.nickname() == null || request.nickname().isEmpty() ||
           request.email() == null || request.email().isEmpty() ||
           request.password() == null || request.password().isEmpty()){
            throw new FieldValidationException("Some Fields are required");
        }
        if(repository.existsByNickname(request.nickname())){
            throw new NickNameAlreadyExistsException("Nickname "+ request.nickname() +" already exists");
        }
        if(repository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email "+ request.email() +" already exists");
        }

        LocalDateTime localT = LocalDateTime.now();

        User user = new User()
                .setNickname(request.nickname())
                .setEmail(request.email())
                .setPassword(passwordEncoderRepository.encode(request.password()))
                .setStatus(Boolean.TRUE)
                .setRole(Role.USER)
                .setCreated_at(localT);

        if(request.full_name() != null && !request.full_name().isEmpty()){
            user.setFull_name(request.full_name());
        }
        if(request.bio() != null && !request.bio().isEmpty()){
            user.setBio(request.bio());
        }
        if(request.profile_picture() != null && !request.profile_picture().isEmpty() && !"null".equals(request.profile_picture())){
            user.setProfile_picture(request.profile_picture());
        }

        repository.save(user);
     }
}
