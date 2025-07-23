package com.example.simple_task_manager.user;

import com.example.simple_task_manager.user.exception.*;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public interface ImageFileStorageService {
     Set<String> validContentTypes = Set.of("image/jpeg", "image/jpg", "image/png");

     Set<String> validExtensions = Set.of(".png", ".jpeg", ".jpg");

     long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Nullable
    String save(MultipartFile file) throws IOException;

    void delete(String fileName) throws IOException;

    default void validate(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new MissingFileException("Image file is required.");
        }

        String contentType = file.getContentType();

        if (contentType == null || !validContentTypes.contains(contentType)){
            throw new UnsupportedFileTypeException("Only PNG and JPEG images are allowed.");
        }

        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        if (bufferedImage == null) {
            throw new InvalidImageFileException("Uploaded file is not a valid image");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new EmptyFileNameException("The file name must not be empty.");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.'));

        if (!validExtensions.contains(extension.toLowerCase())) {
            throw new UnsupportedFileTypeException("Only PNG and JPEG images are allowed.");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException("File size must be less than 10MB.");
        }
    }
}
