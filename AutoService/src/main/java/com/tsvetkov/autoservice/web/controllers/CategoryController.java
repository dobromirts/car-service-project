package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CategoryAddBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.CategoryEditBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoryViewModel;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.validation.category.CategoryAddValidator;
import com.tsvetkov.autoservice.validation.category.CategoryEditValidator;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final CategoryAddValidator addValidator;
    private final CategoryEditValidator editValidator;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper, CloudinaryService cloudinaryService, CategoryAddValidator addValidator, CategoryEditValidator editValidator) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.addValidator = addValidator;
        this.editValidator = editValidator;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Add Category")
    public ModelAndView addCategory(ModelAndView modelAndView, @ModelAttribute(name = "model") CategoryAddBindingModel model) {
        modelAndView.addObject("model", model);
        return view("category/add-categories", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCategoryConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") CategoryAddBindingModel model, BindingResult bindingResult) throws IOException {
        this.addValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("category/add-categories", modelAndView);
        }
        CategoryServiceModel categoryServiceModel = this.modelMapper.map(model, CategoryServiceModel.class);
        categoryServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));
        this.categoryService.addCategory(categoryServiceModel);
        return redirect("/categories/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Categories")
    public ModelAndView allCategories(ModelAndView modelAndView) {
        List<CategoryViewModel> categories = this.categoryService.findAllCategories()
                .stream().map(c -> this.modelMapper.map(c, CategoryViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model", categories);
        return view("/category/all-categories",modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Edit Category")
    public ModelAndView editCategory(@PathVariable String id, ModelAndView modelAndView,
                                     @ModelAttribute("model") CategoryEditBindingModel model) {
        model= this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryEditBindingModel.class);
        modelAndView.addObject("modelId",id);
        modelAndView.addObject("model",model);

        return view("category/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editCategoryConfirm(@PathVariable String id,ModelAndView modelAndView,
                                            @ModelAttribute("model") CategoryEditBindingModel model,
                                             BindingResult bindingResult) {
        this.editValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("modelId",id);
            modelAndView.addObject("model",model);
            return view("category/edit-category", modelAndView);
        }

        this.categoryService.editCategory(id, this.modelMapper.map(model, CategoryServiceModel.class));
        return redirect("/categories/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Category")
    public ModelAndView deleteCategory(@PathVariable String id, ModelAndView modelAndView) {
        CategoryViewModel category = this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class);
        modelAndView.addObject("model", category);
        return view("/category/delete-category",modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id, ModelAndView modelAndView) {
        this.categoryService.deleteCategory(id);
        return redirect("/categories/all");
    }


    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CategoryViewModel> fetchCategories() {
        List<CategoryViewModel> categories = this.categoryService.findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());

        return categories;
    }


}
