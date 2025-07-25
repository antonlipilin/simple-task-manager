package com.example.simple_task_manager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "Username must not be empty.")
    @Size(min = 4, max = 15, message = "Username cannot be shorter than 4 characters and longer than 15 characters.")
    @Pattern(regexp = "^(?! )[A-Za-z0-9@#$%^&*()_+\\-= \\[\\]{}|\\\\;:',.<>/?]+(?<! )$", message = "Username must contain only letters, digits, allowed special characters, spaces, and must not start or end with space")
    private String username;

    @NotBlank(message = "Password must not be empty.")
    @Size(min = 8, max = 20, message = "Password cannot be shorter than 8 characters and longer than 20 characters.")
    @Pattern(regexp = "^(?! )[A-Za-z0-9@#$%^&*()_+\\-= \\[\\]{}|\\\\;:',.<>/?]+(?<! )$", message = "Password must contain only letters, digits, allowed special characters, spaces, and must not start or end with space")
    private String password;

    private String userPicture;

    public UserDto() {
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }
}
