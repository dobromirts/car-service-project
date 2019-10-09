package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

    void createOrder(OrderServiceModel orderServiceModel);

    void updateOrder(OrderServiceModel orderServiceModel);

    List<OrderServiceModel> findAllNewOrders();

    OrderServiceModel findOrderById(String id);

    List<OrderServiceModel> findAllConfirmedOrders();

    void deleteOrder(String id);

    void finishOrder(String id);

    List<OrderServiceModel> findAllFinishedOrders();

    List<OrderServiceModel> findAllUserOrders(String username);
}
