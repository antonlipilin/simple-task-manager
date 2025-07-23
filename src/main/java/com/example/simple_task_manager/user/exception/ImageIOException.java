package com.example.simple_task_manager.user.exception;

public class ImageIOException extends RuntimeException {
    public ImageIOException(String message) {
        super(message);
    }

    public ImageIOException(String message, Throwable cause) {
      super(message, cause);
    }
}
