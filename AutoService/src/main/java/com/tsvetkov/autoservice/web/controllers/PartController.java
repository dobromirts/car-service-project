package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.PartAddBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoriesAllViewModel;
import com.tsvetkov.autoservice.domain.models.view.CategoryViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartAllViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartDetailsViewModel;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.service.PartService;
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
@RequestMapping("/parts")
public class PartController extends BaseController{
    private final CategoryService categoryService;
    private final PartService partService;
    private final CarModelService carModelService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public PartController(CategoryService categoryService, PartService partService, CarModelService carModelService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.partService = partService;
        this.carModelService = carModelService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addPart(ModelAndView modelAndView) {
        List<CategoriesAllViewModel> categories = this.categoryService.findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoriesAllViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("model", categories);
        modelAndView.setViewName("parts/add-part");
        return modelAndView;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addPartConfirm(@ModelAttribute("model") PartAddBindingModel partAddBindingModel, ModelAndView modelAndView) throws IOException {
        PartServiceModel partServiceModel = this.modelMapper.map(partAddBindingModel, PartServiceModel.class);

        List<CategoryServiceModel> categoryServiceModels = partAddBindingModel.getCategories().stream()
                .map(this.categoryService::findCategoryById).collect(Collectors.toList());

        List<CarModelServiceModel> carModelServiceModels = partAddBindingModel.getCarModels().stream()
                .map(this.carModelService::findModelById).collect(Collectors.toList());
//        partServiceModel.getCategories().clear();
        partServiceModel.setCategories(categoryServiceModels);
        partServiceModel.setCarModels(carModelServiceModels);

        partServiceModel.setImageUrl(this.cloudinaryService.uploadImage(partAddBindingModel.getImage()));
        //TODO THIS SHOULD BE IN THE SERVICE

        this.partService.addPart(partServiceModel);

        modelAndView.setViewName("redirect:/parts/all");

        return modelAndView;
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allParts(ModelAndView modelAndView) {
        List<PartAllViewModel> model=this.partService.findAllParts().stream().map(p->this.modelMapper.map(p,PartAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",model);
        modelAndView.setViewName("parts/all-parts");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deletePart(@PathVariable String id ,ModelAndView modelAndView) {
        PartDetailsViewModel partDetailsViewModel=this.modelMapper.map(this.partService.findPartById(id),PartDetailsViewModel.class);
        modelAndView.addObject("model",partDetailsViewModel);
        modelAndView.setViewName("parts/delete-part");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deletePartConfirm(@PathVariable String id ,ModelAndView modelAndView) {
        this.partService.deletePart(id);
        modelAndView.setViewName("redirect:/parts/all");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editPart(@PathVariable String id,ModelAndView modelAndView) {
        PartServiceModel partServiceModel=this.partService.findPartById(id);
        PartAddBindingModel addBindingModel=this.modelMapper.map(partServiceModel,PartAddBindingModel.class);
        addBindingModel.setCategories(categoryService.findAllCategories().stream().map(c->c.getName()).collect(Collectors.toList()));
        modelAndView.addObject("model",addBindingModel);
        modelAndView.addObject("modelId",id);
        modelAndView.setViewName("parts/edit-part");
        return modelAndView;
    }


    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editPartConfirm(@PathVariable String id,@ModelAttribute PartAddBindingModel partAddBindingModel,ModelAndView modelAndView) {
        PartServiceModel partServiceModel=this.modelMapper.map(partAddBindingModel,PartServiceModel.class);
        partServiceModel.getCategories().clear();
        partServiceModel.setCategories(partAddBindingModel.getCategories().stream().map(this.categoryService::findCategoryByName).collect(Collectors.toList()));
        partServiceModel.setId(id);

        PartServiceModel partServiceModel1=this.partService.editPart(partServiceModel);
        modelAndView.setViewName("redirect:/parts/all");
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsPart(@PathVariable String id,ModelAndView modelAndView) {
        PartDetailsViewModel partDetailsViewModel=this.modelMapper.map(this.partService.findPartById(id),PartDetailsViewModel.class);
        modelAndView.addObject("model",partDetailsViewModel);
        modelAndView.setViewName("parts/details-part");
        return modelAndView;
    }


//    @GetMapping("/category/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ModelAndView partsForCategory(@PathVariable String id,ModelAndView modelAndView) {
//        List<PartServiceModel> partServiceModels=this.partService.findAllPartsByModel(id);
//
//        modelAndView.addObject("model",partServiceModels);
//        return super.view("car-models/model-parts",modelAndView);
//    }

    @GetMapping("/category-parts")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView partsForModelAndCategory(@RequestParam(value = "param1", required = true) String carModelId,
                                                 @RequestParam(value = "param2", required = true) String categoryId,
                                                 ModelAndView modelAndView) {
        List<PartDetailsViewModel>partDetailsViewModels=this.partService.findAllPartsByModelAndCategory(carModelId,categoryId)
                .stream().map(p->this.modelMapper.map(p,PartDetailsViewModel.class)).collect(Collectors.toList());

        modelAndView.addObject("model",partDetailsViewModels);
        modelAndView.setViewName("car-models/model-parts");
        return modelAndView;
    }


}
