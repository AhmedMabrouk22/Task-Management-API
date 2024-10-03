package org.example.taskmanagementapi.services;

import org.example.taskmanagementapi.dto.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(new User(userDTO.getName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getImage())
        );
        userDTO.setId(user.getId());
        return userDTO;
    }
}
