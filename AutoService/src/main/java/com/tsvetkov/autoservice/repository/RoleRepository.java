package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role findRoleByAuthority(String name);
}
