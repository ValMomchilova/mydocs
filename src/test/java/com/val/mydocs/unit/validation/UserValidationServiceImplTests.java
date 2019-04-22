package com.val.mydocs.unit.validation;

import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.validation.UserValidationService;
import com.val.mydocs.validation.UserValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserValidationServiceImplTests {
    private UserValidationService userValidationService;

    @Before
    public void init(){
        this.userValidationService = new UserValidationServiceImpl();
    }

    @Test
    public void isValidReturnsTrueForValidUser(){
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("UserName");
        userServiceModel.setPassword("pass");
        userServiceModel.setEmail("email@test.com");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(true, result);
    }

    @Test
    public void isValidReturnsFalseForNullUser(){

        boolean result = this.userValidationService.isValid(null);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForUserNullUserName(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setPassword("pass");
        userServiceModel.setEmail("email@test.com");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForUsereptyUserName(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("");
        userServiceModel.setPassword("pass");
        userServiceModel.setEmail("email@test.com");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForUserNullPassword(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("username");

        userServiceModel.setEmail("email@test.com");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForUserEmptyPassword(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("username");
        userServiceModel.setPassword("");
        userServiceModel.setEmail("email@test.com");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    public void isValidReturnsFalseForUserNullEmail(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("username");
        userServiceModel.setPassword("pass");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }

    @Test
    public void isValidReturnsFalseForUserEmptyEmail(){

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("username");
        userServiceModel.setPassword("pass");
        userServiceModel.setEmail("");

        boolean result = this.userValidationService.isValid(userServiceModel);

        assertEquals(false, result);
    }
}
