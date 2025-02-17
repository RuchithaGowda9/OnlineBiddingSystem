package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.CategoryDto;
import com.crimsonlogic.onlinebiddingsystem.service.CategoryService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return ResponseEntity.ok("Category added successfully.");
    }    
    @GetMapping("/viewcategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        System.out.println(categories);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @PutMapping("/{categoryId}/status")
    public ResponseEntity<String> updateCategoryStatus(@PathVariable Long categoryId, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("status");
        categoryService.updateCategoryStatus(categoryId, newStatus);
        return ResponseEntity.ok("Category status updated successfully.");
    }
    @GetMapping("/active")
    public ResponseEntity<List<CategoryDto>> getActiveCategories() {
        List<CategoryDto> activeCategories = categoryService.getActiveCategories();
        return new ResponseEntity<>(activeCategories, HttpStatus.OK);
    }
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok("Category updated successfully.");
    }


}
