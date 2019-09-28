package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.UserEditProfileBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.UserRegisterBindingModel;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;
import com.tsvetkov.autoservice.domain.models.service.UserServiceModel;
import com.tsvetkov.autoservice.domain.models.view.UserViewModel;
import com.tsvetkov.autoservice.domain.models.view.ProfileViewModel;
import com.tsvetkov.autoservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView) {
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute("model") UserRegisterBindingModel userRegisterBindingModel, ModelAndView modelAndView) {
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        this.userService.registerUser(userServiceModel);

        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(ModelAndView modelAndView, Principal principal) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        ProfileViewModel profileViewModel = this.modelMapper.map(userServiceModel, ProfileViewModel.class);
        modelAndView.addObject("model", profileViewModel);
        modelAndView.setViewName("user/profile");
        return modelAndView;
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(ModelAndView modelAndView, Principal principal, @ModelAttribute("model") UserEditProfileBindingModel userEditProfileBindingModel) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        userEditProfileBindingModel = this.modelMapper.map(userServiceModel, UserEditProfileBindingModel.class);
        modelAndView.addObject("model", userEditProfileBindingModel);
        userEditProfileBindingModel.setPassword(null);
        modelAndView.setViewName("user/profile-edit");
        return modelAndView;
    }

    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute("model") UserEditProfileBindingModel userEditProfileBindingModel, ModelAndView modelAndView) {
        if (!userEditProfileBindingModel.getPassword().equals(userEditProfileBindingModel.getConfirmPassword())) {
            modelAndView.setViewName("user/profile-edit");
        } else {
            UserServiceModel userServiceModel = this.modelMapper.map(userEditProfileBindingModel, UserServiceModel.class);
            this.userService.editUser(userServiceModel, userEditProfileBindingModel.getOldPassword());
            modelAndView.setViewName("redirect:/users/profile");
        }
        return modelAndView;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UserViewModel> userViewModel = this.userService.findAllUsers()
                .stream().map(u -> {
                    UserViewModel userViewModel1 = this.modelMapper.map(u, UserViewModel.class);
                    userViewModel1.setAuthorities(u.getAuthorities().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toSet()));
                    return userViewModel1;
                }).collect(Collectors.toList());

        modelAndView.addObject("model", userViewModel);
        modelAndView.setViewName("user/all-users");
        return modelAndView;
    }

    @PostMapping("/set-admin/{id}")
    public ModelAndView addAdmin(@PathVariable("id") String id, ModelAndView modelAndView) {
        this.userService.editAuthority(id);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    @PostMapping("/set-user/{id}")
    public ModelAndView addUser(@PathVariable("id") String id, ModelAndView modelAndView) {
        this.userService.editAuthority(id);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }
}
