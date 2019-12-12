package com.tsvetkov.autoservice.unit.validation.part;

import com.tsvetkov.autoservice.domain.entities.Part;
import com.tsvetkov.autoservice.domain.models.binding.PartAddBindingModel;
import com.tsvetkov.autoservice.repository.PartRepository;
import com.tsvetkov.autoservice.validation.part.PartAddValidator;
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

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartAddValidatorTests {

    @Mock
    PartRepository partRepository;

    PartAddValidator partAddValidator;

    PartAddBindingModel partAddBindingModel;

    MultipartFile multipartFile;

    @Before
    public void init(){
        partAddValidator=new PartAddValidator(partRepository);
        multipartFile = new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());

        partAddBindingModel=new PartAddBindingModel();
        partAddBindingModel.setCarModels(new ArrayList<>());
        partAddBindingModel.setCategories(new ArrayList<>());
        partAddBindingModel.setDescription("");
        partAddBindingModel.setImage(multipartFile);
        partAddBindingModel.setName("test");
        partAddBindingModel.setPrice(BigDecimal.valueOf(100));
        partAddBindingModel.setWorkPrice(BigDecimal.valueOf(100));
    }

    @Test
    public void validate_WhenValidPart_ExpectedNoExceptions(){
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"validModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_WhenPartNameIsBellow2Symbols_ExpectedExceptions(){
        partAddBindingModel.setName("a");
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

//    @Test
//    public void validate_WhenPartNameIsOver20Symbols_ExpectedExceptions(){
//        partAddBindingModel.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
//        partAddValidator.validate(partAddBindingModel,errors);
//        Assert.assertTrue(errors.hasErrors());
//    }

    @Test
    public void validate_WhenPartAlreadyExists_ExpectedExceptions(){
        Part part=new Part();
        part.setCarModels(new ArrayList<>());
        part.setCategories(new ArrayList<>());
        part.setDescription("");
        part.setImageUrl("test");
        part.setName("test");
        part.setPrice(BigDecimal.valueOf(100));
        part.setWorkPrice(BigDecimal.valueOf(100));

        when(partRepository.findByName(any())).thenReturn(part);
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenPartPriceIsNull_ExpectedExceptions(){
        partAddBindingModel.setPrice(null);
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenWorkPriceIsNull_ExpectedExceptions(){
        partAddBindingModel.setWorkPrice(null);
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }
    @Test
    public void validate_WhenPartCategoriesAreNull_ExpectedExceptions(){
        partAddBindingModel.setCategories(null);
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void validate_WhenPartModelsAreNull_ExpectedExceptions(){
        partAddBindingModel.setCarModels(null);
        Errors errors=new BeanPropertyBindingResult(partAddBindingModel,"invalidModel");
        partAddValidator.validate(partAddBindingModel,errors);
        Assert.assertTrue(errors.hasErrors());
    }

}
