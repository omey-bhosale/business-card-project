package com.business_card.business_card.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.path}")
    private String uploadPath;

    private final List<String> allowedTypes = List.of("image/jpeg", "image/png");

    public String uploadFile(MultipartFile file) throws IOException {
        // Validate file type
        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPG and PNG files are allowed.");
        }

        // Validate file size
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be <= 2MB.");
        }

        // Ensure directory exists
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        // Save file
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadPath + filename);
        file.transferTo(dest);

        return "http://localhost:8080/uploads/" + filename;
    }
}

