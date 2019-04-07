package com.val.mydocs.repository;

import com.val.mydocs.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    UserRole findUserRoleByName(String name);
}
