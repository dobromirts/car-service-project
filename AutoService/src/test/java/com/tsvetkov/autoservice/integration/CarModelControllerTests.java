package com.tsvetkov.autoservice.integration;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.models.binding.CarModelAddBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.CarModelEditBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.web.controllers.CarModelController;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarModelControllerTests {

    @Autowired
    CarModelController carModelController;

    @Autowired
    CarModelRepository carModelRepository;

    @Autowired
    CarRepository carRepository;

    @MockBean
    CarService carService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    CloudinaryService cloudinaryService;

    CarModelAddBindingModel carModelAddBindingModel;

    CarModelEditBindingModel carModelEditBindingModel;

    CarModel carModel;

    BindingResult bindingResult;

    MultipartFile testFile;

    Car car;

    @Before
    public void init(){
        carModelRepository.deleteAll();
        carRepository.deleteAll();
        testFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        car=new Car();
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        carModelAddBindingModel=new CarModelAddBindingModel();
        carModelAddBindingModel.setCar("test");
        carModelAddBindingModel.setHorsePower("100");
        carModelAddBindingModel.setModel("test");
        carModelAddBindingModel.setImage(testFile);

        carModelEditBindingModel=new CarModelEditBindingModel();
        carModelEditBindingModel.setBrand("test1");
        carModelEditBindingModel.setHorsePower("100");
        carModelEditBindingModel.setModel("test1");

        bindingResult=new BeanPropertyBindingResult(carModelAddBindingModel,"carModel");

        carModel=new CarModel();
        carModel.setCar(car);
        carModel.setHorsePower("100");
        carModel.setModel("test");
        carModel.setImageUrl("test");
    }
    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addCarModel_ReturnsCorrectView(){
        ModelAndView modelAndView = carModelController.addCarModel(new ModelAndView(), carModelAddBindingModel);

        Assert.assertEquals("car-models/add-car-model",modelAndView.getViewName());
    }


    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addCarModelConfirm_WhenInvalidModel_ExpectedCorrectView() throws IOException {
        carModelAddBindingModel.setModel("a");
        ModelAndView modelAndView = carModelController.addCarModelConfirm(carModelAddBindingModel,new ModelAndView(),bindingResult);

        Assert.assertEquals("car-models/add-car-model",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addCarModelConfirm_WhenValidModel_ExpectedSavedModel() throws IOException {
        Car savedCar = carRepository.save(car);

        CarServiceModel carServiceModel=new CarServiceModel();
        carServiceModel.setBrand("test");
        carServiceModel.setImageUrl("test");
        carServiceModel.setDeleted(false);
        carServiceModel.setId(savedCar.getId());

        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        when(carService.findCarById(any())).thenReturn(carServiceModel);

        ModelAndView modelAndView = carModelController.addCarModelConfirm(carModelAddBindingModel,new ModelAndView(),bindingResult);
        System.out.println();


        Assert.assertEquals(1,carModelRepository.count());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addCarModelConfirm_WhenValidModel_ExpectedNoSavedModel() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        carModelAddBindingModel.setModel("a");

        ModelAndView modelAndView = carModelController.addCarModelConfirm(carModelAddBindingModel,new ModelAndView(),bindingResult);


        Assert.assertEquals(0,carModelRepository.count());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void allModels_ReturnsCorrectView(){
        ModelAndView modelAndView = carModelController.allModels(new ModelAndView());

        Assert.assertEquals("car-models/all-car-models",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void modelCategories_WhenValidModel_ExpectedCorrectView() throws IOException {
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel savedModel = carModelRepository.save(carModel);

        when(categoryService.findAllCategories()).thenReturn(List.of());

        ModelAndView modelAndView = carModelController.modelCategories(savedModel.getId(),new ModelAndView());

        Assert.assertEquals("category/categories-model-parts",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void deleteCarModel_WhenValidModelId_ExpectedCorrectView(){
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel savedModel = carModelRepository.save(carModel);


        ModelAndView modelAndView = carModelController.deleteCarModel(savedModel.getId(),new ModelAndView());

        Assert.assertEquals("car-models/delete-model",modelAndView.getViewName());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void deleteCarModel_WhenInvalidModelId_Throw(){
        ModelAndView modelAndView = carModelController.deleteCarModel("id",new ModelAndView());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void editCarModel_WhenValidModelId_ExpectedCorrectView(){
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel savedModel = carModelRepository.save(carModel);

        when(carService.findAllCars()).thenReturn(List.of());


        ModelAndView modelAndView = carModelController.editCarModel(savedModel.getId(),new ModelAndView());

        Assert.assertEquals("car-models/edit-model",modelAndView.getViewName());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void editCarModel_WhenInvalidModelId_Throw(){
        ModelAndView modelAndView = carModelController.editCarModel("id",new ModelAndView());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void editCarModelConfirm_WhenValidModelId_ExpectedCorrectView(){
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel savedModel = carModelRepository.save(carModel);

        Car carForEit=new Car();
        carForEit.setDeleted(false);
        carForEit.setBrand("test1");
        carForEit.setImageUrl("test");
        Car edit = carRepository.save(carForEit);

        CarServiceModel carServiceModel=new CarServiceModel();
        carServiceModel.setDeleted(false);
        carServiceModel.setBrand("test1");
        carServiceModel.setImageUrl("test");
        carServiceModel.setId(edit.getId());

        when(carService.findCarByBrand(any())).thenReturn(carServiceModel);



        ModelAndView modelAndView = carModelController.editCarModelConfirm(savedModel.getId(),carModelEditBindingModel);
        System.out.println();

        Assert.assertEquals("redirect:/models/all",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void editCarModelConfirm_WhenValidModelId_ExpectedCorrectEdit(){
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel savedModel = carModelRepository.save(carModel);

        Car carForEit=new Car();
        carForEit.setDeleted(false);
        carForEit.setBrand("test1");
        carForEit.setImageUrl("test");
        Car edit = carRepository.save(carForEit);

        CarServiceModel carServiceModel=new CarServiceModel();
        carServiceModel.setDeleted(false);
        carServiceModel.setBrand("test1");
        carServiceModel.setImageUrl("test");
        carServiceModel.setId(edit.getId());

        when(carService.findCarByBrand(any())).thenReturn(carServiceModel);



        ModelAndView modelAndView = carModelController.editCarModelConfirm(savedModel.getId(),carModelEditBindingModel);
        System.out.println();

        CarModel actual = carModelRepository.findById(savedModel.getId()).orElse(null);

        Assert.assertEquals("test1",actual.getModel());
    }
}
