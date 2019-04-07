package com.val.mydocs.domain.models.view;

import com.val.mydocs.domain.entities.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

public class UsersListViewModel {
    private String id;
    private String username;
    private String email;
    private Set<UserRole> roles;
    private String rolesString;
    private boolean admin;

    public UsersListViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getRolesString() {
        String rolesAsString =  roles.stream().map(u -> u.getName()).collect(Collectors.joining( ", " ));
        return rolesAsString;
    }

    public boolean getAdmin() {
        if (this.roles == null) {
            return false;
        }

        for (UserRole role : this.roles) {
            if (role.getName().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }

}
