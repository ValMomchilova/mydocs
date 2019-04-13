package com.val.mydocs.web.controllers;

import com.val.mydocs.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Locale;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView,
                              Principal principal,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) {
        if (principal != null) {
//            String userName = principal.getName();
//            String lang = null;
//            if (userName != null) {
//                lang = this.userService.getUserLangByName(userName);
//                if (lang != null && !lang.isEmpty()) {
//                    String defaultLang = Locale.US.getLanguage();
//                    String attribute = "lang";
//                    if (!lang.equals(defaultLang) && request.getParameter("lang") == null) {
//                        redirectAttributes.addAttribute(attribute, lang);
//                        return this.redirect("/");
//                    }
//                }
//            }
        }
        return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        return this.view("index");
    }
}
