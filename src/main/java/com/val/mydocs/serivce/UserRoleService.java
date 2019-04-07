package com.val.mydocs.serivce;



import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.domain.models.service.UserRoleSerivceModel;

import java.util.List;

public interface UserRoleService {
    UserRole getUserRoleByName(String name);

    List<UserRoleSerivceModel> findAll();
}
