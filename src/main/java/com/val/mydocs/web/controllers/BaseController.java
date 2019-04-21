package com.val.mydocs.web.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

public abstract class BaseController {

    protected ModelAndView view(String view, ModelAndView modelAndView){
        modelAndView.setViewName(view);

        return modelAndView;
    }

    protected ModelAndView view(String view){
        return this.view(view, new ModelAndView());
    }

    protected ModelAndView redirect(String url){
        return this.view("redirect:" + url);
    }

    protected void addGlobalErrorsToModelAndView(String errorsObjectName, ModelAndView modelAndView, BindingResult bindingResult){
        modelAndView.addObject(errorsObjectName, bindingResult.getGlobalErrors()
                .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
    }
}
