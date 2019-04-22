package com.val.mydocs.validation;

import com.val.mydocs.domain.models.service.UserServiceModel;

public interface UserValidationService {
    boolean isValid(UserServiceModel user);
}
