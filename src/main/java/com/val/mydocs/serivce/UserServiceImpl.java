package com.val.mydocs.serivce;

import com.val.mydocs.domain.entities.User;
import com.val.mydocs.domain.entities.UserRole;
import com.val.mydocs.domain.models.service.UserServiceModel;
import com.val.mydocs.exceptions.ModelValidationException;
import com.val.mydocs.exceptions.UniqueFieldException;
import com.val.mydocs.repository.UserRepository;
import com.val.mydocs.validation.UserValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.ConfigurationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final static String USER_ROLES_ARE_NOT_SET =  "User roles are not set";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final UserValidationService userValidationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCriptPasswordEncoder,
                           UserRoleService userRoleService,
                           ModelMapper modelMapper, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCriptPasswordEncoder;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.userValidationService = userValidationService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(userName).orElseThrow(
                () -> new UsernameNotFoundException("Username not found."));
    }

    @Override
    public boolean register(UserServiceModel userServiceModel) throws UniqueFieldException, ModelValidationException, ConfigurationException {
        if (!this.userValidationService.isValid(userServiceModel)){
            throw new ModelValidationException("User", userServiceModel);
        }

        User user = this.modelMapper.map(userServiceModel, User.class);

        this.checkUniqueness(user);

        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        //
        Set<UserRole> userRoles = this.prepareUserRoles();
        if (userRoles.isEmpty()){
           throw new ConfigurationException(USER_ROLES_ARE_NOT_SET);
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
        if (!this.userValidationService.isValid(userServiceModel)){
            return false;
        }
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

    private void checkUniqueness(User user) throws UniqueFieldException {
        User userWithTheSameUsername = this.userRepository.findUserByUsername(user.getUsername()).orElse(null);
        if (userWithTheSameUsername != null && !userWithTheSameUsername.getId().equals(user.getId())){
            throw new UniqueFieldException(this.getClass().getName(), "username");
        }

        User userWithTheSameEmail = this.userRepository.findUserByEmail(user.getEmail()).orElse(null);
        if (userWithTheSameEmail != null && !userWithTheSameEmail.getId().equals(user.getId())){
            throw new UniqueFieldException(this.getClass().getName(), "email");
        }
    }
}
