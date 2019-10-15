package com.tsvetkov.autoservice.integration;


import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.models.binding.CarBindingModel;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.service.CloudinaryService;

import com.tsvetkov.autoservice.web.controllers.CarController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarControllerTests {

    @Autowired
    CarController carController;

    @MockBean
    CloudinaryService cloudinaryService;


    @Autowired
    CarRepository carRepository;


    Errors errors;

    MultipartFile testFile;

    CarBindingModel carBindingModel;

    BindingResult bindingResult;

    Car car;



    @Before
    public void init(){
        testFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());
        carBindingModel=new CarBindingModel();
        carBindingModel.setImage(testFile);
        carBindingModel.setBrand("test");
        bindingResult=new BeanPropertyBindingResult(carBindingModel, "validCar");

        car=new Car();
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        carRepository.deleteAll();
    }
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCar_ReturnsCorrectViewName() throws IOException {
        ModelAndView modelAndView = carController.addCar(carBindingModel, new ModelAndView());
        Assert.assertEquals("cars/add-car",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCarConfirm_WhenValidBrand_CarCreated() throws IOException {
        when(cloudinaryService.uploadImage(testFile)).thenReturn("test");
        ModelAndView modelAndView=new ModelAndView();

        carController.addCarConfirm(modelAndView,carBindingModel,bindingResult);


        Assert.assertEquals(1,carRepository.count());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCarConfirm_WhenBrandLengthIsLowerThan2Symbols_ExpectedNoSavedCar() throws IOException {
        carBindingModel.setBrand("a");
        when(cloudinaryService.uploadImage(testFile)).thenReturn("test");
        ModelAndView modelAndView=new ModelAndView();

        carController.addCarConfirm(modelAndView,carBindingModel,bindingResult);


        Assert.assertEquals(0,carRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCarConfirm_WhenBrandLengthIsBiggerThan10Symbols_ExpectedNoSavedCar() throws IOException {
        carBindingModel.setBrand("11111111111111");
        when(cloudinaryService.uploadImage(testFile)).thenReturn("test");
        ModelAndView modelAndView=new ModelAndView();

        carController.addCarConfirm(modelAndView,carBindingModel,bindingResult);


        Assert.assertEquals(0,carRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCarConfirm_WhenBrandAlreadyExists_ExpectedNoSavedCar() throws IOException {
        carRepository.save(car);

        when(cloudinaryService.uploadImage(testFile)).thenReturn("test");
        ModelAndView modelAndView=new ModelAndView();

        carController.addCarConfirm(modelAndView,carBindingModel,bindingResult);


        Assert.assertEquals(1,carRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void allCars_When1Car_Expected1Count(){
        carRepository.save(car);

        ModelAndView modelAndView = carController.allCars(new ModelAndView());
        List actual = (List) modelAndView.getModelMap().get("model");


        Assert.assertEquals(1,actual.size());

    }
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void allCars_ReturnsCorrectViewName(){
        ModelAndView modelAndView = carController.allCars(new ModelAndView());

        Assert.assertEquals("cars/all-cars",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void allCars_When0Cars_ExpectedEmptyModel(){

        ModelAndView modelAndView = carController.allCars(new ModelAndView());

        List actual = (List) modelAndView.getModelMap().get("model");
        System.out.println();

        Assert.assertEquals(0,actual.size());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCar_ReturnsCorrectViewName() throws IOException {
        Car save = carRepository.save(car);

        ModelAndView modelAndView = carController.deleteCar(car.getId(), new ModelAndView());

        Assert.assertEquals("cars/delete-car",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCarConfirm_WhenValidId_ExpectedDeleteCar() throws IOException {
        Car save = carRepository.save(car);

        carController.deleteCarConfirm(car.getId());

        Assert.assertEquals(0,carRepository.findAllByDeletedFalse().size());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCarConfirm_WhenInvalidId_Throw() throws IOException {
        carController.deleteCarConfirm("test");

        Assert.assertEquals(0,carRepository.findAllByDeletedFalse().size());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCar_ReturnsCorrectViewName() throws IOException {
        Car save = carRepository.save(car);

        ModelAndView modelAndView = carController.editCar(save.getId(), new ModelAndView(),carBindingModel);

        Assert.assertEquals("cars/edit-car",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCarConfirm_WhenValidCar_ExpectedEditedCar() throws IOException {
        Car save = carRepository.save(car);
        carBindingModel.setBrand("test1");

        ModelAndView modelAndView = carController.editCarConfirm(new ModelAndView(),save.getId(),carBindingModel,bindingResult);

        Car actual = carRepository.findById(save.getId()).orElse(null);

        Assert.assertEquals(carBindingModel.getBrand(),actual.getBrand());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCarConfirm_WhenBrandLengthIsLowerThan2Symbols_Throw() throws IOException {
        Car save = carRepository.save(car);
        carBindingModel.setBrand("t");

        ModelAndView modelAndView = carController.editCarConfirm(new ModelAndView(),save.getId(),carBindingModel,bindingResult);

        Assert.assertEquals("cars/edit-car",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCarConfirm_WhenBrandLengthIsBiggerThan10Symbols_Throw() throws IOException {
        Car save = carRepository.save(car);

        Car testCar=new Car();
        testCar.setBrand("takenName");
        testCar.setImageUrl("test");
        testCar.setDeleted(false);

        carRepository.save(testCar);

        carBindingModel.setBrand("takenName");

        ModelAndView modelAndView = carController.editCarConfirm(new ModelAndView(),save.getId(),carBindingModel,bindingResult);

        Assert.assertEquals("cars/edit-car",modelAndView.getViewName());
    }


}
