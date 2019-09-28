package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CarBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController extends BaseController {
    private final CarService carService;
    private final CarModelService carModelService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public CarController(CarService carService, CarModelService carModelService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.carService = carService;
        this.carModelService = carModelService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    public ModelAndView addCar() {
        return super.view("cars/add-car");
    }

    @PostMapping("/add")
    public ModelAndView addCarConfirm(@ModelAttribute CarBindingModel carBindingModel,ModelAndView modelAndView) throws IOException {
        CarServiceModel carServiceModel=this.modelMapper.map(carBindingModel, CarServiceModel.class);

        carServiceModel.setImageUrl(this.cloudinaryService.uploadImage(carBindingModel.getImage()));
        //TODO THIS SHOULD BE IN THE SERVICE

        this.carService.addCar(carServiceModel);
        modelAndView.setViewName("redirect:/cars/all");
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView allCars(ModelAndView modelAndView) {
        modelAndView.addObject("model",this.carService.findAllCars());
        return super.view("cars/all-cars",modelAndView);
    }



    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CarServiceModel> fetchCategories() {
        List<CarServiceModel> cars = this.carService.findAllCars()
                .stream()
                .map(c -> this.modelMapper.map(c, CarServiceModel.class))
                .collect(Collectors.toList());

        return cars;
    }

    //TODO EDIT AND DELETE IMPL

    @GetMapping("/{id}")
    public ModelAndView carModels(@PathVariable String id, ModelAndView modelAndView) {
        List<CarModelServiceModel> carModelServiceModels=this.carModelService.findAllCarModels(id);

        modelAndView.addObject("model",carModelServiceModels);
        return super.view("cars/car-models",modelAndView);
    }

}
