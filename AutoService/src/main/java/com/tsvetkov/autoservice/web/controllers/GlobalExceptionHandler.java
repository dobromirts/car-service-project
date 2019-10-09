package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.error.PartNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController{

    @ExceptionHandler(Throwable.class)
    public ModelAndView globalExceptionHandler(Throwable ex){
        ModelAndView modelAndView=new ModelAndView();
        return view("error",modelAndView);
    }

    @ExceptionHandler(PartNotFoundException.class)
    public ModelAndView handlePartNotFound(Throwable e){
        ModelAndView modelAndView=new ModelAndView("error/part-error");
        modelAndView.addObject("message",e.getMessage());
        return modelAndView;
    }

}
