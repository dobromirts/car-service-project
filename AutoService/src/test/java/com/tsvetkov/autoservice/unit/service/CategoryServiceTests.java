package com.tsvetkov.autoservice.unit.service;


import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.service.CategoryService;
import com.tsvetkov.autoservice.service.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTests {


      CategoryRepository categoryRepository;
      CategoryService categoryService;
      ModelMapper modelMapper;

      @Before
      public void setup(){
          categoryRepository=Mockito.mock(CategoryRepository.class);
          modelMapper=new ModelMapper();
          categoryService=new CategoryServiceImpl(categoryRepository,modelMapper);
      }

    @Test
    public void addCategory_whenValidCategory_expectedCarServiceModel() {
        CategoryServiceModel toBeSaved=new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName("test");
        toBeSaved.setImageUrl("test");

        Category category=new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");


        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category expected=categoryRepository.save(category);

        CategoryServiceModel actual=categoryService.addCategory(toBeSaved);
        //assert
        Assert.assertEquals(expected.getId(),actual.getId());
        Assert.assertEquals(expected.getName(),actual.getName());
        Assert.assertEquals(expected.getImageUrl(),actual.getImageUrl());
    }

    @Test(expected = Exception.class)
    public void addCategory_withNullName_throwsException() {
        CategoryServiceModel toBeSaved=new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName(null);
        toBeSaved.setImageUrl("test");
        CategoryServiceModel actual=categoryService.addCategory(toBeSaved);
    }

    @Test(expected = Exception.class)
    public void addCategory_withNullUrl_throwsException() {
        CategoryServiceModel toBeSaved=new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName("test");
        toBeSaved.setImageUrl(null);
        CategoryServiceModel actual=categoryService.addCategory(toBeSaved);
    }

    @Test
    public void findAllCategories_when2Categories_expected2Categories() {
        //arrange
        when(categoryRepository.findAllByDeletedFalse())
                .thenReturn(List.of(
                        new Category() {{
                            setId("1");
                            setName("first");
                            setImageUrl("first");
                        }},
                        new Category() {{
                            setId("2");
                            setName("second");
                            setImageUrl("second");
                        }}
                ));
        //act
        List<CategoryServiceModel> allCategories = categoryService.findAllCategories();
        //assert
        Assert.assertEquals(2,allCategories.size());
    }

    @Test
    public void findAllCategories_when0Categories_expectedEmptyList() {
        //arrange
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        ModelMapper modelMapper = new ModelMapper();

        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, modelMapper);
        when(categoryRepository.findAllByDeletedFalse())
                .thenReturn(List.of());
        //act
        List<CategoryServiceModel> allCategories = categoryService.findAllCategories();
        //assert
        Assert.assertEquals(0,allCategories.size());
    }
}
