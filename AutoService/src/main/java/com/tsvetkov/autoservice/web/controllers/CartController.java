package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CartViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartDetailsViewModel;
import com.tsvetkov.autoservice.service.CartService;
import com.tsvetkov.autoservice.service.OrderService;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.service.UserService;
import com.tsvetkov.autoservice.validation.cart.CartCheckoutValidator;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final PartService partService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final CartCheckoutValidator cartCheckoutValidator;
    private final CartService cartService;

    public CartController(PartService partService,ModelMapper modelMapper, OrderService orderService, CartCheckoutValidator cartCheckoutValidator, CartService cartService) {
        this.partService = partService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.cartCheckoutValidator = cartCheckoutValidator;
        this.cartService = cartService;
    }

    @PostMapping("/add-part")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addPartConfirm(String id, int quantity, HttpSession session) {
        PartDetailsViewModel part = this.modelMapper.map(this.partService.findPartById(id), PartDetailsViewModel.class);
        CartViewModel cartViewModel = new CartViewModel();
        cartViewModel.setPart(part);
        cartViewModel.setQuantity(quantity);
        List<CartViewModel> cartViewModels = this.cartService.retrieveCard(session);
        this.cartService.addPartToCart(cartViewModel, cartViewModels);
        return redirect("/home");
    }


    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session) {
        List<CartViewModel> cartViewModels = this.cartService.retrieveCard(session);

        modelAndView.addObject("totalPartsPrice", this.cartService.calculateTotalPartsPrice(cartViewModels));
        modelAndView.addObject("totalWorkPrice", this.cartService.calculateTotalWorkPrice(cartViewModels));
        modelAndView.addObject("totalPrice", this.cartService.calculateTotalPrice(cartViewModels));
        return view("cart/details", modelAndView);
    }

    @GetMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Checkout")
    public ModelAndView checkout(ModelAndView modelAndView, @ModelAttribute(name = "model") CartPersonBindingModel model) {
        modelAndView.addObject("model", model);
        return view("cart/cart-confirmation", modelAndView);
    }

    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView orderConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") CartPersonBindingModel model,
                                     Principal principal, BindingResult bindingResult, HttpSession session) {
        this.cartCheckoutValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return view("cart/cart-confirmation", modelAndView);
        }

        List<CartViewModel> cartViewModels = this.cartService.retrieveCard(session);
        OrderServiceModel orderServiceModel = this.cartService.prepareOrder(cartViewModels, principal.getName(), model);
        orderServiceModel.setFinished(false);
        this.orderService.createOrder(orderServiceModel);
        session.removeAttribute("shopping-cart");
        return redirect("/home");
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removePart(@PathVariable String id, HttpSession session) {
        this.cartService.removePartFromCart(id, this.cartService.retrieveCard(session));
        return redirect("/home");
    }
}