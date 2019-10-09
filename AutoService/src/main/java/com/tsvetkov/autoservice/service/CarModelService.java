package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;

import java.util.List;

public interface CarModelService {

    CarModelServiceModel addCarModel(String id,CarModelServiceModel carModelServiceModel);

    List<CarModelServiceModel> findAllModels();

    CarModelServiceModel findModelById(String id);

    List<CarModelServiceModel> findAllCarModels(String id);

    void deleteModel(String id);

    CarModelServiceModel editModel(String id,CarModelServiceModel carModelServiceModel);

    CarModelServiceModel findModelByModel(String model);
}
