package com.tsvetkov.autoservice.web.controllers;


import com.tsvetkov.autoservice.domain.models.binding.CarModelAddBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;

import com.tsvetkov.autoservice.domain.models.view.CarModelAllViewModel;
import com.tsvetkov.autoservice.domain.models.view.CategoryViewModel;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.service.PartService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/models")
public class CarModelController extends BaseController {
    private final CarModelService carModelService;
    private final ModelMapper modelMapper;
    private final PartService partService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    public CarModelController(CarModelService carModelService, ModelMapper modelMapper, PartService partService, CategoryService categoryService, CloudinaryService cloudinaryService) {
        this.carModelService = carModelService;
        this.modelMapper = modelMapper;
        this.partService = partService;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    public ModelAndView addCarModel() {
        return super.view("car-models/add-car-model");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCarModelConfirm(@ModelAttribute("model") CarModelAddBindingModel carModelAddBindingModel, ModelAndView modelAndView) throws IOException {
        CarModelServiceModel carModelServiceModel = this.modelMapper.map(carModelAddBindingModel, CarModelServiceModel.class);
        carModelServiceModel.setImageUrl(this.cloudinaryService.uploadImage(carModelAddBindingModel.getImage()));
        this.carModelService.addCarModel(carModelAddBindingModel.getCar(), carModelServiceModel);

        modelAndView.setViewName("redirect:/models/all");
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView allModels(ModelAndView modelAndView) {
        modelAndView.addObject("model", this.carModelService.findAllModels());
        return super.view("car-models/all-car-models", modelAndView);
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CarModelAllViewModel> fetchModels() {
        List<CarModelAllViewModel> models = this.carModelService.findAllModels()
                .stream()
                .map(m -> this.modelMapper.map(m, CarModelAllViewModel.class))
                .collect(Collectors.toList());
        return models;
    }

//    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ModelAndView modelParts(@PathVariable String id, ModelAndView modelAndView) {
//        List<PartServiceModel> partServiceModels=this.partService.findAllPartsByModel(id);
//
//        modelAndView.addObject("model",partServiceModels);
//        return super.view("car-models/model-parts",modelAndView);
//    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView modelCategories(@PathVariable String id, ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels=this.categoryService.findAllCategories()
                .stream().map(c->this.modelMapper.map(c,CategoryViewModel.class)).collect(Collectors.toList());

        CarModelServiceModel carModel=this.carModelService.findModelById(id);

        modelAndView.addObject("model",categoryViewModels);
        modelAndView.addObject("carModel",carModel);
        return super.view("category/categories-model-parts",modelAndView);
    }
}
