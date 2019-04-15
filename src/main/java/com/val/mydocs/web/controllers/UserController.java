package com.val.mydocs.web.controllers;

import com.val.mydocs.domain.models.binding.UserBindingModel;
import com.val.mydocs.domain.models.binding.UserEditBindingModel;
import com.val.mydocs.domain.models.binding.UserRoleBindingModel;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.domain.models.view.UsersListViewModel;
import com.val.mydocs.serivce.UserRoleService;
import com.val.mydocs.serivce.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController extends BaseController{
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, ModelAndView modelAndView){
        if(error != null){
            System.out.println(error);
        }

        return this.view("login");
    }

    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute(name = "user") UserBindingModel user, ModelAndView modelAndView){
        return this.view("register");
    }

    @PostMapping("/register")
    //Order of parameters is important!
    public ModelAndView registerPost(@Valid @ModelAttribute(name = "user") UserBindingModel user,
                                     BindingResult bindingResult,
                                     ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("globalErrors", bindingResult.getGlobalErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList()));

            //modelAndView.addObject("error", err.)
            return this.view("register", modelAndView);
        }
        this.userService.register(this.modelMapper.map(user, UserServiceModel.class));
        return this.redirect("/login");
    }

    //@PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/users/show")
    public ModelAndView showUsers(ModelAndView modelAndView, Principal principal){
        List<UsersListViewModel> users = this.userService.findAllUsersNotUsername(principal.getName())
                .stream().map(v -> this.modelMapper.map(v, UsersListViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("users",users);
        return this.view("users-list", modelAndView);
    }

    @GetMapping("/user/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") String id,
                             ModelAndView modelAndView) {
        this.prepareUserModelAndView(id, modelAndView);
        modelAndView.addObject("mode", "edit");
        return modelAndView;
    }

    @PostMapping("/user/edit/{id}")
    public ModelAndView editConfirm(@PathVariable(name = "id") String id,
                                    @Valid @ModelAttribute(name = "userModel") UserEditBindingModel userModel,
                                    BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            this.addUserModels(modelAndView);
            return this.view("edit-user", modelAndView);
        }

        boolean isOldAdminRole = this.userService.isAdmin(id);

        // If is already admin, roles cannot be edited
        if (!isOldAdminRole) {
            userModel.setRolesByRolesIds();
            this.userService.saveRoles(this.modelMapper.map(userModel, UserServiceModel.class));
        }

        return this.redirect("/users/show");
    }

    private void addUserModels(ModelAndView modelAndView) {
        List<UserRoleBindingModel> roles = this.userRoleService.findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, UserRoleBindingModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("roles", roles);
    }

    private ModelAndView prepareUserModelAndView(String id, ModelAndView modelAndView){
        UserServiceModel userServiceModel = this.userService.findUserByID(id);
        UserEditBindingModel userModel = this.modelMapper.map(userServiceModel, UserEditBindingModel.class);
        userModel.setRolesIdsByRoles();
        this.addUserModels(modelAndView);
        modelAndView.addObject("userModel", userModel);
        return this.view("edit-user", modelAndView);
    }

}
