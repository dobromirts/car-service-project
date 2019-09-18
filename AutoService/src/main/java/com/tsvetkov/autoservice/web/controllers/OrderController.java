package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.service.PartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController{
    private final PartService partService;
    private  final ModelMapper modelMapper;

    @Autowired
    public OrderController(PartService partService, ModelMapper modelMapper) {
        this.partService = partService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/details/{id}")
    public ModelAndView detailsPart(@PathVariable String id,ModelAndView modelAndView){
        modelAndView.addObject("model",this.partService.findPartById(id));
        return super.view("parts/details-part",modelAndView);
    }
}
