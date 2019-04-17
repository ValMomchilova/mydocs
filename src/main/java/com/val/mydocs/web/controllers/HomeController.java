package com.val.mydocs.web.controllers;

import com.val.mydocs.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView,
                              Principal principal) {
        if (principal != null) {
            return this.redirect("/home");
        }
        return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView,
                              Principal principal) {
        return this.view("home");
    }
}
