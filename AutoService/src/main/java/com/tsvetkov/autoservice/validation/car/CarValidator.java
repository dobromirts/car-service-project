package com.tsvetkov.autoservice.validation.car;

import com.tsvetkov.autoservice.domain.models.binding.CarBindingModel;
import com.tsvetkov.autoservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CarValidator implements Validator {
    private final CarRepository carRepository;

    @Autowired
    public CarValidator(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CarBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CarBindingModel carBindingModel = (CarBindingModel) o;

        if (carBindingModel.getBrand().length()<2 || carBindingModel.getBrand().length()>10){
            errors.rejectValue("brand","Brand should be between 2 and 10 symbols!","Brand should be between 2 and 10 symbols!");
        }

        if (this.carRepository.findByBrandAndDeletedFalse(carBindingModel.getBrand()).isPresent()){
            errors.rejectValue("brand","Brand already exists!","Brand already exists!");
        }
    }
}
