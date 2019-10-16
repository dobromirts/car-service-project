package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Order;
import com.tsvetkov.autoservice.domain.entities.User;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, UserService userService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public void createOrder(OrderServiceModel orderServiceModel) {
        orderServiceModel.setMadeOn(LocalDateTime.now());
        orderServiceModel.setConfirmed(false);

        Order order = this.orderRepository.saveAndFlush(this.modelMapper.map(orderServiceModel, Order.class));
        System.out.println();
    }

    @Override
    public void updateOrder(OrderServiceModel orderServiceModel) {
        orderServiceModel.setMadeOn(LocalDateTime.now());
        orderServiceModel.setConfirmed(true);

        this.orderRepository.saveAndFlush(this.modelMapper.map(orderServiceModel, Order.class));
    }

    @Override
    public List<OrderServiceModel> findAllNewOrders() {
        return this.orderRepository.findAllByConfirmedFalse().stream()
                .map(o->this.modelMapper.map(o,OrderServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel findOrderById(String id) {
        Order order=this.orderRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid order"));
        return this.modelMapper.map(order,OrderServiceModel.class);
    }

    @Override
    public List<OrderServiceModel> findAllConfirmedOrders() {
        return this.orderRepository.findAllByConfirmedTrueAndFinishedFalseOrderByDate().stream()
                .map(o->this.modelMapper.map(o,OrderServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(String id) {
        this.orderRepository.deleteById(id);
    }

    @Override
    public void finishOrder(String id) {
        Order order=this.orderRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid order!"));
        order.setFinished(true);
        orderRepository.save(order);
    }

    @Override
    public List<OrderServiceModel> findAllFinishedOrders() {

        return this.orderRepository.findAllByFinishedTrue().stream()
                .map(o->this.modelMapper.map(o,OrderServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> findAllUserOrders(String username) {
        User user=this.modelMapper.map(this.userService.findUserByUsername(username),User.class);

        return this.orderRepository.findAllByUserAndConfirmedTrueAndFinishedFalse(user).stream()
                .map(o->this.modelMapper.map(o,OrderServiceModel.class)).collect(Collectors.toList());
    }
}
