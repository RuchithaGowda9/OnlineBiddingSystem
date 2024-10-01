package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.dto.CategoryDto;
import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;
import com.crimsonlogic.onlinebiddingsystem.exception.CategoryAlreadyExistsException;
import com.crimsonlogic.onlinebiddingsystem.repository.CategoryRepository;
import com.crimsonlogic.onlinebiddingsystem.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addCategory(CategoryDto categoryDto) {
        ProductCategory category = modelMapper.map(categoryDto, ProductCategory.class);
        category.setStatus("active");
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryAlreadyExistsException("Category with name '" + categoryDto.getCategoryName() + "' already exists.");
        }
    }
    
    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
    @Override
    public void updateCategoryStatus(Long categoryId, String newStatus) {
        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(newStatus);
        categoryRepository.save(category);
    }
    @Override
    public List<CategoryDto> getActiveCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> "active".equals(category.getStatus()))
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCategory(Long categoryId, CategoryDto categoryDto) {
        ProductCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        categoryRepository.save(category);
    }

	@Override
	public CategoryDto getCategoryById(Long categoryId) {
	    ProductCategory category = categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new RuntimeException("Category not found"));
	    return modelMapper.map(category, CategoryDto.class);
	}



}
