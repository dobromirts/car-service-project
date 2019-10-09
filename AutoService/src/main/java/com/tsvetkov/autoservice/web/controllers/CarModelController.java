package com.tsvetkov.autoservice.web.controllers;


import com.tsvetkov.autoservice.domain.models.binding.CarModelAddBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.CarModelEditBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;

import com.tsvetkov.autoservice.domain.models.service.CarServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CarModelEditViewModel;
import com.tsvetkov.autoservice.domain.models.view.CarModelHomeViewModel;
import com.tsvetkov.autoservice.domain.models.view.CarModelViewModel;
import com.tsvetkov.autoservice.domain.models.view.CategoryViewModel;
import com.tsvetkov.autoservice.service.*;
import com.tsvetkov.autoservice.validation.carmodel.CarModelAddValidator;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/models")
public class CarModelController extends BaseController {
    private final CarModelService carModelService;
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final CarModelAddValidator addValidator;

    public CarModelController(CarModelService carModelService, CarService carService, ModelMapper modelMapper, CategoryService categoryService, CloudinaryService cloudinaryService, CarModelAddValidator addValidator) {
        this.carModelService = carModelService;
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.addValidator = addValidator;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Model")
    public ModelAndView addCarModel(ModelAndView modelAndView,@ModelAttribute("model") CarModelAddBindingModel model) {
        modelAndView.addObject("model",model);
        return view("car-models/add-car-model",modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCarModelConfirm(@ModelAttribute("model") CarModelAddBindingModel model, ModelAndView modelAndView, BindingResult bindingResult) throws IOException {
        this.addValidator.validate(model,bindingResult);
        if (bindingResult.hasErrors()){
            modelAndView.addObject("model",model);
            return view("car-models/add-car-model",modelAndView);
        }
        CarModelServiceModel carModelServiceModel = this.modelMapper.map(model, CarModelServiceModel.class);
        carModelServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));
        this.carModelService.addCarModel(model.getCar(), carModelServiceModel);
        return redirect("/models/all");
    }

    @GetMapping("/all")
    @PageTitle("All Models")
    public ModelAndView allModels(ModelAndView modelAndView) {
        modelAndView.addObject("model", this.carModelService.findAllModels());
        return super.view("car-models/all-car-models", modelAndView);
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CarModelViewModel> fetchModels() {
        List<CarModelViewModel> models = this.carModelService.findAllModels()
                .stream()
                .map(m -> this.modelMapper.map(m, CarModelViewModel.class))
                .collect(Collectors.toList());
        return models;
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Categories")
    public ModelAndView modelCategories(@PathVariable String id, ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.findAllCategories()
                .stream().map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList());

        CarModelServiceModel carModel = this.carModelService.findModelById(id);

        modelAndView.addObject("model", categoryViewModels);
        modelAndView.addObject("carModel", carModel);
        return super.view("category/categories-model-parts", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Model")
    public ModelAndView deleteCarModel(@PathVariable String id, ModelAndView modelAndView) {
        CarModelServiceModel carModelServiceModel = this.carModelService.findModelById(id);
        modelAndView.addObject("model", this.modelMapper.map(carModelServiceModel, CarModelViewModel.class));
        return view("car-models/delete-model", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCarModelConfirm(@PathVariable String id) {
        this.carModelService.deleteModel(id);
        return redirect("/models/all");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Model")
    public ModelAndView editCarModel(@PathVariable String id, ModelAndView modelAndView) {
        CarModelServiceModel carModelServiceModel = this.carModelService.findModelById(id);

        CarModelEditViewModel carModelEditBindingModel = this.modelMapper.map(carModelServiceModel, CarModelEditViewModel.class);
        carModelEditBindingModel.setBrand(carService.findAllCars().stream().map(c -> c.getBrand()).collect(Collectors.toList()));

        modelAndView.addObject("model", carModelEditBindingModel);
        modelAndView.addObject("modelId", id);
        modelAndView.setViewName("parts/edit-part");
        return view("car-models/edit-model", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editCarModelConfirm(@PathVariable String id, @ModelAttribute CarModelEditBindingModel carModelEditBindingModel) {
        CarModelServiceModel carModelServiceModel = this.modelMapper.map(carModelEditBindingModel, CarModelServiceModel.class);
        carModelServiceModel.setId(id);
        CarServiceModel car = this.carService.findCarByBrand(carModelEditBindingModel.getBrand());
        carModelServiceModel.setCar(car);

        this.carModelService.editModel(id, carModelServiceModel);
        return redirect("/models/all");
    }
    @GetMapping("/car/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Models")
    public ModelAndView carModels(@PathVariable String id, ModelAndView modelAndView) {
        List<CarModelHomeViewModel> carModelHomeViewModels=this.carModelService
                .findAllCarModels(id).stream().map(m->this.modelMapper.map(m,CarModelHomeViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",carModelHomeViewModels);
        return view("car-models/models-car",modelAndView);
    }
}
