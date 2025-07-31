package com.example.simple_task_manager.user.exception;

public class EmptyFileNameException extends ImageUploadException {
    public EmptyFileNameException(String message) {
        super(message);
    }
}
