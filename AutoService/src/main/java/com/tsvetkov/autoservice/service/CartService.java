package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.binding.CartPersonBindingModel;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CartViewModel;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    List<CartViewModel> retrieveCard(HttpSession session);

    void addPartToCart(CartViewModel cartViewModel, List<CartViewModel> cart);

    void removePartFromCart(String id, List<CartViewModel> cart);

    BigDecimal calculateTotalPartsPrice(List<CartViewModel> cart);

    BigDecimal calculateTotalWorkPrice(List<CartViewModel> cart);

    BigDecimal calculateTotalPrice(List<CartViewModel> cart);

    OrderServiceModel prepareOrder(List<CartViewModel> cartViewModels, String username, CartPersonBindingModel cartPersonBindingModel);
}
