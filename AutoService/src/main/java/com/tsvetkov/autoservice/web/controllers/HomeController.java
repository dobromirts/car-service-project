package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.view.CarViewModel;
import com.tsvetkov.autoservice.service.CarService;
import com.tsvetkov.autoservice.service.PartService;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class HomeController extends BaseController{
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
    @PageTitle("Index")
    public ModelAndView index(){
        return view("index");
    }

    @GetMapping("/about")
    @PageTitle("About")
    public ModelAndView about(){
        return view("about");
    }

    @GetMapping("/contacts")
    @PageTitle("Contacts")
    public ModelAndView contacts(){
        return view("contacts");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView home(ModelAndView modelAndView){
        List<CarViewModel> cars=this.carService.findAllCars().stream().map(c->this.modelMapper.map(c,CarViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("model",cars);
        return view("home",modelAndView);
    }

}
