package com.example.simple_task_manager.user.exception;

public class MissingFileException extends RuntimeException {
    public MissingFileException(String message) {
        super(message);
    }
}
