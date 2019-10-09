package com.tsvetkov.autoservice.web.controllers;

import com.tsvetkov.autoservice.domain.models.binding.UserEditProfileBindingModel;
import com.tsvetkov.autoservice.domain.models.binding.UserRegisterBindingModel;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;
import com.tsvetkov.autoservice.domain.models.service.UserServiceModel;
import com.tsvetkov.autoservice.domain.models.view.UserViewModel;
import com.tsvetkov.autoservice.domain.models.view.ProfileViewModel;
import com.tsvetkov.autoservice.service.UserService;
import com.tsvetkov.autoservice.validation.user.UserEditProfileValidator;
import com.tsvetkov.autoservice.validation.user.UserRegisterValidator;
import com.tsvetkov.autoservice.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRegisterValidator registerValidator;
    private final UserEditProfileValidator editProfileValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserRegisterValidator registerValidator, UserEditProfileValidator editProfileValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.registerValidator = registerValidator;
        this.editProfileValidator = editProfileValidator;
    }


    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login() {
        return view("login");
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView register(@ModelAttribute(name = "model") UserRegisterBindingModel userRegisterBindingModel, ModelAndView modelAndView) {
        modelAndView.addObject("model", userRegisterBindingModel);
        return view("register", modelAndView);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute(name = "model") UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, ModelAndView modelAndView) {
        this.registerValidator.validate(userRegisterBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            userRegisterBindingModel.setPassword(null);
            userRegisterBindingModel.setConfirmPassword(null);
            modelAndView.addObject("model", userRegisterBindingModel);
            return view("register", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        this.userService.registerUser(userServiceModel);
        return redirect("/users/login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
    public ModelAndView profile(ModelAndView modelAndView, Principal principal) {
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        ProfileViewModel profileViewModel = this.modelMapper.map(userServiceModel, ProfileViewModel.class);
        modelAndView.addObject("model", profileViewModel);
        return view("user/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView editProfile(ModelAndView modelAndView, Principal principal,
                                    @ModelAttribute(name = "model") UserEditProfileBindingModel model) {

        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        model = this.modelMapper.map(userServiceModel, UserEditProfileBindingModel.class);
        model.setPassword(null);
        modelAndView.addObject("model", model);
        return view("user/profile-edit", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(ModelAndView modelAndView,@ModelAttribute(name = "model") UserEditProfileBindingModel model,BindingResult bindingResult) {
        this.editProfileValidator.validate(model,bindingResult);

        if (bindingResult.hasErrors()){
            model.setOldPassword(null);
            model.setPassword(null);
            model.setConfirmPassword(null);
            modelAndView.addObject("model", model);
            return view("user/profile-edit", modelAndView);
        }
        UserServiceModel userServiceModel = this.modelMapper.map(model, UserServiceModel.class);
        this.userService.editUser(userServiceModel, model.getOldPassword());

        return redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UserViewModel> userViewModel = this.userService.findAllUsers()
                .stream().map(u -> {
                    UserViewModel userViewModel1 = this.modelMapper.map(u, UserViewModel.class);
                    userViewModel1.setAuthorities(u.getAuthorities().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toSet()));
                    return userViewModel1;
                }).collect(Collectors.toList());

        modelAndView.addObject("model", userViewModel);
        return view("user/all-users", modelAndView);
    }

    @PostMapping("/set-admin/{id}")
    public ModelAndView addAdmin(@PathVariable("id") String id) {
        this.userService.editAuthority(id);
        return redirect("/home");
    }

    @PostMapping("/set-user/{id}")
    public ModelAndView addUser(@PathVariable("id") String id) {
        this.userService.editAuthority(id);
        return redirect("/home");
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
