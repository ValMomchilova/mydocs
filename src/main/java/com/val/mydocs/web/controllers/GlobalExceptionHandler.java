package com.val.mydocs.web.controllers;

import com.val.mydocs.exceptions.ModelValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler({Throwable.class})
    public ModelAndView handleModelValidationException(ModelValidationException e,
                                                       HttpServletRequest request) {
        e.printStackTrace();

        ModelAndView modelAndView = new ModelAndView("error");

        Locale locale = RequestContextUtils.getLocale(request);
        if (locale != null && modelAndView != null) {
            modelAndView.addObject("locale", locale);
        }

        return modelAndView;
    }
}
