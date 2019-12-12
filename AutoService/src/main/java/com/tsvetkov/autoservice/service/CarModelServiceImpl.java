package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.repository.CarModelRepository;
import com.tsvetkov.autoservice.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarModelServiceImpl implements CarModelService {
    private final static String INVALID_MODEL_MESSAGE="Invalid model";

    private final CarModelRepository carModelRepository;
    private final CarService carService;
    private final ModelMapper modelMapper;

    @Autowired
    public CarModelServiceImpl(CarModelRepository carModelRepository, CarService carService, ModelMapper modelMapper) {
        this.carModelRepository = carModelRepository;
        this.carService = carService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CarModelServiceModel addCarModel(String id, CarModelServiceModel carModelServiceModel) {
        CarServiceModel carServiceModel=this.carService.findCarById(id);

        CarModel carModel = this.modelMapper.map(carModelServiceModel, CarModel.class);
        carModel.setCar(this.modelMapper.map(carServiceModel,Car.class));
        carModel.setDeleted(false);

        return this.modelMapper.map(this.carModelRepository.saveAndFlush(carModel), CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllModels() {
        return this.carModelRepository.findAllByDeletedFalse().stream().map(m -> this.modelMapper.map(m, CarModelServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CarModelServiceModel findModelById(String id) {
        CarModel carModel = this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException(INVALID_MODEL_MESSAGE));
        return this.modelMapper.map(carModel,CarModelServiceModel.class);
    }

    @Override
    public List<CarModelServiceModel> findAllCarModels(String id) {
        CarServiceModel carServiceModel=this.carService.findCarById(id);

        List<CarModel> allCarsModelsByCar = this.carModelRepository.findAllCarsModelsByCarAndDeletedFalse(this.modelMapper.map(carServiceModel,Car.class));
        return allCarsModelsByCar.stream().map(m->this.modelMapper.map(m,CarModelServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteModel(String id) {
        CarModel carModel=this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException(INVALID_MODEL_MESSAGE));
        carModel.setDeleted(true);
        this.carModelRepository.save(carModel);
    }

    @Override
    public CarModelServiceModel editModel(String id, CarModelServiceModel carModelServiceModel) {
        CarModel carModel=this.carModelRepository.findById(id).orElseThrow(()->new IllegalArgumentException(INVALID_MODEL_MESSAGE));
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
