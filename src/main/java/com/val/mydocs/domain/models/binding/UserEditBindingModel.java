package com.val.mydocs.domain.models.binding;

import java.util.HashSet;
import java.util.Set;

public class UserEditBindingModel {
    private String id;
    private String username;
    private String password;
    private String email;
    private Set<UserRoleBindingModel> roles;
    private Set<String> roleIds;

    public UserEditBindingModel() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRoleBindingModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoleBindingModel> roles) {
        this.roles = roles;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public void setRolesByRolesIds(){
        if (this.roleIds != null && this.roleIds.size() > 0){
            Set<UserRoleBindingModel> roles = new HashSet<>();
            for (String capitalId : this.roleIds) {
                UserRoleBindingModel role = new UserRoleBindingModel();
                role.setId(capitalId);
                roles.add(role);
            }

            this.setRoles(roles);
        }
    }

    public void setRolesIdsByRoles(){
        if (this.roles != null && this.roles.size() > 0){
            Set<String> rolesIds = new HashSet<>();
            for (UserRoleBindingModel role : this.roles) {
                rolesIds.add(role.getId());
            }

            this.setRoleIds(rolesIds);
        }
    }
}
