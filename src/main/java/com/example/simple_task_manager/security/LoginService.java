package com.example.simple_task_manager.security;

import com.example.simple_task_manager.user.User;
import com.example.simple_task_manager.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public LoginService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadedUser = this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("LOADED USER " + loadedUser.getUsername() + " " + loadedUser.getPassword());
        return new UserDetailsImpl(loadedUser);
    }
}
