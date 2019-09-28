package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CategoryBindingModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoriesAllViewModel;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCategory(ModelAndView modelAndView){
        modelAndView.setViewName("/category/add-categories");
        return modelAndView;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCategoryConfirm(@ModelAttribute("model") CategoryBindingModel categoryBindingModel, ModelAndView modelAndView) throws IOException {
        CategoryServiceModel categoryServiceModel=this.modelMapper.map(categoryBindingModel, CategoryServiceModel.class);
        categoryServiceModel.setImageUrl(this.cloudinaryService.uploadImage(categoryBindingModel.getImage()));

        this.categoryService.addCategory(categoryServiceModel);
        modelAndView.setViewName("redirect:/categories/all");
        return modelAndView;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allCategories(ModelAndView modelAndView){
        List<CategoriesAllViewModel> categories=this.categoryService.findAllCategories()
                .stream().map(c->this.modelMapper.map(c,CategoriesAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",categories);
        modelAndView.setViewName("/category/all-categories");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editCategory(@PathVariable String id ,ModelAndView modelAndView){
        CategoriesAllViewModel category=this.modelMapper.map(this.categoryService.findCategoryById(id),CategoriesAllViewModel.class);
        modelAndView.addObject("model",category);
        modelAndView.setViewName("/category/edit-category");
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editCategoryConfirm(@PathVariable String id,@ModelAttribute("model") CategoryBindingModel categoryBindingModel,ModelAndView modelAndView){
        this.categoryService.editCategory(id,this.modelMapper.map(categoryBindingModel,CategoryServiceModel.class));
        modelAndView.setViewName("redirect:/categories/all");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCategory(@PathVariable String id ,ModelAndView modelAndView){
        CategoriesAllViewModel category=this.modelMapper.map(this.categoryService.findCategoryById(id),CategoriesAllViewModel.class);
        modelAndView.addObject("model",category);
        modelAndView.setViewName("/category/delete-category");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id,ModelAndView modelAndView){
        this.categoryService.deleteCategory(id);
        modelAndView.setViewName("redirect:/categories/all");
        return modelAndView;
    }



    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<CategoriesAllViewModel> fetchCategories() {
        List<CategoriesAllViewModel> categories = this.categoryService.findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoriesAllViewModel.class))
                .collect(Collectors.toList());

        return categories;
    }



}
