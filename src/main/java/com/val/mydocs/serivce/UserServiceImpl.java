package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCriptPasswordEncoder;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCriptPasswordEncoder, UserRoleService userRoleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCriptPasswordEncoder = bCriptPasswordEncoder;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(userName).orElseThrow(
                () -> new UsernameNotFoundException("Username not found."));
    }

    @Override
    public boolean register(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCriptPasswordEncoder.encode(user.getPassword()));
        //
        Set<UserRole> userRoles = this.prepareUserRoles();
        if (userRoles.isEmpty()){
            Exception e = new Exception("User roles are not set");
            e.printStackTrace();
            return false;
        }
        user.setRoles(userRoles);

        return this.saveRepositoryUser(user);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllUsersNotUsername(String username) {
        List<UserServiceModel> allUsers = this.findAllUsers();
        List<UserServiceModel> allUsersNotUsername = allUsers.stream().filter(u -> !username.equals(u.getUsername()))
                .collect(Collectors.toList());
        return allUsersNotUsername;
    }

    @Override
    public UserServiceModel findUserByID(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null){
            return null;
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public boolean saveUser(UserServiceModel userServiceModel) {
       User user = this.modelMapper.map(userServiceModel, User.class);
       return this.saveRepositoryUser(user);
    }

    @Override
    public boolean saveRoles(UserServiceModel userServiceModel) {
        User user = this.userRepository.findById(userServiceModel.getId()).orElse(null);
        if (user == null){
            return false;
        }
        user.setRoles(userServiceModel.getRoles().stream().map(r -> this.modelMapper.map(r, UserRole.class))
                    .collect(Collectors.toSet()));
        return this.saveRepositoryUser(user);
    }

    @Override
    public boolean isAdmin(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null){
            return false;
        }
        for (UserRole userRole : user.getRoles()) {
            if(userRole.getName().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }

    @Override
    public UserServiceModel findUserByUserName(String username) {
        User user = this.userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found."));
        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        return userServiceModel;
    }

    @Override
    public String getUserLangByName(String username) {
        User user = this.userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found."));

        String lang = user.getLang();
        return lang;
    }


    private Set<UserRole> prepareUserRoles(){
        Set<UserRole> userRoles = new HashSet<>();
        String roleName = "USER";
        UserRole role = this.userRoleService.getUserRoleByName(roleName);
        userRoles.add(role);
        if (this.userRepository.findAll().isEmpty()){
            roleName = "ADMIN";
            role = this.userRoleService.getUserRoleByName(roleName);
            userRoles.add(role);
            roleName = "MODERATOR";
            role = this.userRoleService.getUserRoleByName(roleName);
            userRoles.add(role);
        }
        return userRoles;
    }

    private boolean saveRepositoryUser(User user){
        try {
            this.userRepository.save(user);
            return true;
        }   catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
