package com.tsvetkov.autoservice.validation.carmodel;

import com.tsvetkov.autoservice.domain.models.binding.CarModelAddBindingModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CarModelAddValidator implements Validator {
    private final CarModelRepository carModelRepository;

    @Autowired
    public CarModelAddValidator(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CarModelAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CarModelAddBindingModel carModelAddBindingModel= (CarModelAddBindingModel) o;

        if (carModelAddBindingModel.getModel().length()<2 || carModelAddBindingModel.getModel().length()>15){
            errors.rejectValue("model","Model name should be between 2 and 15 symbols!","Model name should be between 2 and 15 symbols!");
        }
        if (carModelRepository.findModelByModelAndDeletedFalse(carModelAddBindingModel.getModel())!=null){
            errors.rejectValue("model","Model already exists","Model already exists");
        }
        if (Integer.parseInt(carModelAddBindingModel.getHorsePower())<0){
            errors.rejectValue("horsePower","Horse power can not be negative!","Horse power can not be negative!");
        }
    }
}
