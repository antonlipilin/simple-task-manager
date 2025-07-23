package com.example.simple_task_manager.advice;

import com.example.simple_task_manager.user.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = {
            EmptyFileNameException.class,
            FileSizeExceededException.class,
            MissingFileException.class,
            UnsupportedFileTypeException.class,
            ImageIOException.class,
            InvalidImageFileException.class
    })
    public String handleImageUploadExceptions (Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/settings";
    }

}
