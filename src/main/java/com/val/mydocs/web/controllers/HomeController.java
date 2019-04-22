package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.service.SubjectServiceModel;
import com.val.mydocs.domain.models.view.SubjectDetailsViewModel;
import com.val.mydocs.serivce.SubjectService;
import com.val.mydocs.serivce.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {
    private final UserService userService;
    private final SubjectService subjectService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, SubjectService subjectService, ModelMapper modelMapper) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
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
                              Principal principal) throws AuthenticationException {

        String username = this.getPrincipalName(principal);
        List<SubjectServiceModel> subjectServiceModelList = this.subjectService.findAllSubjectsBySubjectTypeOrder(username);
        List<SubjectDetailsViewModel> subjectDetailsViewModels = subjectServiceModelList.stream()
                .map(s -> this.modelMapper.map(s, SubjectDetailsViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("subjects", subjectDetailsViewModels);

        return this.view("home", modelAndView);
    }

    private String getPrincipalName(Principal principal) throws AuthenticationException {
        if (principal == null) {
            throw new AuthenticationException();
            // to do: error
        }
        return principal.getName();
    }
}
