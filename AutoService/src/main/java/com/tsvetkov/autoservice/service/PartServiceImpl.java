package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.entities.Part;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.error.PartNotFoundException;
import com.tsvetkov.autoservice.repository.PartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {
    private final static String INCORRECT_PART_MESSAGE="Incorrect Part!";
    private final static String PART_NOT_FOUND_MESSAGE="Part with given id was not found!";

    private final PartRepository partRepository;
    private final CategoryService categoryService;
    private final CarModelService carModelService;
    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, CategoryService categoryService, CarModelService carModelService, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.categoryService = categoryService;
        this.carModelService = carModelService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PartServiceModel addPart(PartServiceModel partServiceModel) {
        Part part=this.modelMapper.map(partServiceModel,Part.class);
        return this.modelMapper.map(this.partRepository.saveAndFlush(part),PartServiceModel.class);
    }

    @Override
    public List<PartServiceModel> findAllParts() {
        return this.partRepository.findAll().stream().map(p->this.modelMapper.map(p,PartServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public PartServiceModel findPartById(String id) {
        Part part=this.partRepository.findById(id).orElseThrow(()->new PartNotFoundException(PART_NOT_FOUND_MESSAGE));
        return this.modelMapper.map(part,PartServiceModel.class);
    }

    @Override
    public PartServiceModel editPart(PartServiceModel partServiceModel) {
        Part part=this.partRepository.findById(partServiceModel.getId()).orElseThrow(()->new IllegalArgumentException(INCORRECT_PART_MESSAGE));
        part.setName(partServiceModel.getName());
        part.setDescription(partServiceModel.getDescription());
        part.setPrice(partServiceModel.getPrice());
        part.setCategories(partServiceModel.getCategories().stream().map(c->this.modelMapper.map(c, Category.class)).collect(Collectors.toList()));
        part.setCarModels(partServiceModel.getCarModels().stream().map(m->this.modelMapper.map(m, CarModel.class)).collect(Collectors.toList()));

        return this.modelMapper.map(this.partRepository.saveAndFlush(part),PartServiceModel.class);
    }

    @Override
    public void deletePart(String id) {
        this.partRepository.deleteById(id);
    }

    @Override
    public List<PartServiceModel> findAllPartsByModel(String id) {
        CarModel carModel=this.modelMapper.map(this.carModelService.findModelById(id),CarModel.class);

        List<Part> allPartsByCarModels = this.partRepository.findAllPartsByCarModels(carModel);
        return allPartsByCarModels.stream().map(p->this.modelMapper.map(p,PartServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<PartServiceModel> findAllByCategory(String category) {
        return this.partRepository.findAll()
                .stream()
                .filter(product -> product.getCategories().stream().anyMatch(categoryStream -> categoryStream.getName().equals(category)))
                .map(product -> this.modelMapper.map(product, PartServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PartServiceModel> findAllPartsByModelAndCategory(String carModelId, String categoryId) {
        CarModel carModel=this.modelMapper.map(this.carModelService.findModelById(carModelId),CarModel.class);
        Category category=this.modelMapper.map(this.categoryService.findCategoryById(categoryId),Category.class);

        List<PartServiceModel> partServiceModels=this.partRepository.findAllByCarModelsAndCategories(carModel,category).stream()
                .map(p->this.modelMapper.map(p,PartServiceModel.class)).collect(Collectors.toList());
        return partServiceModels;
    }
}
