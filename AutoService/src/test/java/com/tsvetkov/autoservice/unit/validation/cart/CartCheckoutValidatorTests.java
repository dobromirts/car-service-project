package com.tsvetkov.autoservice.unit.validation.cart;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.validation.cart.CartCheckoutValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@RunWith(MockitoJUnitRunner.class)
public class CartCheckoutValidatorTests {

    CartCheckoutValidator cartCheckoutValidator;

    CartPersonBindingModel cartPersonBindingModel;

    @Before
    public void init(){
        cartCheckoutValidator=new CartCheckoutValidator();

        cartPersonBindingModel=new CartPersonBindingModel();
        cartPersonBindingModel.setPersonName("test");
        cartPersonBindingModel.setPhoneNumber("1234556789");
        cartPersonBindingModel.setDescription("");
        cartPersonBindingModel.setDate("12/12/2019");
    }

    @Test
    public void validate_WhenModelIsValid_ExpectedErrors(){
        Errors errors=new BeanPropertyBindingResult(cartPersonBindingModel,"validModel");
        cartCheckoutValidator.validate(cartPersonBindingModel,errors);

        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenPersonNameIsEmpty_ExpectedErrors(){
        cartPersonBindingModel.setPersonName("");
        Errors errors=new BeanPropertyBindingResult(cartPersonBindingModel,"invalidModel");
        cartCheckoutValidator.validate(cartPersonBindingModel,errors);

        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenPhoneNumberIsEmpty_ExpectedErrors(){
        cartPersonBindingModel.setPhoneNumber("");
        Errors errors=new BeanPropertyBindingResult(cartPersonBindingModel,"invalidModel");
        cartCheckoutValidator.validate(cartPersonBindingModel,errors);

        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenDateIsEmpty_ExpectedErrors(){
        cartPersonBindingModel.setPhoneNumber("");
        Errors errors=new BeanPropertyBindingResult(cartPersonBindingModel,"invalidModel");
        cartCheckoutValidator.validate(cartPersonBindingModel,errors);

        Assert.assertTrue(errors.hasErrors());
    }
}
