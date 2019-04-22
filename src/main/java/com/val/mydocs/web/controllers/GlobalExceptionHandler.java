package com.val.mydocs.web.controllers;

import com.val.mydocs.exceptions.ModelValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler({ModelValidationException.class})
    public ModelAndView handleModelValidationException(ModelValidationException e,
                                                       HttpServletRequest request) {
        e.printStackTrace();

        ModelAndView modelAndView = new ModelAndView("error");
        addLangToModelAndView(request, modelAndView);

        return modelAndView;
    }

    @ExceptionHandler({AuthenticationException.class})
    public ModelAndView handleAuthenticationException(AuthenticationException e,
                                                       HttpServletRequest request) {
        e.printStackTrace();

        ModelAndView modelAndView = new ModelAndView("error/403");
        addLangToModelAndView(request, modelAndView);

        return modelAndView;
    }

    private void addLangToModelAndView(HttpServletRequest request, ModelAndView modelAndView) {
        Locale locale = RequestContextUtils.getLocale(request);
        if (locale != null && modelAndView != null) {
            modelAndView.addObject("locale", locale);
        }
    }
}
