package com.tsvetkov.autoservice.unit.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.repository.CarRepository;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.CarServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTests {

    CarRepository carRepository;
    ModelMapper modelMapper;
    CarService carService;

    @Before
    public void init(){
        carRepository= Mockito.mock(CarRepository.class);
        modelMapper=new ModelMapper();
        carService=new CarServiceImpl(modelMapper,carRepository);
    }

    @Test
    public void addCar_whenValidCar_expectedCar(){
        Car expected=new Car();
        expected.setId("1");
        expected.setBrand("1");
        expected.setImageUrl("1");

        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(expected);
        CarServiceModel actual=carService.addCar(new CarServiceModel());

        Assert.assertEquals(expected.getId(),actual.getId());
        Assert.assertEquals(expected.getBrand(),actual.getBrand());
        Assert.assertEquals(expected.getImageUrl(),actual.getImageUrl());
    }

    @Test(expected = Exception.class)
    public void addCar_whenInvalidCar_throwsException(){
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(null);
        CarServiceModel actual=carService.addCar(new CarServiceModel());
    }
    @Test(expected = Exception.class)
    public void addCar_withNullBrand_throwsException() {
        CarServiceModel toBeSaved = new CarServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setBrand(null);
        toBeSaved.setImageUrl("1");

        when(carRepository.saveAndFlush(any(Car.class))).thenThrow(Exception.class);
        CarServiceModel actual = carService.addCar(toBeSaved);
    }

    @Test(expected = Exception.class)
    public void addCar_withNullUrl_throwsException() {
        CarServiceModel toBeSaved = new CarServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setBrand("1");
        toBeSaved.setImageUrl(null);

        when(carRepository.saveAndFlush(any(Car.class))).thenThrow(Exception.class);
        CarServiceModel actual = carService.addCar(toBeSaved);
    }

    @Test
    public void findAllCars_when2Cars_expected2Cars(){
        Car one=new Car();
        one.setId("1");
        one.setBrand("1");
        one.setImageUrl("1");
        one.setDeleted(false);

        Car two=new Car();
        two.setId("2");
        two.setBrand("2");
        two.setImageUrl("2");
        two.setDeleted(false);

        when(carRepository.findAllByDeletedFalse()).thenReturn(List.of(one,two));
        List<CarServiceModel> actual=carService.findAllCars();

        Assert.assertEquals(2,actual.size());
    }

    @Test
    public void findAllCars_when0Cars_expectedEmptyList(){
        when(carRepository.findAllByDeletedFalse()).thenReturn(List.of());
        List<CarServiceModel> actual=carService.findAllCars();
        Assert.assertEquals(0,actual.size());
    }

    @Test
    public void deleteCar_whenValidId_expectedDeletedCar() {
        Car car = new Car();
        car.setId("1");
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        Car expected = new Car();
        expected.setId("1");
        expected.setBrand("test");
        expected.setImageUrl("test");
        expected.setDeleted(true);

        lenient().when(carRepository.findById("1"))
                .thenReturn(Optional.of(car));

        when(carRepository.save(any(Car.class))).thenReturn(expected);

        CarServiceModel actual = carService.deleteCar("1");

        Assert.assertEquals(expected.getDeleted(), actual.getDeleted());
    }

    @Test(expected = Exception.class)
    public void deleteCar_whenInvalidId_throwsException() {
        Car car = new Car();
        car.setId("1");
        car.setBrand("test");
        car.setImageUrl("test");
        car.setDeleted(false);

        lenient().when(carRepository.findById("1"))
                .thenReturn(Optional.of(car));

        lenient().when(carRepository.save(any(Car.class))).thenReturn(car);

        CarServiceModel actual = carService.deleteCar("2");
    }


    @Test
    public void editCar_whenValidId_expectedEditedCategory() {
        CarServiceModel toBeEdit = new CarServiceModel();
        toBeEdit.setId("1");
        toBeEdit.setBrand("test1");
        toBeEdit.setImageUrl("test");

        Car car = new Car();
        car.setId("1");
        car.setBrand("test");
        car.setImageUrl("test");

        Car expected = new Car();
        expected.setId("1");
        expected.setBrand("test1");
        expected.setImageUrl("test");

        when(carRepository.findById("1"))
                .thenReturn(Optional.of(car));

        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(expected);

        CarServiceModel actual = carService.editCar("1", toBeEdit);

        Assert.assertEquals(expected.getBrand(), actual.getBrand());

    }

    @Test(expected = Exception.class)
    public void editCar_whenInvalidId_throwsException() {
        CarServiceModel toBeEdit = new CarServiceModel();
        toBeEdit.setId("invalid");
        toBeEdit.setBrand("test1");
        toBeEdit.setImageUrl("test");

        Car car = new Car();
        car.setId("1");
        car.setBrand("test");
        car.setImageUrl("test");

        Car expected = new Car();
        expected.setId("1");
        expected.setBrand("test1");
        expected.setImageUrl("test");

        lenient().when(carRepository.findById("1"))
                .thenReturn(Optional.of(car));

        lenient().when(carRepository.saveAndFlush(any(Car.class))).thenReturn(expected);

        CarServiceModel actual = carService.editCar("invalid", toBeEdit);
    }
}
