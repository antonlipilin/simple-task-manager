package com.example.simple_task_manager.user.impl;

import com.example.simple_task_manager.user.ImageFileStorageService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileSystemStorageService implements ImageFileStorageService {

    private static final String DEFAULT_FOLDER_PATH = "/home/purplewave/Рабочий стол/avatars";

    private String folderPath = DEFAULT_FOLDER_PATH;

    public FileSystemStorageService() {
    }

    public FileSystemStorageService(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public void delete(String fileName) throws IOException {
        Files.deleteIfExists(Path.of(this.folderPath, fileName));
    }

    @Override
    @Nullable
    public String save(MultipartFile file) throws IOException {
        validate(file);

        String fileName = generateUniqueFileName(file);
        Path path = Path.of(this.folderPath, fileName);

        file.transferTo(path);

        return fileName;
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        return UUID.randomUUID() + extension;

    }
}
