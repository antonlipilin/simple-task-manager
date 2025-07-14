package com.example.simple_task_manager.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(UserDto dto) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User userEntity = convertToEntity(dto);
        userRepository.save(userEntity);
    }

    private User convertToEntity(UserDto dto) {
        User user = mapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    public User getUserById(long id) {
        return userRepository.findUserById(id);
    }
}
