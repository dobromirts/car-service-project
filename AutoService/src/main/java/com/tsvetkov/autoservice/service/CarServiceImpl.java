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
    private final static String INVALID_CAR_MESSAGE="Invalid Car";

    private final ModelMapper modelMapper;
    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(ModelMapper modelMapper, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
    }

    @Override
    public CarServiceModel addCar(CarServiceModel carServiceModel) {
        carServiceModel.setDeleted(false);
        return this.modelMapper.map(this.carRepository.saveAndFlush(this.modelMapper.map(carServiceModel, Car.class)),CarServiceModel.class);
    }

    @Override
    public List<CarServiceModel> findAllCars() {
        return this.carRepository.findAllByDeletedFalse().stream().map(c->this.modelMapper.map(c,CarServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CarServiceModel deleteCar(String id) {
        Car car=this.carRepository.findById(id).orElseThrow(()->new IllegalArgumentException(INVALID_CAR_MESSAGE));
        car.setDeleted(true);
        return this.modelMapper.map(this.carRepository.save(car),CarServiceModel.class);
    }

    @Override
    public CarServiceModel editCar(String id,CarServiceModel carServiceModel) {
        Car car = this.carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(INVALID_CAR_MESSAGE));
        car.setBrand(carServiceModel.getBrand());
        return this.modelMapper.map(this.carRepository.saveAndFlush(car), CarServiceModel.class);
    }

    @Override
    public CarServiceModel findCarById(String id) {
        Car car=this.carRepository.findById(id).orElseThrow(()->new IllegalArgumentException(INVALID_CAR_MESSAGE));
        return this.modelMapper.map(car,CarServiceModel.class);
    }

    @Override
    public CarServiceModel findCarByBrand(String brand) {
        return this.modelMapper.map(this.carRepository.findByBrandAndDeletedFalse(brand),CarServiceModel.class);
    }
}
