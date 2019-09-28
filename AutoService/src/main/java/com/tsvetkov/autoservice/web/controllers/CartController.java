package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CartViewModel;
import com.tsvetkov.autoservice.domain.models.view.PartDetailsViewModel;
import com.tsvetkov.autoservice.service.OrderService;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final PartService partService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    public CartController(PartService partService, UserService userService, HttpSession httpSession, ModelMapper modelMapper, OrderService orderService) {
        this.partService = partService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @PostMapping("/add-part")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addPartConfirm(String id, int quantity, ModelAndView modelAndView, HttpSession session) {
        PartDetailsViewModel part = this.modelMapper.map(this.partService.findPartById(id), PartDetailsViewModel.class);
        CartViewModel cartViewModel = new CartViewModel();
        cartViewModel.setPart(part);
        cartViewModel.setQuantity(quantity);

        List<CartViewModel> cartViewModels = this.retrieveCard(session);
        this.addPartToCart(cartViewModel, cartViewModels);

        return redirect("/home");
    }


    @GetMapping("/details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session) {
        List<CartViewModel> cartViewModels = this.retrieveCard(session);
        modelAndView.addObject("totalPrice", this.calculateTotalPrice(cartViewModels));
        return view("cart/details", modelAndView);
    }

    @GetMapping("/checkout")
    public ModelAndView checkout() {
        return view("cart/cart-confirmation");
    }

    @PostMapping("/order")
    public ModelAndView orderConfirm(@ModelAttribute CartPersonBindingModel cartPersonBindingModel, Principal principal, HttpSession session){
        List<CartViewModel> cartViewModels = this.retrieveCard(session);

        OrderServiceModel orderServiceModel=this.prepareOrder(cartViewModels,principal.getName(),cartPersonBindingModel);
        this.orderService.createOrder(orderServiceModel);

        return redirect("/home");
    }

    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }

    private List<CartViewModel> retrieveCard(HttpSession session) {
        this.initCart(session);
        return (List<CartViewModel>) session.getAttribute("shopping-cart");
    }

    private void addPartToCart(CartViewModel cartViewModel, List<CartViewModel> cart) {
        for (CartViewModel cartItem : cart) {
            if (cartItem.getPart().getId().equals(cartViewModel.getPart().getId())) {
                cartItem.setQuantity(cartViewModel.getQuantity() + cartItem.getQuantity());
                return;
            }
        }
        cart.add(cartViewModel);
    }

    private void removePartFromCart(String id, List<CartViewModel> cart) {
        cart.removeIf(ci -> ci.getPart().getId().equals(id));
    }

    private BigDecimal calculateTotalPrice(List<CartViewModel> cart) {
        BigDecimal result = new BigDecimal(0);

        for (CartViewModel cartViewModel : cart) {
            result = result.add(cartViewModel.getPart().getPrice().multiply(new BigDecimal(cartViewModel.getQuantity())));
        }
        return result;
    }

    private OrderServiceModel prepareOrder(List<CartViewModel> cartViewModels,String username,CartPersonBindingModel cartPersonBindingModel){
        OrderServiceModel orderServiceModel=this.modelMapper.map(cartPersonBindingModel,OrderServiceModel.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(cartPersonBindingModel.getDate(), formatter);

        orderServiceModel.setDate(date);

        orderServiceModel.setUser(this.userService.findUserByUsername(username));

        List<PartServiceModel> partServiceModels=new ArrayList<>();
        for (CartViewModel cartViewModel : cartViewModels) {
            PartServiceModel partServiceModel=this.modelMapper.map(cartViewModel.getPart(),PartServiceModel.class);

            for (int i = 0; i <cartViewModel.getQuantity() ; i++) {
                partServiceModels.add(partServiceModel);
            }
        }

        orderServiceModel.setParts(partServiceModels);
        orderServiceModel.setTotalPrice(this.calculateTotalPrice(cartViewModels));

        return orderServiceModel;
    }
}
