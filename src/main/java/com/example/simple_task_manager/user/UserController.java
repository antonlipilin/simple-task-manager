package com.example.simple_task_manager.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/tasks")
    public String showMainPage() {
        return "tasks";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/tasks";
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createAccount() {
        return "redirect:/login?success";
    }
}
