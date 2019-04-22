package com.val.mydocs.serivce;

import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean register(UserServiceModel userServiceModel) throws UniqueFieldException, ModelValidationException;

    List<UserServiceModel> findAllUsers();

    List<UserServiceModel> findAllUsersNotUsername(String username);

    UserServiceModel findUserByID(String id);

    boolean saveUser(UserServiceModel userServiceModel);

    boolean saveRoles(UserServiceModel userServiceModel);

    boolean isAdmin(String id);

    UserServiceModel findUserByUserName(String username);
}
