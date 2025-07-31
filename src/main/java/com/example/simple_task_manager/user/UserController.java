package com.example.simple_task_manager.user;

import com.example.simple_task_manager.security.UserDetailsImpl;
import com.example.simple_task_manager.user.dto.ChangePasswordDto;
import com.example.simple_task_manager.user.dto.UserDto;
import com.example.simple_task_manager.user.exception.UserAlreadyExistsException;
import com.example.simple_task_manager.user.validation.UserChangePasswordValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UserController {

    private final UserService userService;

    private final UserChangePasswordValidator userChangePasswordValidator;

    private final TokenBasedRememberMeServices rememberMeServices;

    private LogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @Autowired
    public UserController(UserService userService, UserChangePasswordValidator userChangePasswordValidator, TokenBasedRememberMeServices rememberMeServices) {
        this.userService = userService;
        this.userChangePasswordValidator = userChangePasswordValidator;
        this.rememberMeServices = rememberMeServices;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/tasks")
    public String showMainPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        long userId = userDetails.getUser().getId();
        UserDto user = userService.getUserById(userId);

        model.addAttribute("user", user);

        return "tasks";
    }

    @GetMapping("/")
    public String redirectToMainPage() {
        return "redirect:/tasks";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());

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
    public String settings(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        long userId = userDetails.getUser().getId();
        UserDto user = userService.getUserById(userId);

        model.addAttribute("user", user);

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ChangePasswordDto());
        }

        return "settings";
    }

    @PostMapping("/settings/changeImage")
    public String changeImage(@RequestAttribute("file") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        userService.changeProfileImage(file, userDetails);

        return "redirect:/settings";
    }

    @PostMapping("/settings/changePassword")
    public String changePassword(@ModelAttribute("changePasswordForm") @Valid ChangePasswordDto form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) {
        userChangePasswordValidator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", bindingResult);
            redirectAttributes.addFlashAttribute("form", form);

            return "redirect:/settings";
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        this.userService.changePassword(form, userDetails);
        this.logoutHandler.logout(request, response, authentication);
        this.rememberMeServices.logout(request, response, authentication);

        return "redirect:/login?changePassword";
    }
}
