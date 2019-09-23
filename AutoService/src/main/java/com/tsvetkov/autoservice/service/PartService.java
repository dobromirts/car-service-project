package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;

import java.util.List;

public interface PartService {

    PartServiceModel addPart(PartServiceModel partServiceModel);

    List<PartServiceModel> findAllParts();

    PartServiceModel findPartById(String id);

    PartServiceModel editPart(PartServiceModel partServiceModel);

    void deletePart(String id);

    List<PartServiceModel> findAllPartsByModel(String id);
}
