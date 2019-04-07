package com.val.mydocs.configuration;

import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseSeeder {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public DatabaseSeeder(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @PostConstruct
    public void seed(){
        if (this.userRoleRepository.findAll().isEmpty()){
            UserRole userRole = new UserRole();
            userRole.setName("USER");
            UserRole adminRole = new UserRole();
            adminRole.setName("ADMIN");
            UserRole moderatorRole = new UserRole();
            moderatorRole.setName("MODERATOR");
            this.userRoleRepository.save(userRole);
            this.userRoleRepository.save(adminRole);
            this.userRoleRepository.save(moderatorRole);
        }
    }
}
