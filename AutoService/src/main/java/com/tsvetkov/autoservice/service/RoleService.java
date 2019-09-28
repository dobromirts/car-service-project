package com.tsvetkov.autoservice.service;

import com.tsvetkov.autoservice.domain.entities.Role;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;


import java.util.Set;

public interface RoleService {
    void seedRolesInDb();

    Set<RoleServiceModel> findAllAuthorities();

    RoleServiceModel findByAuthority(String name);
}
