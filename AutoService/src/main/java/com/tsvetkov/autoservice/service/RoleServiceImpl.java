package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Role;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;
import com.tsvetkov.autoservice.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRolesInDb() {
        if (this.roleRepository.count()==0){
            this.roleRepository.save(new Role("ROLE_ROOT"));
            this.roleRepository.save(new Role("ROLE_ADMIN"));
            this.roleRepository.save(new Role("ROLE_USER"));
        }
    }

    @Override
    public Set<RoleServiceModel> findAllAuthorities() {
        return this.roleRepository.findAll().stream().map(r->this.modelMapper.map(r,RoleServiceModel.class)).collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String name) {
        return this.modelMapper.map(this.roleRepository.findRoleByAuthority(name),RoleServiceModel.class);
    }
}
