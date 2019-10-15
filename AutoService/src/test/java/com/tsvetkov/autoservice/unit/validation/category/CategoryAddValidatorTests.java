package com.tsvetkov.autoservice.unit.validation.category;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.binding.CategoryAddBindingModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.validation.category.CategoryAddValidator;
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
public class CategoryAddValidatorTests {
    @Mock
    CategoryRepository categoryRepository;

    CategoryAddValidator categoryAddValidator;

    CategoryAddBindingModel categoryAddBindingModel;

    MultipartFile file;

    @Before
    public void init(){
        file=new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        categoryAddValidator=new CategoryAddValidator(categoryRepository);

        categoryAddBindingModel=new CategoryAddBindingModel();
        categoryAddBindingModel.setName("test");
        categoryAddBindingModel.setImage(file);
    }

    @Test
    public void validate_WhenValidCategory_NoException(){
        Errors errors=new BeanPropertyBindingResult(categoryAddBindingModel,"validCategory");
        categoryAddValidator.validate(categoryAddBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenNameLengthIsLowerThan3Symbols_ExpectedException(){
        categoryAddBindingModel.setName("a");
        Errors errors=new BeanPropertyBindingResult(categoryAddBindingModel,"invalidCategory");
        categoryAddValidator.validate(categoryAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenNameLengthIsBiggerThan15Symbols_ExpectedException(){
        categoryAddBindingModel.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Errors errors=new BeanPropertyBindingResult(categoryAddBindingModel,"invalidCategory");
        categoryAddValidator.validate(categoryAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenCategoryAlreadyExists_ExpectedException(){
        Category category=new Category();
        category.setName("test");
        category.setImageUrl("test");

        when(categoryRepository.findCategoriesByNameAndDeletedFalse(any())).thenReturn(category);

        Errors errors=new BeanPropertyBindingResult(categoryAddBindingModel,"invalidCategory");
        categoryAddValidator.validate(categoryAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }
}
