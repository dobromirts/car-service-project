package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;

import java.util.List;

public interface CarService {

    CarServiceModel addCar(CarServiceModel carServiceModel);

    List<CarServiceModel> findAllCars();
}
