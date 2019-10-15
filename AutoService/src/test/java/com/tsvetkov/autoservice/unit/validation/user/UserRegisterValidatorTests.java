package com.tsvetkov.autoservice.unit.validation.user;

import com.tsvetkov.autoservice.domain.entities.User;
import com.tsvetkov.autoservice.domain.models.binding.UserRegisterBindingModel;
import com.tsvetkov.autoservice.repository.UserRepository;
import com.tsvetkov.autoservice.validation.user.UserRegisterValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRegisterValidatorTests {

    @Mock
    UserRepository userRepository;

    UserRegisterValidator userRegisterValidator;

    UserRegisterBindingModel userRegisterBindingModel;

    User user;

    @Before
    public void init(){
        userRegisterValidator=new UserRegisterValidator(userRepository);
        userRegisterBindingModel=new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("test");
        userRegisterBindingModel.setPassword("test");
        userRegisterBindingModel.setConfirmPassword("test");
        userRegisterBindingModel.setEmail("test@test.com");

        user =new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
        user.setAuthorities(new HashSet<>());
    }

    @Test
    public void validate_WhenValidModel_NoExceptions(){
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"validUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenPasswordIsNull_ExpectedException(){
        userRegisterBindingModel.setPassword(null);

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenUsernameIsLowerThan2Symbols_ExpectedException(){
        userRegisterBindingModel.setUsername("a");

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenUsernameIsBiggerThan20Symbols_ExpectedException(){
        userRegisterBindingModel.setUsername("aaaaaaaaaaaaaaaaaaaaa");

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenPasswordsDoesNotMatch_ExpectedException(){
        userRegisterBindingModel.setPassword("323");

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenUsernameAlreadyExists_ExpectedException(){
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenEmailAlreadyExists_ExpectedException(){
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));

        Errors errors=new BeanPropertyBindingResult(userRegisterBindingModel,"invalidUser");
        userRegisterValidator.validate(userRegisterBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }
}
