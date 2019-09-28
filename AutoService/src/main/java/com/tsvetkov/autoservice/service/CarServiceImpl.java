package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService{
    private final ModelMapper modelMapper;
    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(ModelMapper modelMapper, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
    }

    @Override
    public CarServiceModel addCar(CarServiceModel carServiceModel) {
        return this.modelMapper.map(this.carRepository.saveAndFlush(this.modelMapper.map(carServiceModel, Car.class)),CarServiceModel.class);
    }

    @Override
    public List<CarServiceModel> findAllCars() {
        return this.carRepository.findAll().stream().map(c->this.modelMapper.map(c,CarServiceModel.class)).collect(Collectors.toList());
    }
}
