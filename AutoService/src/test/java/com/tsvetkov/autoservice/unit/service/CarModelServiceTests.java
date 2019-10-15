package com.tsvetkov.autoservice.unit.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CarModelServiceImpl;
import com.tsvetkov.autoservice.service.CarService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarModelServiceTests {

    @Mock
    CarModelRepository carModelRepository;

    @Mock
    CarService carService;

    CarModelService carModelService;

    CarModelServiceModel carModelServiceModel;

    CarServiceModel carServiceModel;

    Car car;

    CarModel carModel;

    @Before
    public void init(){
        ModelMapper modelMapper=new ModelMapper();
        carModelService=new CarModelServiceImpl(carModelRepository,carService,modelMapper);

        carServiceModel=new CarServiceModel();
        carServiceModel.setId("1");
        carServiceModel.setBrand("test");
        carServiceModel.setImageUrl("test");
        carServiceModel.setDeleted(false);

        car=new Car();
        car.setId("1");
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        carModelServiceModel=new CarModelServiceModel();
        carModelServiceModel.setId("1");
        carModelServiceModel.setImageUrl("test");
        carModelServiceModel.setCar(carServiceModel);
        carModelServiceModel.setModel("test");
        carModelServiceModel.setHorsePower("100");
        carModelServiceModel.setDeleted(false);


        carModel=new CarModel();
        carModel.setId("1");
        carModel.setImageUrl("test");
        carModel.setCar(car);
        carModel.setModel("test");
        carModel.setHorsePower("100");
        carModel.setDeleted(false);
    }

    @Test
    public void addCarModel_WhenValidCar_ExpectedSavedModel(){
        when(carService.findCarById(any())).thenReturn(carServiceModel);

        CarModelServiceModel toBeSaved=new CarModelServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setImageUrl("test");
        toBeSaved.setCar(carServiceModel);
        toBeSaved.setModel("test");
        toBeSaved.setHorsePower("100");

        when(carModelRepository.saveAndFlush(any())).thenReturn(carModel);

        CarModelServiceModel actual=carModelService.addCarModel("1",toBeSaved);

        Assert.assertEquals(carModelServiceModel.getDeleted(),actual.getDeleted());
        Assert.assertEquals(carModelServiceModel.getModel(),actual.getModel());
        Assert.assertEquals(carModelServiceModel.getId(),actual.getId());
    }


    @Test
    public void findAllModels_When0Models_ExpectedEmptyList(){
        when(carModelRepository.findAllByDeletedFalse()).thenReturn(List.of());
        List<CarModelServiceModel> actual = carModelService.findAllModels();

        Assert.assertEquals(0,actual.size());
    }

    @Test
    public void findAllModels_When1Models_Expected1Count(){

        when(carModelRepository.findAllByDeletedFalse()).thenReturn(List.of(carModel));
        List<CarModelServiceModel> actual = carModelService.findAllModels();

        Assert.assertEquals(1,actual.size());
    }

    @Test
    public void findAllCarModels_WhenCarWith0Models_ExpectedEmptyList(){
        when(carService.findCarById("1")).thenReturn(carServiceModel);
        when(carModelRepository.findAllCarsModelsByCarAndDeletedFalse(any())).thenReturn(List.of());

        List<CarModelServiceModel> actual = carModelService.findAllCarModels("1");
        Assert.assertEquals(0,actual.size());
    }

    @Test
    public void findAllCarModels_WhenCarWith2Models_Expected2Count(){
        when(carService.findCarById("1")).thenReturn(carServiceModel);
        when(carModelRepository.findAllCarsModelsByCarAndDeletedFalse(any())).thenReturn(List.of(carModel,carModel));

        List<CarModelServiceModel> actual = carModelService.findAllCarModels("1");
        Assert.assertEquals(2,actual.size());
    }

    @Test
    public void deleteModel_ExpectedInvokedRepositorySaveMethod(){
        when(carModelRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(carModel));
        carModelService.deleteModel("1");
        verify(carModelRepository).save(any(CarModel.class));
    }


}
