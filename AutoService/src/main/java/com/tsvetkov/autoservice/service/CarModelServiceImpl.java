package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarModelServiceImpl implements CarModelService {
    private final CarModelRepository carModelRepository;
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final CarRepository carRepository;

    @Autowired
    public CarModelServiceImpl(CarModelRepository carModelRepository, CarService carService, ModelMapper modelMapper, CarRepository carRepository) {
        this.carModelRepository = carModelRepository;
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
    }

    @Override
    public CarModelServiceModel addCarModel(String id, CarModelServiceModel carModelServiceModel) {
        Car car = this.carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid car!"));

        CarModel carModel = this.modelMapper.map(carModelServiceModel, CarModel.class);
        carModel.setCar(car);

        return this.modelMapper.map(this.carModelRepository.saveAndFlush(carModel), CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllModels() {
        return this.carModelRepository.findAll().stream().map(m -> this.modelMapper.map(m, CarModelServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CarModelServiceModel findModelById(String id) {
        CarModel carModel = this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid model"));
        return this.modelMapper.map(carModel,CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllCarModels(String id) {
        Car car=this.carRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid car!"));
        List<CarModel> allCarsModelsByCar = this.carModelRepository.findAllCarsModelsByCar(car);
        return allCarsModelsByCar.stream().map(m->this.modelMapper.map(m,CarModelServiceModel.class)).collect(Collectors.toList());
    }
}
