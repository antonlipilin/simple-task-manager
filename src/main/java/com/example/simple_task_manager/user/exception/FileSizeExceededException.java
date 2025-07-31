package com.example.simple_task_manager.user.exception;

public class FileSizeExceededException extends ImageUploadException {
    public FileSizeExceededException(String message) {
        super(message);
    }
}
