package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

    void createOrder(OrderServiceModel orderServiceModel);

    List<OrderServiceModel> findAllOrders();
}
