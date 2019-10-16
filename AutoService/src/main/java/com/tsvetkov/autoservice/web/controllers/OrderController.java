package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.OrderConfirmBindingModel;
import com.tsvetkov.autoservice.domain.models.service.OrderServiceModel;
import com.tsvetkov.autoservice.domain.models.view.OrderViewModel;
import com.tsvetkov.autoservice.service.OrderService;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("New Orders")
    public ModelAndView allOrders(ModelAndView modelAndView){
        modelAndView.addObject("orders",this.orderService.findAllNewOrders());
        return view("orders/new-orders",modelAndView);
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Confirm Order")
    public ModelAndView confirmOrder(@PathVariable String id, ModelAndView modelAndView){
        OrderServiceModel orderServiceModel=this.orderService.findOrderById(id);

        modelAndView.addObject("model",orderServiceModel);
        return view("orders/confirm-order",modelAndView);
    }

    @PostMapping("/confirm/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView confirmOrderConfirm(@PathVariable String id, @ModelAttribute OrderConfirmBindingModel orderConfirmBindingModel, ModelAndView modelAndView){
        OrderServiceModel orderServiceModel=this.orderService.findOrderById(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(orderConfirmBindingModel.getDate(), formatter);

        orderServiceModel.setDate(date);
        this.orderService.updateOrder(orderServiceModel);


        return redirect("/home");
    }

    @GetMapping("/confirmed")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Confirmed Orders")
    public ModelAndView allConfirmedOrders(ModelAndView modelAndView){
        modelAndView.addObject("orders",this.orderService.findAllConfirmedOrders());
        return view("orders/all-confirmed-orders",modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete Order")
    public  ModelAndView deleteOrder(@PathVariable String id,ModelAndView modelAndView){
        OrderViewModel orderViewModel=this.modelMapper.map(this.orderService.findOrderById(id),OrderViewModel.class);
        modelAndView.addObject("model",orderViewModel);
        modelAndView.addObject("modelId",id);
        return view("orders/delete-order",modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  ModelAndView deleteOrderConfirm(@PathVariable String id){
        this.orderService.deleteOrder(id);
        return redirect("/orders/all");
    }

    @PostMapping("/finish/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  ModelAndView finishOrderConfirm(@PathVariable String id){
        this.orderService.finishOrder(id);
        return redirect("/orders/finished");
    }

    @GetMapping("/finished")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Finished Orders")
    public ModelAndView allFinishedOrders(ModelAndView modelAndView){
        modelAndView.addObject("orders",this.orderService.findAllFinishedOrders());
        return view("orders/finished-orders",modelAndView);
    }

    @GetMapping("/myorders")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My Orders")
    public ModelAndView userOrders(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("orders",this.orderService.findAllUserOrders(principal.getName()));
        return view("orders/user-orders",modelAndView);
    }


    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Confirmed Orders")
    public ModelAndView orderDetails(@PathVariable String id,ModelAndView modelAndView){
        OrderServiceModel orderServiceModel=this.orderService.findOrderById(id);
        modelAndView.addObject("model",orderServiceModel);
        return view("orders/details-order",modelAndView);
    }

    @GetMapping("/myorder/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Confirmed Orders")
    public ModelAndView userOrderDetails(@PathVariable String id,ModelAndView modelAndView){
        OrderServiceModel orderServiceModel=this.orderService.findOrderById(id);
        modelAndView.addObject("model",orderServiceModel);
        return view("orders/user-order-details",modelAndView);
    }



}
