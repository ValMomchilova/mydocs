package com.val.mydocs.web.controllers;

import com.val.mydocs.exceptions.UniqueFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.stream.Collectors;

public abstract class BaseController {
    @Autowired
    private MessageSource messageSource;

    protected BaseController() {
    }

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

    protected void addErrorUniqueErrorToBindingResult(BindingResult bindingResult,
                                                      HttpServletRequest request,
                                                      UniqueFieldException e) {
        String message =  this.messageSource.getMessage("error.must.be.unique",
                null,
                RequestContextUtils.getLocale(request));

        String formattedMessage = MessageFormat.format(message, e.getFieldName());
        bindingResult.addError(new FieldError(e.getEntityName(),
                e.getFieldName(),
                formattedMessage));
    }
}
