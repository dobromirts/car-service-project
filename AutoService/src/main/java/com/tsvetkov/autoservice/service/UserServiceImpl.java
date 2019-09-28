package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.User;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;
import com.tsvetkov.autoservice.domain.models.service.UserServiceModel;
import com.tsvetkov.autoservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {
        this.roleService.seedRolesInDb();

        if (this.userRepository.count() == 0) {
            userServiceModel.setAuthorities(this.roleService.findAllAuthorities());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        try {
            this.userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserServiceModel login(UserServiceModel userServiceModel) {
        return null;
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No such username"));
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel editUser(UserServiceModel userServiceModel, String oldPassword) {
        User user = this.userRepository.findByUsername(userServiceModel.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No such username"));

        if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        } else {
            user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        }
        if (!user.getEmail().equals(userServiceModel.getEmail())) {
            user.setEmail(userServiceModel.getEmail());
        }
        this.userRepository.saveAndFlush(user);
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(u -> {
                    UserServiceModel userServiceModel = this.modelMapper.map(u, UserServiceModel.class);
                    userServiceModel.setAuthorities(u.getAuthorities().stream()
                            .map(a -> this.modelMapper.map(a, RoleServiceModel.class)).collect(Collectors.toSet()));
                    return userServiceModel;
                }).collect(Collectors.toList());
    }



    @Override
    public void editAuthority(String id) {
        User user=this.userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("No such username"));
        UserServiceModel userServiceModel=this.modelMapper.map(user,UserServiceModel.class);

        boolean hasAuthority=false;

        for (RoleServiceModel authority : userServiceModel.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")){
                hasAuthority=true;
            }
        }

        if (hasAuthority){
            userServiceModel.getAuthorities().clear();
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }else {
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
        }

        this.userRepository.saveAndFlush(this.modelMapper.map(userServiceModel,User.class));
    }
}
