package org.example.taskmanagementapi.services.user;

import org.example.taskmanagementapi.dto.user.UserDTO;
import org.example.taskmanagementapi.entities.User;
import org.example.taskmanagementapi.exceptions.NotFoundExceptionHandler;
import org.example.taskmanagementapi.mappers.UserMapper;
import org.example.taskmanagementapi.repositories.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundExceptionHandler("This user not found"));
    }

    @Override
    public UserDTO findUserDTOByEmail(String email) {
        return userMapper.toDto(findUserByEmail(email));
    }


    @Override
    public User findUserById(long id) {
        return userRepository.findUserById(id)
                .orElseThrow(()-> new NotFoundExceptionHandler("This user not found"));
    }

    @Override
    public UserDTO findUserDTOById(String email) {
        return userMapper.toDto(findUserByEmail(email));
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserById(Long id) {
        return userRepository.existsUserById(id);
    }
}
