package com.tsvetkov.autoservice.validation.user;

import com.tsvetkov.autoservice.domain.models.binding.UserRegisterBindingModel;
import com.tsvetkov.autoservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserRegisterValidator implements Validator {
    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegisterBindingModel userRegisterBindingModel= (UserRegisterBindingModel) o;
        if (userRegisterBindingModel.getPassword()==null || userRegisterBindingModel.getUsername().length() < 2 || userRegisterBindingModel.getUsername().length() > 20) {
            errors.rejectValue("username","Username should be between 2 and 20 symbols","Username should be between 2 and 20 symbols!");
        }else if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            errors.rejectValue("password","Invalid Password","Passwords do not match!");
        }else  if (userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent()){
            errors.rejectValue("username","Username already exists","Username already exists!");
        }else if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
            errors.rejectValue("email","Email already exists","Email already exists!");
        }

    }
}
