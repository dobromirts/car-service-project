package com.tsvetkov.autoservice.unit.validation.carmodel;


import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.models.binding.CarModelAddBindingModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.validation.carmodel.CarModelAddValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarModelAddValidatorTests {

    @Mock
    CarModelRepository carModelRepository;

    MultipartFile firstFile;

    CarModelAddValidator carModelAddValidator;

    CarModelAddBindingModel carModelAddBindingModel;



    @Before
    public void init(){
        carModelAddValidator=new CarModelAddValidator(carModelRepository);
        firstFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        carModelAddBindingModel=new CarModelAddBindingModel();
        carModelAddBindingModel.setCar("car");
        carModelAddBindingModel.setHorsePower("100");
        carModelAddBindingModel.setImage(firstFile);
        carModelAddBindingModel.setImage(firstFile);
        carModelAddBindingModel.setModel("test");
    }

    @Test
    public void validate_WhenModelIsValid_NoErrorExpected(){
        Errors errors = new BeanPropertyBindingResult(carModelAddBindingModel, "validCar");

        carModelAddValidator.validate(carModelAddBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenModeLengthIsLowerThan2Symbols_ExpectedErrors(){
        carModelAddBindingModel.setModel("a");
        Errors errors = new BeanPropertyBindingResult(carModelAddBindingModel, "invalidCar");

        carModelAddValidator.validate(carModelAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenModeLengthIsBiggerThan15Symbols_ExpectedErrors(){
        carModelAddBindingModel.setModel("aaaaaaaaaaaaaaaaaaaaaaaaa");
        Errors errors = new BeanPropertyBindingResult(carModelAddBindingModel, "invalidCar");

        carModelAddValidator.validate(carModelAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenModeAlreadyExists_ExpectedErrors(){
        CarModel carModel=new CarModel();
        carModel.setModel("test");

        when(carModelRepository.findModelByModelAndDeletedFalse(any())).thenReturn(carModel);
        Errors errors = new BeanPropertyBindingResult(carModelAddBindingModel, "invalidCar");

        carModelAddValidator.validate(carModelAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenModeHorsePowersAreNegativeNumber_ExpectedErrors(){
        carModelAddBindingModel.setHorsePower("-12");
        Errors errors = new BeanPropertyBindingResult(carModelAddBindingModel, "invalidCar");

        carModelAddValidator.validate(carModelAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }
}
