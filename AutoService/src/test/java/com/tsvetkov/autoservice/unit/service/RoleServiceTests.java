package com.tsvetkov.autoservice.unit.service;

import com.tsvetkov.autoservice.domain.entities.Role;
import com.tsvetkov.autoservice.domain.models.service.RoleServiceModel;
import com.tsvetkov.autoservice.repository.RoleRepository;
import com.tsvetkov.autoservice.service.RoleService;
import com.tsvetkov.autoservice.service.RoleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTests {
    @Mock
    RoleRepository roleRepository;
    ModelMapper modelMapper;
    RoleService roleService;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        roleService = new RoleServiceImpl(roleRepository, modelMapper);
    }

    @Test
    public void seedRolesInDb_when0cont_addRoles() {
        Role role = new Role();
        role.setAuthority("role");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        roleService.seedRolesInDb();
        verify(roleRepository, times(3)).save(any(Role.class));
    }

    @Test
    public void seedRolesInDb_whenCountBiggerThan0_noRolesAdded() {
        Role role = new Role();
        role.setAuthority("role");
        lenient().when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleRepository.count()).thenReturn(2L);

        roleService.seedRolesInDb();
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    public void findAllAuthorities_when2Authorities_expected2Authorities() {
        Role one = new Role();
        one.setId("1");
        one.setAuthority("1");

        Role two = new Role();
        two.setId("2");
        two.setAuthority("1");

        when(roleRepository.findAll()).thenReturn(List.of(one, two));
        Set<RoleServiceModel> actual = roleService.findAllAuthorities();

        Assert.assertEquals(2, actual.size());
    }

    @Test
    public void findAllAuthorities_when0Authorities_expectedEmptyList() {
        when(roleRepository.findAll()).thenReturn(List.of());
        Set<RoleServiceModel> actual = roleService.findAllAuthorities();
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void findByAuthority_whenValidName_expectedAuthority() {
        Role expected = new Role();
        expected.setId("1");
        expected.setAuthority("1");

        when(roleRepository.findRoleByAuthority("1"))
                .thenReturn(expected);

        RoleServiceModel actual = roleService.findByAuthority("1");

        Assert.assertEquals(expected.getAuthority(), actual.getAuthority());
        Assert.assertEquals(expected.getId(), actual.getId());
    }

    @Test(expected = Exception.class)
    public void findByAuthority_whenInvalidName_throwsException() {
        when(roleRepository.findRoleByAuthority("1")).thenThrow(Exception.class);
    }


}
