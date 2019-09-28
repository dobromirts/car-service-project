package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.view.CarViewModel;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.PartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class HomeController {
    private final PartService partService;
    private final CarService carService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(PartService partService, CarService carService, ModelMapper modelMapper) {
        this.partService = partService;
        this.carService = carService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about(ModelAndView modelAndView){
        modelAndView.setViewName("about");
        return modelAndView;
    }

    @GetMapping("/contacts")
    public ModelAndView contacts(ModelAndView modelAndView){
        modelAndView.setViewName("contacts");
        return modelAndView;
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(ModelAndView modelAndView){
        List<CarViewModel> cars=this.carService.findAllCars().stream().map(c->this.modelMapper.map(c,CarViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",cars);
        modelAndView.setViewName("home");
        return modelAndView;
    }

}
