package com.crimsonlogic.onlinebiddingsystem.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private String status;
}
