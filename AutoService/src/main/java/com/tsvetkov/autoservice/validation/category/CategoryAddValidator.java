package com.tsvetkov.autoservice.validation.category;

import com.tsvetkov.autoservice.domain.models.binding.CategoryAddBindingModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryAddValidator implements Validator {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryAddValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CategoryAddBindingModel categoryAddBindingModel = (CategoryAddBindingModel) o;

        if (categoryAddBindingModel.getName().length()<3 || categoryAddBindingModel.getName().length()>15){
            errors.rejectValue("name","Invalid name","Name should be between 3 and 15 symbols!");
        }

        if (this.categoryRepository.findCategoriesByNameAndDeletedFalse(categoryAddBindingModel.getName())!=null){
            errors.rejectValue("name","Invalid name","Category already exists!");
        }

    }
}
