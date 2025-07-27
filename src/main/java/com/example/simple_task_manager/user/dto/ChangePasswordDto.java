package com.example.simple_task_manager.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {

    @NotEmpty(message = "Password must not be empty.")
    private String currentPassword;

    @NotEmpty(message = "Password must not be empty.")
    @Size(min = 8, max = 20, message = "Password cannot be shorter than 8 characters and longer than 20 characters.")
    @Pattern(regexp = "^(?! )[A-Za-z0-9@#$%^&*()_+\\-= \\[\\]{}|\\\\;:',.<>/?]+(?<! )$", message = "Password must contain only letters, digits, allowed special characters, spaces, and must not start or end with space")
    private String newPassword;

    private String repeatPassword;

    public ChangePasswordDto() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
