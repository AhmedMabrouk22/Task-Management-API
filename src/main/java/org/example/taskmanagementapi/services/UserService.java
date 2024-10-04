package org.example.taskmanagementapi.services;

import org.example.taskmanagementapi.dto.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.exceptions.DatabaseException;
import org.example.taskmanagementapi.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = userRepository.save(new User(userDTO.getName(),
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getImage())
            );
            userDTO.setId(user.getId());
            return userDTO;
        } catch (DataIntegrityViolationException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                throw new DatabaseException("email is already exist");
            }

            throw new DatabaseException("Unexpected error when create user");
        }
    }
}
