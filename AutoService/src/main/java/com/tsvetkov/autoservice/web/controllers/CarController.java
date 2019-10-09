package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CarBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CarModelHomeViewModel;
import com.tsvetkov.autoservice.domain.models.view.CarViewModel;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.validation.car.CarValidator;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController extends BaseController {
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final CarValidator validator;

    @Autowired
    public CarController(CarService carService,ModelMapper modelMapper, CloudinaryService cloudinaryService, CarValidator validator) {
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.validator = validator;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Car")
    public ModelAndView addCar(@ModelAttribute(name = "model") CarBindingModel model, ModelAndView modelAndView) {
        modelAndView.addObject("model",model);
        return view("cars/add-car",modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCarConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") CarBindingModel model, BindingResult bindingResult) throws IOException {
        this.validator.validate(model,bindingResult);

        if (bindingResult.hasErrors()){
            modelAndView.addObject("model",model);
            return view("cars/add-car",modelAndView);
        }
        CarServiceModel carServiceModel=this.modelMapper.map(model, CarServiceModel.class);
        carServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));
        this.carService.addCar(carServiceModel);
        return redirect("/cars/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Cars")
    public ModelAndView allCars(ModelAndView modelAndView) {
        List<CarViewModel>carViewModels=this.carService.findAllCars()
                .stream().map(c->this.modelMapper.map(c,CarViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",carViewModels);
        return view("cars/all-cars",modelAndView);
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



    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Car")
    public ModelAndView deleteCar(@PathVariable String id,ModelAndView modelAndView){
        CarViewModel carViewModel=this.modelMapper.map(carService.findCarById(id),CarViewModel.class);
        modelAndView.addObject("model",carViewModel);
        return view("cars/delete-car",modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCarConfirm(@PathVariable String id){
        this.carService.deleteCar(id);
        return redirect("/cars/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Car")
    public ModelAndView editCar(@PathVariable String id,ModelAndView modelAndView,@ModelAttribute(name = "model") CarBindingModel model){
        model=this.modelMapper.map(carService.findCarById(id),CarBindingModel.class);
        modelAndView.addObject("modelId",id);
        modelAndView.addObject("model",model);
        return view("cars/edit-car",modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editCarConfirm(ModelAndView modelAndView,@PathVariable String id,@ModelAttribute(name = "model") CarBindingModel model,BindingResult bindingResult){
        this.validator.validate(model,bindingResult);
        if (bindingResult.hasErrors()){
            modelAndView.addObject("modelId",id);
            modelAndView.addObject("model",model);
            return view("cars/edit-car",modelAndView);
        }
        this.carService.editCar(id,this.modelMapper.map(model,CarServiceModel.class));
        return redirect("/cars/all");
    }

}
