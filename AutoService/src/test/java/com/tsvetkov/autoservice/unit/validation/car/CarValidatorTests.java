package com.tsvetkov.autoservice.unit.validation.car;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.models.binding.CarBindingModel;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.validation.car.CarValidator;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CarValidatorTests {

    @Mock
    CarRepository categoryRepository;

    MultipartFile firstFile;

    CarValidator carValidator;

    @Before
    public void init(){
        carValidator=new CarValidator(categoryRepository);
        firstFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());
    }

    @Test
    public void validate_WithCorrectCarBrand_ExpectedNoErrors(){
        CarBindingModel carBindingModel=new CarBindingModel();
        carBindingModel.setBrand("test");
        carBindingModel.setImage(firstFile);

        Errors errors = new BeanPropertyBindingResult(carBindingModel, "validCar");
        carValidator.validate(carBindingModel, errors);

        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WithCarBrandLowerThan2Symbols_ExpectedErrors(){
        CarBindingModel carBindingModel=new CarBindingModel();
        carBindingModel.setBrand("t");
        carBindingModel.setImage(firstFile);

        Errors errors = new BeanPropertyBindingResult(carBindingModel, "invalidCar");
        carValidator.validate(carBindingModel, errors);

        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WithCarBrandBiggerThan10Symbols_ExpectedErrors(){
        CarBindingModel carBindingModel=new CarBindingModel();
        carBindingModel.setBrand("11111111111111111111111");
        carBindingModel.setImage(firstFile);


        Errors errors = new BeanPropertyBindingResult(carBindingModel, "invalidCar");
        carValidator.validate(carBindingModel, errors);

        Assert.assertTrue(errors.hasErrors());

    }

    @Test
    public void validate_WithExistingCar_ExpectedErrors(){
        CarBindingModel carBindingModel=new CarBindingModel();
        carBindingModel.setBrand("test");
        carBindingModel.setImage(firstFile);

        Car car=new Car();
        car.setBrand("test");
        car.setImageUrl("test");

        when(categoryRepository.findByBrandAndDeletedFalse(any())).thenReturn(java.util.Optional.ofNullable(car));


        Errors errors = new BeanPropertyBindingResult(carBindingModel, "invalidCar");
        carValidator.validate(carBindingModel, errors);

        Assert.assertTrue(errors.hasErrors());
    }


}
