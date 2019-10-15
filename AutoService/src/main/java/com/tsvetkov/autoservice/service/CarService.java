package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;

import java.util.List;

public interface CarService {

    CarServiceModel addCar(CarServiceModel carServiceModel);

    CarServiceModel findCarById(String id);

    List<CarServiceModel> findAllCars();

    CarServiceModel deleteCar(String id);

    CarServiceModel editCar(String id,CarServiceModel carServiceModel);

    CarServiceModel findCarByBrand(String brand);
}
