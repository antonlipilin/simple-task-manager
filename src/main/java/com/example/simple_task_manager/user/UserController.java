package com.example.simple_task_manager.user;

import com.example.simple_task_manager.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/tasks")
    public String showMainPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        long userId = userDetails.getUser().getId();
        User user = userService.getUserById(userId);

        model.addAttribute("user", user);

        return "tasks";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/tasks";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String createAccount(@ModelAttribute("user") @Valid UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.saveUser(dto);
        } catch (UserAlreadyExistsException ex) {
            bindingResult.rejectValue("username", "error.username", "A user with this username already exists");
            return "registration";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }
}
