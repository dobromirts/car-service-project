package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.PartAddBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoryViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartAllViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartDetailsViewModel;
import com.tsvetkov.autoservice.error.PartNotFoundException;
import com.tsvetkov.autoservice.service.CarModelService;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.validation.part.PartAddValidator;
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
@RequestMapping("/parts")
public class PartController extends BaseController {
    private final CategoryService categoryService;
    private final PartService partService;
    private final CarModelService carModelService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final PartAddValidator partAddValidator;

    @Autowired
    public PartController(CategoryService categoryService, PartService partService, CarModelService carModelService, CloudinaryService cloudinaryService, ModelMapper modelMapper, PartAddValidator partAddValidator) {
        this.categoryService = categoryService;
        this.partService = partService;
        this.carModelService = carModelService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.partAddValidator = partAddValidator;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Part")
    public ModelAndView addPart(ModelAndView modelAndView, @ModelAttribute("model") PartAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("parts/add-part", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addPartConfirm(ModelAndView modelAndView, @ModelAttribute("model") PartAddBindingModel model, BindingResult bindingResult) throws IOException {
        this.partAddValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("parts/add-part", modelAndView);
        }
        PartServiceModel partServiceModel = this.modelMapper.map(model, PartServiceModel.class);
        List<CategoryServiceModel> categoryServiceModels = model.getCategories().stream()
                .map(this.categoryService::findCategoryById).collect(Collectors.toList());
        List<CarModelServiceModel> carModelServiceModels = model.getCarModels().stream()
                .map(this.carModelService::findModelById).collect(Collectors.toList());
        partServiceModel.setCategories(categoryServiceModels);
        partServiceModel.setCarModels(carModelServiceModels);
        partServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));
        this.partService.addPart(partServiceModel);
        return redirect("/parts/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Parts")
    public ModelAndView allParts(ModelAndView modelAndView) {
        List<PartAllViewModel> model = this.partService.findAllParts().stream().map(p -> this.modelMapper.map(p, PartAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model", model);
        modelAndView.setViewName("parts/all-parts");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Part")
    public ModelAndView deletePart(@PathVariable String id, ModelAndView modelAndView) {
        PartDetailsViewModel partDetailsViewModel = this.modelMapper.map(this.partService.findPartById(id), PartDetailsViewModel.class);
        modelAndView.addObject("model", partDetailsViewModel);
        modelAndView.setViewName("parts/delete-part");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deletePartConfirm(@PathVariable String id, ModelAndView modelAndView) {
        this.partService.deletePart(id);
        modelAndView.setViewName("redirect:/parts/all");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @PageTitle("Edit Part")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editPart(@PathVariable String id, ModelAndView modelAndView) {
        PartServiceModel partServiceModel = this.partService.findPartById(id);
        PartAddBindingModel addBindingModel = this.modelMapper.map(partServiceModel, PartAddBindingModel.class);
        addBindingModel.setCategories(categoryService.findAllCategories().stream().map(c -> c.getName()).collect(Collectors.toList()));
        addBindingModel.setCarModels(carModelService.findAllModels().stream().map(m -> m.getModel()).collect(Collectors.toList()));
        modelAndView.addObject("model", addBindingModel);
        modelAndView.addObject("modelId", id);
        modelAndView.setViewName("parts/edit-part");
        return modelAndView;
    }


    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editPartConfirm(@PathVariable String id, @ModelAttribute PartAddBindingModel partAddBindingModel, ModelAndView modelAndView) {
        PartServiceModel partServiceModel = this.modelMapper.map(partAddBindingModel, PartServiceModel.class);
        partServiceModel.setCategories(partAddBindingModel.getCategories().stream().map(this.categoryService::findCategoryByName).collect(Collectors.toList()));
        partServiceModel.setCarModels(partAddBindingModel.getCarModels().stream().map(this.carModelService::findModelByModel).collect(Collectors.toList()));
        partServiceModel.setId(id);

        this.partService.editPart(partServiceModel);
        modelAndView.setViewName("redirect:/parts/all");
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Part Details")
    public ModelAndView detailsPart(@PathVariable String id, ModelAndView modelAndView) {
        PartDetailsViewModel partDetailsViewModel = this.modelMapper.map(this.partService.findPartById(id), PartDetailsViewModel.class);
        modelAndView.addObject("model", partDetailsViewModel);
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
    @PageTitle("Parts")
    public ModelAndView partsForModelAndCategory(@RequestParam(value = "param1", required = true) String carModelId,
                                                 @RequestParam(value = "param2", required = true) String categoryId,
                                                 ModelAndView modelAndView) {
        List<PartDetailsViewModel> partDetailsViewModels = this.partService.findAllPartsByModelAndCategory(carModelId, categoryId)
                .stream().map(p -> this.modelMapper.map(p, PartDetailsViewModel.class)).collect(Collectors.toList());

        modelAndView.addObject("model", partDetailsViewModels);
        modelAndView.setViewName("parts/parts-model");
        return modelAndView;
    }


}
