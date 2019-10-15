package com.tsvetkov.autoservice.integration;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.binding.CategoryAddBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.CategoryEditBindingModel;
import com.tsvetkov.autoservice.repository.CategoryRepository;
import com.tsvetkov.autoservice.service.CloudinaryService;
import com.tsvetkov.autoservice.web.controllers.CategoryController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryControllerTests {

    @Autowired
    CategoryController categoryController;

    @Autowired
    CategoryRepository categoryRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    BindingResult bindingResult;

    MultipartFile testFile;

    CategoryAddBindingModel categoryAddBindingModel;

    CategoryEditBindingModel categoryEditBindingModel;

    Category category;

    @Before
    public void init(){
        categoryRepository.deleteAll();
        testFile= new MockMultipartFile("data", "filename.txt",
                "text/plain", "some xml".getBytes());
        categoryAddBindingModel=new CategoryAddBindingModel();
        categoryAddBindingModel.setName("test");
        categoryAddBindingModel.setImage(testFile);
        bindingResult=new BeanPropertyBindingResult(categoryAddBindingModel,"test");

        categoryEditBindingModel=new CategoryEditBindingModel();
        categoryEditBindingModel.setName("test");


        category=new Category();
        category.setName("test");
        category.setImageUrl("test");

    }
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategory_ReturnsCorrectView(){
        ModelAndView modelAndView = categoryController.addCategory(new ModelAndView(), categoryAddBindingModel);

        Assert.assertEquals("category/add-categories",modelAndView.getViewName());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategoryConfirm_WhenValidCategory_ExpectedSavedCategory() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");

        categoryController.addCategoryConfirm(new ModelAndView(),categoryAddBindingModel,bindingResult);

        Assert.assertEquals(1,categoryRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategoryConfirm_WhenNameIsLowerThan3Symbols_ExpectedNoCategorySaved() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        categoryAddBindingModel.setName("a");

        categoryController.addCategoryConfirm(new ModelAndView(),categoryAddBindingModel,bindingResult);

        Assert.assertEquals(0,categoryRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategoryConfirm_WhenNameIsBiggerThan15Symbols_ExpectedNoCategorySaved() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        categoryAddBindingModel.setName("aaaaaaaaaaaaaaaaaaaaaaaaa");

        categoryController.addCategoryConfirm(new ModelAndView(),categoryAddBindingModel,bindingResult);

        Assert.assertEquals(0,categoryRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategoryConfirm_WhenNameAlreadyExists_ExpectedNoCategorySaved() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        category.setDeleted(false);
        categoryRepository.save(category);

        categoryController.addCategoryConfirm(new ModelAndView(),categoryAddBindingModel,bindingResult);

        Assert.assertEquals(1,categoryRepository.count());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void addCategoryConfirm_WhenNameAlreadyExists_ExpectedCorrectView() throws IOException {
        when(cloudinaryService.uploadImage(any())).thenReturn("test");
        categoryAddBindingModel.setName("a");

        ModelAndView modelAndView = categoryController.addCategoryConfirm(new ModelAndView(), categoryAddBindingModel, bindingResult);

        Assert.assertEquals("category/add-categories",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void allCategories_ReturnsCorrectView(){
        ModelAndView modelAndView = categoryController.allCategories(new ModelAndView());

        Assert.assertEquals("/category/all-categories",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCategory_ReturnsCorrectView(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);

        ModelAndView modelAndView = categoryController.editCategory(savedCategory.getId(),new ModelAndView(),categoryEditBindingModel);

        Assert.assertEquals("category/edit-category",modelAndView.getViewName());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCategoryConfirm_WhenValidCategory_ExpectedCorrectRedirect(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);
        categoryEditBindingModel.setName("test1");

        ModelAndView modelAndView = categoryController
                .editCategoryConfirm(savedCategory.getId(),new ModelAndView(),categoryEditBindingModel,bindingResult);

        Assert.assertEquals("redirect:/categories/all",modelAndView.getViewName());
    }


    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void editCategoryConfirm_WhenInvalidCategory_CorrectViewName(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);
        categoryEditBindingModel.setName("a");

        ModelAndView modelAndView = categoryController
                .editCategoryConfirm(savedCategory.getId(),new ModelAndView(),categoryEditBindingModel,bindingResult);

        Assert.assertEquals("category/edit-category",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCategory_ReturnsCorrectView(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);
        ModelAndView modelAndView = categoryController.deleteCategory(savedCategory.getId(),new ModelAndView());

        Assert.assertEquals("/category/delete-category",modelAndView.getViewName());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCategory_WhenInvalidId_Throw(){
        ModelAndView modelAndView = categoryController.deleteCategory("id",new ModelAndView());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCategoryConfirm_WhenValidId_ExpectedCorrectRedirect(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);
        ModelAndView modelAndView = categoryController.deleteCategoryConfirm(savedCategory.getId(),new ModelAndView());

        Assert.assertEquals("redirect:/categories/all",modelAndView.getViewName());
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCategoryConfirm_WhenValidId_ExpectedDeletedCategory(){
        category.setDeleted(false);
        Category savedCategory = categoryRepository.save(category);
        ModelAndView modelAndView = categoryController.deleteCategoryConfirm(savedCategory.getId(),new ModelAndView());

        Assert.assertEquals(0,categoryRepository.findAllByDeletedFalse().size());
    }

    @Test(expected = Exception.class)
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void deleteCategoryConfirm_WhenInvalidId_Throw(){
        ModelAndView modelAndView = categoryController.deleteCategoryConfirm("id",new ModelAndView());
    }


}
