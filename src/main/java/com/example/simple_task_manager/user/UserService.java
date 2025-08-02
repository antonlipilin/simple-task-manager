package com.example.simple_task_manager.user;

import com.example.simple_task_manager.security.UserDetailsImpl;
import com.example.simple_task_manager.user.dto.ChangePasswordDto;
import com.example.simple_task_manager.user.dto.UserDto;
import com.example.simple_task_manager.user.exception.ImageIOException;
import com.example.simple_task_manager.user.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileStorageService imageService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, ImageFileStorageService imageService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
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

    public UserDto getUserById(long id) {
        User loadedUser = userRepository.findUserById(id);
        return mapper.map(loadedUser, UserDto.class);
    }

    @Transactional
    public void changeProfileImage(MultipartFile file) {
        UserDetailsImpl userDetails = getAuthenticatedUserDetails();
        String savedFileName = null;

        try {
            savedFileName = imageService.save(file);

            if (savedFileName != null) {
                long principalId = userDetails.getUser().getId();
                User loadedUser = userRepository.findUserById(principalId);

                String oldFileName = loadedUser.getUserPicture();
                loadedUser.setUserPicture(savedFileName);

                userRepository.save(loadedUser);

                if (oldFileName != null) {
                    imageService.delete(oldFileName);
                }
            }

        } catch (IOException e) {

            if (savedFileName != null) {
                try {
                    imageService.delete(savedFileName);
                } catch (IOException exception) {
                    throw new ImageIOException("Failed to change profile image. Please try again.", e);
                }
            }

            throw new ImageIOException("Failed to change profile image. Please try again.", e);
        }  catch (Exception e) {

            if (savedFileName != null) {
                try {
                    imageService.delete(savedFileName);
                } catch (IOException exception) {
                    throw new ImageIOException("Failed to change profile image. Please try again.", e);
                }
            }

            throw e;
        }

    }

    @Transactional
    public void deleteProfileImage() {
        UserDetailsImpl userDetails = getAuthenticatedUserDetails();

        try {
            long userId = userDetails.getUser().getId();
            User loadedUser = this.userRepository.findUserById(userId);

            String fileName = loadedUser.getUserPicture();

            if (fileName != null) {
                loadedUser.setUserPicture(null);
                userRepository.save(loadedUser);

                imageService.delete(fileName);
            }
        } catch (IOException e) {
            throw new ImageIOException("Failed to delete profile image. Please try again.", e);
        }

    }

    public void changePassword(ChangePasswordDto dto) {
        UserDetailsImpl userDetails = getAuthenticatedUserDetails();
        long userId = userDetails.getUser().getId();
        User loadedUser = this.userRepository.findUserById(userId);

        loadedUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        this.userRepository.save(loadedUser);
    }


    private UserDetailsImpl getAuthenticatedUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
