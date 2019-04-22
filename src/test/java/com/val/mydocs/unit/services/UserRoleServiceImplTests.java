package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.repository.UserRoleRepository;
import com.val.mydocs.serivce.UserRoleService;
import com.val.mydocs.serivce.UserRoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRoleServiceImplTests {
    @Mock
    private UserRoleRepository mockRepository;

    private UserRoleService service;

    @Before()
    public void init(){
        this.service = new UserRoleServiceImpl(this.mockRepository, new ModelMapper());
    }

    @Test
    public void getUserRoleByNameGetsUserRole(){
        when(this.mockRepository.findUserRoleByName(any()))
                .thenReturn(new UserRole());

        this.service.getUserRoleByName("TestUser");

        verify(mockRepository)
                .findUserRoleByName(any());
    }

    @Test
    public void findAllFindsAll(){
        this.service.findAll();

        verify(mockRepository)
                .findAll();
    }
}
