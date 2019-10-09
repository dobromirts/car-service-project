package com.tsvetkov.autoservice.validation.cart;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CartCheckoutValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CartPersonBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartPersonBindingModel cartPersonBindingModel= (CartPersonBindingModel) o;
        if (cartPersonBindingModel.getPersonName().equals("")){
            errors.rejectValue("personName","Name can not be empty!","Name can not be empty!");
        }

        if (cartPersonBindingModel.getPhoneNumber().equals("")){
            errors.rejectValue("phoneNumber","Phone number can not be empty!","Phone number can not be empty!");
        }

        if (cartPersonBindingModel.getDate().equals("")){
            errors.rejectValue("date","Select date!","Select date!");
        }
    }
}
