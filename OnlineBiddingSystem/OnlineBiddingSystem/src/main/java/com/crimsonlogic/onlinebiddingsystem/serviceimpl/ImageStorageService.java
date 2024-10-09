package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    @Value("${image.upload.dir}") 
    private String uploadDir;

    public String saveImage(MultipartFile imageFile) {
       
        String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);
        System.out.println("Image upload directory: " + uploadDir);
        
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            
            imageFile.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }

        
        return filename; 
    }
}
