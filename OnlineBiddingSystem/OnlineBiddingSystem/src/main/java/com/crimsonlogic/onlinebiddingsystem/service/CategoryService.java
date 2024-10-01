package com.crimsonlogic.onlinebiddingsystem.service;

import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.dto.CategoryDto;

public interface CategoryService {
    void addCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
	void updateCategoryStatus(Long categoryId, String newStatus);
	List<CategoryDto> getActiveCategories();
	void updateCategory(Long categoryId, CategoryDto categoryDto);
	CategoryDto getCategoryById(Long categoryId);
}
