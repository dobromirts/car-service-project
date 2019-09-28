package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoriesAllViewModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        try {
            Category category = this.categoryRepository.save(this.modelMapper.map(categoryServiceModel, Category.class));
            return this.modelMapper.map(category, CategoryServiceModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository.findAll().stream().map(c -> this.modelMapper.map(c, CategoryServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel findCategoryById(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(String id,CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        category.setName(categoryServiceModel.getName());
        return this.modelMapper.map(this.categoryRepository.saveAndFlush(category), CategoryServiceModel.class);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
        this.categoryRepository.delete(category);
    }

    @Override
    public CategoryServiceModel findCategoryByName(String name) {
        return this.modelMapper.map(this.categoryRepository.findCategoriesByName(name),CategoryServiceModel.class);
    }
}