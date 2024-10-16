package org.example.taskmanagementapi.services.user;

import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.entities.User;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User findUserByEmail(String email);
    UserDTO findUserDTOByEmail(String email);
    User findUserById(long id);
    UserDTO findUserDTOById(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserById(Long id);
    UserDTO getLoggedUser(Principal currentUser);



}
