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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTests {
    @Mock
    CategoryRepository categoryRepository;

    CategoryService categoryService;
    ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper);
    }

    @Test
    public void addCategory_whenValidCategory_expectedCarServiceModel() {
        CategoryServiceModel toBeSaved = new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName("test");
        toBeSaved.setImageUrl("test");


        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");
        category.setDeleted(false);


        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category expected = categoryRepository.save(category);

        CategoryServiceModel actual = categoryService.addCategory(toBeSaved);
        //assert
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
        Assert.assertEquals(expected.getDeleted(), actual.getDeleted());
    }

    @Test(expected = Exception.class)
    public void addCategory_withNullName_throwsException() {
        CategoryServiceModel toBeSaved = new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName(null);
        toBeSaved.setImageUrl("test");
        CategoryServiceModel actual = categoryService.addCategory(toBeSaved);
    }

    @Test(expected = Exception.class)
    public void addCategory_withNullUrl_throwsException() {
        CategoryServiceModel toBeSaved = new CategoryServiceModel();
        toBeSaved.setId("1");
        toBeSaved.setName("test");
        toBeSaved.setImageUrl(null);
        CategoryServiceModel actual = categoryService.addCategory(toBeSaved);
    }

    @Test
    public void findAllCategories_when2Categories_expected2Categories() {
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

        List<CategoryServiceModel> allCategories = categoryService.findAllCategories();

        Assert.assertEquals(2, allCategories.size());
    }

    @Test
    public void findAllCategories_when0Categories_expectedEmptyList() {
        when(categoryRepository.findAllByDeletedFalse())
                .thenReturn(List.of());
        //act
        List<CategoryServiceModel> allCategories = categoryService.findAllCategories();
        //assert
        Assert.assertEquals(0, allCategories.size());
    }

    @Test
    public void findCategoryById_whenValidCategory_expectedCategory() {
        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");

        when(categoryRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(category));
        //act
        CategoryServiceModel actual = categoryService.findCategoryById("1");
        //assert
        Assert.assertEquals(category.getId(), actual.getId());
        Assert.assertEquals(category.getName(), actual.getName());
        Assert.assertEquals(category.getImageUrl(), actual.getImageUrl());
    }

    @Test(expected = Exception.class)
    public void findCategoryById_whenInvalidId_throwsException() {
        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");
        category.setDeleted(false);

        lenient().when(categoryRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(category));

        categoryService.findCategoryById("2");
    }

    @Test
    public void editCategory_whenValidCategory_expectedEditedCategory() {
        CategoryServiceModel toBeEdit = new CategoryServiceModel();
        toBeEdit.setId("1");
        toBeEdit.setName("test1");
        toBeEdit.setImageUrl("test");

        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");

        Category expected = new Category();
        category.setId("1");
        category.setName("test1");
        category.setImageUrl("test");

        when(categoryRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(category));

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(expected);

        CategoryServiceModel actual = categoryService.editCategory("1", toBeEdit);

        Assert.assertEquals(expected.getName(), actual.getName());

    }

    @Test(expected = Exception.class)
    public void editCategory_whenInvalidId_throwsException() {
        CategoryServiceModel toBeEdit = new CategoryServiceModel();
        toBeEdit.setId("invalid");
        toBeEdit.setName("test1");
        toBeEdit.setImageUrl("test");

        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");

        Category expected = new Category();
        category.setId("1");
        category.setName("test1");
        category.setImageUrl("test");

        lenient().when(categoryRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(category));

        lenient().when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(expected);

        CategoryServiceModel actual = categoryService.editCategory("invalid", toBeEdit);
    }

    @Test
    public void deleteCategory_whenValidCategory_expectedDeletedCategory() {
        Category category = new Category();
        category.setId("1");
        category.setName("test");
        category.setImageUrl("test");
        category.setDeleted(false);

        Category expected = new Category();
        expected.setId("1");
        expected.setName("test1");
        expected.setImageUrl("test");
        expected.setDeleted(true);

        when(categoryRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(category));

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(expected);

        CategoryServiceModel actual = categoryService.deleteCategory("1");

        Assert.assertEquals(expected.getDeleted(), actual.getDeleted());
    }

    @Test(expected = Exception.class)
    public void deleteCategory_whenInvalidCategory_throwsException() {
        CategoryServiceModel actual = categoryService.deleteCategory("2");
    }

    @Test
    public void findCategoryByName_whenValidName_expectedCategory() {
        Category expected = new Category();
        expected.setId("1");
        expected.setName("test");
        expected.setImageUrl("test");
        expected.setDeleted(false);

        when(categoryRepository.findCategoriesByNameAndDeletedFalse("1"))
                .thenReturn(expected);

        CategoryServiceModel actual = categoryService.findCategoryByName("1");

        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
    }

    @Test(expected = Exception.class)
    public void findCategoryByName_whenInvalidName_throwsException() {
        CategoryServiceModel actual = categoryService.findCategoryByName("2");
    }
}
