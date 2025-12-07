package com.mypensamiento.mypensamiento.application.usecase.user;

import com.mypensamiento.mypensamiento.application.dto.request.UpdatePasswordRequest;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.UserNotFoundException;
import com.mypensamiento.mypensamiento.domain.model.User;
import com.mypensamiento.mypensamiento.domain.repository.PasswordEncoderRepository;
import com.mypensamiento.mypensamiento.domain.repository.UserRepository;

public class UpdatePasswordUseCase {

    UserRepository userRepository;
    PasswordEncoderRepository passwordEncoderRepository;

    public UpdatePasswordUseCase(UserRepository userRepository, PasswordEncoderRepository passwordEncoderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoderRepository = passwordEncoderRepository;
    }

    public void execute(UpdatePasswordRequest request , Long id){

        User user = userRepository.getById(id);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (request.password() == null || request.password().isEmpty()
                || request.newPassword() == null || request.newPassword().isEmpty()
                || request.confirmPassword() == null || request.confirmPassword().isEmpty()) {
            throw new FieldValidationException("All password fields are required");
        }

        if (!passwordEncoderRepository.matches(request.password(), user.getPassword())) {
            throw new FieldValidationException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new FieldValidationException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoderRepository.encode(request.newPassword()));
        userRepository.save(user);
    }

}
