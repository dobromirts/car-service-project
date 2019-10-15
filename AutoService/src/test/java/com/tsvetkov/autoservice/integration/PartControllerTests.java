package com.tsvetkov.autoservice.integration;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.binding.PartAddBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.repository.PartRepository;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.web.controllers.PartController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartControllerTests {
    @Autowired
    PartController partController;

    @Autowired
    PartRepository partRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CarModelRepository carModelRepository;

    @Autowired
    CarRepository carRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    @MockBean
    CategoryService categoryService;

    @MockBean
    CarModelService carModelService;

    BindingResult bindingResult;

    MultipartFile testFile;

    PartAddBindingModel partAddBindingModel;

    Car car;

    CarModel carModel;

    Category category;


    @Before
    public void init(){
        partRepository.deleteAll();
        testFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        partAddBindingModel=new PartAddBindingModel();
        partAddBindingModel.setName("test");
        partAddBindingModel.setImage(testFile);
        partAddBindingModel.setPrice(BigDecimal.valueOf(100));
        partAddBindingModel.setWorkPrice(BigDecimal.valueOf(100));
        partAddBindingModel.setDescription("");
        partAddBindingModel.setCarModels(new ArrayList<>());
        partAddBindingModel.setCategories(new ArrayList<>());

        bindingResult=new BeanPropertyBindingResult(partAddBindingModel,"model");

        car=new Car();
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        carModel=new CarModel();
        carModel.setModel("test");
        carModel.setImageUrl("test");
        carModel.setHorsePower("100");
        carModel.setDeleted(false);


        category=new Category();
        category.setName("test");
        category.setImageUrl("test");
        category.setDeleted(false);

    }


    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addPart_ExpectedCorrectView(){
        ModelAndView modelAndView = partController.addPart(new ModelAndView(), partAddBindingModel);

        Assert.assertEquals("parts/add-part",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addPartConfirm_WhenValidPart_ExpectedSavedPart() throws IOException {
        Car savedCar = carRepository.save(car);
        carModel.setCar(savedCar);
        CarModel saveModel = carModelRepository.save(carModel);
        Category savedCategory = categoryRepository.save(category);

        CategoryServiceModel categoryServiceModel=new CategoryServiceModel();
        categoryServiceModel.setName("test");
        categoryServiceModel.setImageUrl("test");
        categoryServiceModel.setDeleted(false);
        categoryServiceModel.setId(savedCategory.getId());

        CarServiceModel carServiceModel=new CarServiceModel();
        carServiceModel.setBrand("test");
        carServiceModel.setImageUrl("test");
        carServiceModel.setDeleted(false);
        carServiceModel.setId(savedCar.getId());


        CarModelServiceModel carModelServiceModel=new CarModelServiceModel();
        carModelServiceModel.setModel("test");
        carModelServiceModel.setImageUrl("test");
        carModelServiceModel.setHorsePower("100");
        carModelServiceModel.setDeleted(false);
        carModelServiceModel.setId(saveModel.getId());
        carModelServiceModel.setCar(carServiceModel);

        when(categoryService.findCategoryById(any())).thenReturn(categoryServiceModel);
        when(carModelService.findModelById(any())).thenReturn(carModelServiceModel);
        when(cloudinaryService.uploadImage(any())).thenReturn("test");

        partAddBindingModel.getCategories().add(savedCategory.getName());
        partAddBindingModel.getCarModels().add(saveModel.getModel());


        ModelAndView modelAndView = partController.addPartConfirm(new ModelAndView(),partAddBindingModel,bindingResult);

        Assert.assertEquals(1,partRepository.count());
    }


    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void addPartConfirm_WhenValidPart_ExpectedNoSavedPart() throws IOException {
        partAddBindingModel.setName("a");


        ModelAndView modelAndView = partController.addPartConfirm(new ModelAndView(),partAddBindingModel,bindingResult);

        Assert.assertEquals(0,partRepository.count());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"USER","ADMIN"})
    public void allParts_ExpectedCorrectView(){
        ModelAndView modelAndView = partController.allParts(new ModelAndView());

        Assert.assertEquals("parts/all-parts",modelAndView.getViewName());
    }

}
