package com.tsvetkov.autoservice.validation.user;

import com.tsvetkov.autoservice.domain.entities.User;
import com.tsvetkov.autoservice.domain.models.binding.UserEditProfileBindingModel;
import com.tsvetkov.autoservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserEditProfileValidator implements Validator {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserEditProfileValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditProfileBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEditProfileBindingModel userEditBindingModel = (UserEditProfileBindingModel) o;

        User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElseThrow(()->new IllegalArgumentException("Invalid User"));

        if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue("oldPassword","Wrong passwords!","Wrong password!");
        }

        if (userEditBindingModel.getPassword() != null && !userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            errors.rejectValue("password","Passwords do not match!","Passwords do not match!");
        }

        if (!user.getEmail().equals(userEditBindingModel.getEmail()) && this.userRepository.findByEmail(userEditBindingModel.getEmail()).isPresent()) {
            errors.rejectValue("email","Email already exists!","Email already exists!");
        }
    }
}
