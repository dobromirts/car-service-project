package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.domain.models.service.PartServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CartViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public CartServiceImpl(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }


    public List<CartViewModel> retrieveCard(HttpSession session) {
        this.initCart(session);
        return (List<CartViewModel>) session.getAttribute("shopping-cart");
    }


    public void addPartToCart(CartViewModel cartViewModel, List<CartViewModel> cart) {
        for (CartViewModel cartItem : cart) {
            if (cartItem.getPart().getId().equals(cartViewModel.getPart().getId())) {
                cartItem.setQuantity(cartViewModel.getQuantity() + cartItem.getQuantity());
                return;
            }
        }
        cart.add(cartViewModel);
    }


    public void removePartFromCart(String id, List<CartViewModel> cart) {
        cart.removeIf(ci -> ci.getPart().getId().equals(id));
    }


    public BigDecimal calculateTotalPartsPrice(List<CartViewModel> cart) {
        BigDecimal result = new BigDecimal(0);

        for (CartViewModel cartViewModel : cart) {
            result = result.add(cartViewModel.getPart().getPrice().multiply(new BigDecimal(cartViewModel.getQuantity())));
        }
        return result;
    }


    public BigDecimal calculateTotalWorkPrice(List<CartViewModel> cart) {
        BigDecimal result = new BigDecimal(0);

        for (CartViewModel cartViewModel : cart) {
            result = result.add(cartViewModel.getPart().getWorkPrice().multiply(new BigDecimal(cartViewModel.getQuantity())));
        }
        return result;
    }


    public BigDecimal calculateTotalPrice(List<CartViewModel> cart) {
        return calculateTotalPartsPrice(cart).add(calculateTotalWorkPrice(cart));
    }


    public OrderServiceModel prepareOrder(List<CartViewModel> cartViewModels, String username, CartPersonBindingModel cartPersonBindingModel) {
        OrderServiceModel orderServiceModel=this.modelMapper.map(cartPersonBindingModel,OrderServiceModel.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(cartPersonBindingModel.getDate(), formatter);

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
        orderServiceModel.setTotalPartsPrice(this.calculateTotalPartsPrice(cartViewModels));
        orderServiceModel.setTotalWorkPrice(this.calculateTotalWorkPrice(cartViewModels));
        orderServiceModel.setTotalPrice(this.calculateTotalPrice(cartViewModels));

        return orderServiceModel;
    }
}
