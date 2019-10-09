package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
    boolean registerUser(UserServiceModel userServiceModel);


    UserServiceModel findUserByUsername(String username);


    UserServiceModel editUser(UserServiceModel userServiceModel, String oldPassword);

    List<UserServiceModel> findAllUsers();

    void editAuthority(String id);


}
