package com.tsvetkov.autoservice.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeController {

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
        modelAndView.setViewName("home");
        return modelAndView;
    }

}
