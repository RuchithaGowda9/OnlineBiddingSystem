package com.crimsonlogic.onlinebiddingsystem.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.crimsonlogic.onlinebiddingsystem.entity.User;

import lombok.Data;

/**
 * @author Ruchitha
 *
 */
@Data
public class ProductDto {
    private Long productId;
    private String name;
    private Double askingPrice;
    private LocalDateTime endTime;
    private String description;
    private Long categoryId; 
    private String status;
    private Long sellerId;
    private MultipartFile image;
}
