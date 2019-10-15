package com.tsvetkov.autoservice.integration;

import com.tsvetkov.autoservice.domain.entities.Part;
import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.repository.PartRepository;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.web.controllers.CartController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartControllerTests {

    @Autowired
    CartController cartController;

    @MockBean
    PartService partService;

    HttpSession httpSession;

    PartServiceModel partServiceModel;

    @Before
    public void init(){
        partServiceModel=new PartServiceModel();
        partServiceModel.setImageUrl("test");
        partServiceModel.setCategories(new ArrayList<>());
        partServiceModel.setCarModels(new ArrayList<>());
        partServiceModel.setId("test");
        partServiceModel.setDescription("");
        partServiceModel.setName("test");
        partServiceModel.setPrice(BigDecimal.valueOf(100));
        partServiceModel.setWorkPrice(BigDecimal.valueOf(100));

        httpSession=new  MockHttpSession();
    }

    @Test
    @WithMockUser
    public void addPartConfirm_WhenCorrectId_ExpectedCorrectRedirect(){
        when(partService.findPartById(any())).thenReturn(partServiceModel);

        ModelAndView modelAndView = cartController.addPartConfirm("test", 1, httpSession);

        Assert.assertEquals("redirect:/home",modelAndView.getViewName());
    }

    @Test
    @WithMockUser
    public void cartDetails_When1PartAdded_ExpectedCorrectValues(){
        when(partService.findPartById(any())).thenReturn(partServiceModel);

        cartController.addPartConfirm("test", 1, httpSession);

        ModelAndView modelAndView = cartController.cartDetails(new ModelAndView(), httpSession);
        Map<String, Object> model = modelAndView.getModel();
        BigDecimal totalPartsPrice = (BigDecimal) model.get("totalPartsPrice");
        BigDecimal totalWorkPrice = (BigDecimal) model.get("totalWorkPrice");
        BigDecimal totalPrice = (BigDecimal) model.get("totalPrice");


        Assert.assertEquals(partServiceModel.getWorkPrice(),totalWorkPrice);
        Assert.assertEquals(partServiceModel.getPrice().add(partServiceModel.getWorkPrice()),totalPrice);
    }

    @Test
    @WithMockUser
    public void checkout_ExpectedCorrectView(){
        when(partService.findPartById(any())).thenReturn(partServiceModel);
        CartPersonBindingModel cartPersonBindingModel=new CartPersonBindingModel();
        cartPersonBindingModel.setPhoneNumber("test");
        cartPersonBindingModel.setPersonName("test");
        cartPersonBindingModel.setDate("12/02/2019");
        cartPersonBindingModel.setDescription("test");

        ModelAndView modelAndView = cartController.checkout(new ModelAndView(),cartPersonBindingModel);

        Assert.assertEquals("cart/cart-confirmation",modelAndView.getViewName());
    }

    @Test
    @WithMockUser
    public void removePart_When1Part_ExpectedPartRemoved(){
        when(partService.findPartById(any())).thenReturn(partServiceModel);

        cartController.addPartConfirm("test", 1, httpSession);


        ModelAndView modelAndView = cartController.removePart("test", httpSession);
        LinkedList attribute = (LinkedList) httpSession.getAttribute("shopping-cart");

        Assert.assertEquals(0,attribute.size());
    }



}
