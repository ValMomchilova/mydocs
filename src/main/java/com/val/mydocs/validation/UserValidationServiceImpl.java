package com.val.mydocs.validation;

import com.val.mydocs.domain.models.service.UserServiceModel;
import org.springframework.stereotype.Component;

@Component
public class UserValidationServiceImpl implements UserValidationService {
    @Override
    public boolean isValid(UserServiceModel user) {
        if (user == null){
            return false;
        }
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            return false;
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            return false;
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            return false;
        }

        return true;
    }
}

