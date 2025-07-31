package com.example.simple_task_manager.user.exception;

public class UnsupportedFileTypeException extends ImageUploadException {
    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
