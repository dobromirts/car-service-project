package com.tsvetkov.autoservice.unit.validation.category;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.binding.CategoryEditBindingModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.validation.category.CategoryEditValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryEditValidatorTests {

    @Mock
    CategoryRepository categoryRepository;

    CategoryEditValidator categoryEditValidator;

    CategoryEditBindingModel categoryEditBindingModel;



    @Before
    public void init(){
        categoryEditValidator=new CategoryEditValidator(categoryRepository);
        categoryEditBindingModel=new CategoryEditBindingModel();
        categoryEditBindingModel.setName("test");

    }

    @Test
    public void validate_WhenValidCategory_NoExceptions(){
        Errors errors =new BeanPropertyBindingResult(categoryEditValidator,"validCategory");
        categoryEditValidator.validate(categoryEditBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }


    @Test
    public void validate_WhenNameLengthIsLowerThan3Symbols_ExpectedException(){
        categoryEditBindingModel.setName("a");
        Errors errors =new BeanPropertyBindingResult(categoryEditValidator,"invalidCategory");
        categoryEditValidator.validate(categoryEditBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

}
