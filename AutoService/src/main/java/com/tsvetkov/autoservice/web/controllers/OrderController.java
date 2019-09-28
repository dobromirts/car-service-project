package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController{
    private final OrderService orderService;
    private  final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView allOrders(ModelAndView modelAndView){
        modelAndView.addObject("orders",this.orderService.findAllOrders());
        return view("orders/all-orders",modelAndView);
    }
}
