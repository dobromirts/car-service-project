package com.tsvetkov.autoservice.validation.category;

import com.tsvetkov.autoservice.domain.models.binding.CategoryEditBindingModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryEditValidator implements Validator {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryEditValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CategoryEditBindingModel categoryEditBindingModel= (CategoryEditBindingModel) o;

        if (categoryEditBindingModel.getName().length()<3){
            errors.rejectValue("name","Invalid name","Name should be at least 3 symbols!");
        }
        if (categoryRepository.findCategoriesByNameAndDeletedFalse(categoryEditBindingModel.getName())!=null){
            errors.rejectValue("name","Invalid name","Category with this name already exists!");
        }
    }
}
