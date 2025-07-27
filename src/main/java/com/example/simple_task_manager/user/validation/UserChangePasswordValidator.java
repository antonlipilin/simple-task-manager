package com.example.simple_task_manager.user.validation;


import com.example.simple_task_manager.security.UserDetailsImpl;
import com.example.simple_task_manager.user.User;
import com.example.simple_task_manager.user.dto.ChangePasswordDto;
import com.example.simple_task_manager.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserChangePasswordValidator implements Validator {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserChangePasswordValidator(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordDto passwordDto = (ChangePasswordDto) target;

        if (!passwordDto.getNewPassword().equals(passwordDto.getRepeatPassword())) {
            errors.rejectValue("repeatPassword", "password.mismatch", "Passwords do not match");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), userDetails.getPassword())) {
            errors.rejectValue("currentPassword", "incorrect.currentPassword", "Current password is incorrect");
        }
    }
}
