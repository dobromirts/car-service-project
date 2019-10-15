package com.tsvetkov.autoservice.unit.validation.user;

import com.tsvetkov.autoservice.domain.entities.User;
import com.tsvetkov.autoservice.domain.models.binding.UserEditProfileBindingModel;
import com.tsvetkov.autoservice.repository.UserRepository;
import com.tsvetkov.autoservice.validation.user.UserEditProfileValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserEditProfileValidatorTests {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserEditProfileValidator userEditProfileValidator;

    UserEditProfileBindingModel userEditProfileBindingModel;

    User user;

    @Before
    public void init(){
        userEditProfileValidator=new UserEditProfileValidator(userRepository,bCryptPasswordEncoder);

        userEditProfileBindingModel=new UserEditProfileBindingModel();
        userEditProfileBindingModel.setUsername("test");
        userEditProfileBindingModel.setOldPassword("12345");
        userEditProfileBindingModel.setPassword("12345");
        userEditProfileBindingModel.setConfirmPassword("12345");
        userEditProfileBindingModel.setEmail("test@test.com");

        user =new User();
        user.setUsername("test");
        user.setPassword("12345");
        user.setEmail("test@test.com");
        user.setAuthorities(new HashSet<>());
    }

    @Test
    public void validate_WhenValidUser_NoExceptions(){
        when(userRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        when(bCryptPasswordEncoder.matches(any(),any())).thenReturn(true);

        Errors errors=new BeanPropertyBindingResult(userEditProfileBindingModel,"validUser");
        userEditProfileValidator.validate(userEditProfileBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenOldPasswordDoesNotMatchesUserPassword_ExpectedExceptions(){
        user.setPassword("123");
        when(userRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));

        Errors errors=new BeanPropertyBindingResult(userEditProfileBindingModel,"validUser");
        userEditProfileValidator.validate(userEditProfileBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenPasswordDoesNotMatchesConfirmPassword_ExpectedExceptions(){
        userEditProfileBindingModel.setConfirmPassword("123");
        when(userRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        when(bCryptPasswordEncoder.matches(any(),any())).thenReturn(true);

        Errors errors=new BeanPropertyBindingResult(userEditProfileBindingModel,"validUser");
        userEditProfileValidator.validate(userEditProfileBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }


}
