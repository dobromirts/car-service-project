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

    //TODO ONLY CARSERVICE SHOULD STAY HERE WITHOUT REPOSITORY ???

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
        carModel.setDeleted(false);

        return this.modelMapper.map(this.carModelRepository.saveAndFlush(carModel), CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllModels() {
        return this.carModelRepository.findAllByDeletedFalse().stream().map(m -> this.modelMapper.map(m, CarModelServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CarModelServiceModel findModelById(String id) {
        CarModel carModel = this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid model"));
        return this.modelMapper.map(carModel,CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllCarModels(String id) {
        Car car=this.carRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid car!"));
        List<CarModel> allCarsModelsByCar = this.carModelRepository.findAllCarsModelsByCarAndDeletedFalse(car);
        return allCarsModelsByCar.stream().map(m->this.modelMapper.map(m,CarModelServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteModel(String id) {
        CarModel carModel=this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid model!"));
        carModel.setDeleted(true);
        this.carModelRepository.save(carModel);
    }

    @Override
    public CarModelServiceModel editModel(String id, CarModelServiceModel carModelServiceModel) {
        CarModel carModel=this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid model"));
        carModel.setHorsePower(carModelServiceModel.getHorsePower());
        carModel.setCar(this.modelMapper.map(carModelServiceModel.getCar(),Car.class));
        carModel.setModel(carModelServiceModel.getModel());


        return this.modelMapper.map(this.carModelRepository.saveAndFlush(carModel),CarModelServiceModel.class);
    }

    @Override
    public CarModelServiceModel findModelByModel(String model) {
        return this.modelMapper.map(this.carModelRepository.findModelByModelAndDeletedFalse(model),CarModelServiceModel.class);
    }
}
