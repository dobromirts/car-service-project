package com.tsvetkov.autoservice.validation.part;

import com.tsvetkov.autoservice.domain.models.binding.PartAddBindingModel;
import com.tsvetkov.autoservice.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PartAddValidator implements Validator {
    private final PartRepository partRepository;

    @Autowired
    public PartAddValidator(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PartAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PartAddBindingModel partAddBindingModel= (PartAddBindingModel) o;
        if (partAddBindingModel.getName().length()<2 || partAddBindingModel.getName().length()>50){
            errors.rejectValue("name","Name should be between 2 and 10 symbols!","Name should be between 2 and 20 symbols!");
        }

        if (this.partRepository.findByName(partAddBindingModel.getName())!=null){
            errors.rejectValue("name","Part with this name already exists!","Part with this name already exists!");
        }

        if (partAddBindingModel.getPrice()==null){
            errors.rejectValue("price","Price can not be null!","Price can be null!");
        }

        if (partAddBindingModel.getWorkPrice()==null){
            errors.rejectValue("workPrice","Work price can not be null!","Work price can be null!");
        }

        if (partAddBindingModel.getCategories()==null){
            errors.rejectValue("categories","Select category!","Select category!");
        }
        if (partAddBindingModel.getCarModels()==null){
            errors.rejectValue("carModels","Select model!","Select model!");
        }
    }
}
