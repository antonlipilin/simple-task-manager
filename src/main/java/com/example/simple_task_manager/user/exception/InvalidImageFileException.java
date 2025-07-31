package com.example.simple_task_manager.user.exception;

public class InvalidImageFileException extends ImageUploadException {
    public InvalidImageFileException(String message) {
        super(message);
    }
}
