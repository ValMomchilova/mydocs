package com.val.mydocs.unit.services;

import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.UserRepository;
import com.val.mydocs.serivce.UserRoleService;
import com.val.mydocs.serivce.UserService;
import com.val.mydocs.serivce.UserServiceImpl;
import com.val.mydocs.validation.UserValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.ConfigurationException;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    public static final String TEST_USER_NAME = "test user";

    @Mock
    private UserRepository mockRepository;

    @Mock
    private UserValidationService mockValidationService;

    @Mock
    private UserRoleService mockUserRoleService;

    @Mock
    private BCryptPasswordEncoder mockEncoder;

    private UserService service;

    @Before()
    public void init() {
        this.service = new UserServiceImpl(this.mockRepository,
                this.mockEncoder,
                this.mockUserRoleService,
                new ModelMapper(),
                this.mockValidationService);
    }

    @Test
    public void loadUserByUsernameLoadsUser(){
        User user = new User();
        user.setUsername(TEST_USER_NAME);
        when(this.mockRepository.findUserByUsername(any()))
                .thenReturn(user);

        UserDetails foundUser = this.service.loadUserByUsername(TEST_USER_NAME);

        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameLoadsUserThrowsNotFoundIfUserNameIsNotFound(){
        when(this.mockRepository.findUserByUsername(any()))
                .thenReturn(null);

        UserDetails foundUser = this.service.loadUserByUsername(TEST_USER_NAME);
    }

    @Test
    public void registerTestSaveValidAndUniqueUser() throws UniqueFieldException, ModelValidationException, ConfigurationException {
        when(mockValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.findUserByUsername(any()))
                .thenReturn(null);

        when(mockRepository.save(any()))
                .thenReturn(new User());

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USER_NAME);

        this.service.register(userServiceModel);


        verify(mockRepository)
                .save(any());
    }

    @Test(expected = ModelValidationException.class)
    public void registerTestNotValidUserThrows() throws UniqueFieldException, ModelValidationException, ConfigurationException {
        when(mockValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(Boolean.FALSE);

        UserServiceModel userServiceModel = new UserServiceModel();

        this.service.register(userServiceModel);
    }

    @Test(expected = UniqueFieldException.class)
    public void registerTestNotUniqueUserThrows() throws UniqueFieldException, ModelValidationException, ConfigurationException {
        when(mockValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(Boolean.TRUE);

        User user = new User();
        user.setId("test");
        when(mockRepository.findUserByUsername(any()))
                .thenReturn(user);

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USER_NAME);

        this.service.register(userServiceModel);
    }

    @Test
    public void findAllUserFindsAll(){

        this.service.findAllUsers();

        verify(mockRepository)
                .findAll();
    }

    @Test
    public void findAllUsersNotUsernameReturnsOtherUsers(){
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setUsername("User1");
        userList.add(user1);

        User user2 = new User();
        user2.setUsername("User2");
        userList.add(user2);

        when(mockRepository.findAll())
                .thenReturn(userList);


        List<UserServiceModel> foundUsers =  this.service.findAllUsersNotUsername("User1");

        assertEquals(1, foundUsers.size());
        assertEquals("User2", foundUsers.get(0).getUsername());
    }

    @Test
    public void findUserByIdFindsById(){

        this.service.findUserByID("test");

        verify(mockRepository)
                .findById("test");
    }

    @Test
    public void saveUserSaveValidAndUniqueUser() {
        when(mockValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(Boolean.TRUE);

        when(mockRepository.save(any()))
                .thenReturn(new User());

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(TEST_USER_NAME);

        this.service.saveUser(userServiceModel);


        verify(mockRepository)
                .save(any());
    }

    @Test
    public void saveRolesSavsUser() {
        when(mockRepository.findById(any()))
                .thenReturn(Optional.of(new User()));

        when(mockRepository.save(any()))
                .thenReturn(new User());

        UserServiceModel userServiceModel = new UserServiceModel();
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setName("Role1");
        userServiceModel.setRoles(roles);
        userServiceModel.setUsername(TEST_USER_NAME);

        this.service.saveRoles(userServiceModel);


        verify(mockRepository)
                .save(any());
    }

    @Test
    public void IsAdminReturnsTrueIfIsAdmin(){
        User user = new User();
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setName("ADMIN");
        roles.add(userRole);
        user.setRoles(roles);
        user.setUsername(TEST_USER_NAME);

        when(mockRepository.findById(any()))
                .thenReturn(Optional.of(user));

        boolean result =  this.service.isAdmin("test");

        assertEquals(true, result);
    }

    @Test
    public void IsAdminReturnsFalseIfNotAdmin(){
        User user = new User();
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setName("USER");
        roles.add(userRole);
        user.setRoles(roles);
        user.setUsername(TEST_USER_NAME);

        when(mockRepository.findById(any()))
                .thenReturn(Optional.of(user));

        boolean result =  this.service.isAdmin("test");

        assertEquals(false, result);
    }

    @Test
    public void findUserByUserNameFindsUser(){
        User user = new User();
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setName("ADMIN");
        roles.add(userRole);
        user.setRoles(roles);
        user.setUsername(TEST_USER_NAME);

        when(mockRepository.findUserByUsername(any()))
                .thenReturn(user);

        UserServiceModel userServiceModel = this.service.findUserByUserName(TEST_USER_NAME);

        assertEquals(TEST_USER_NAME, userServiceModel.getUsername());
    }
}
