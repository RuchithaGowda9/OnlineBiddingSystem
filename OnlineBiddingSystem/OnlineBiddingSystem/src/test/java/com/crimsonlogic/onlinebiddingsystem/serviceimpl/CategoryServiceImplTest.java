package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.dto.CategoryDto;
import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;
import com.crimsonlogic.onlinebiddingsystem.exception.CategoryAlreadyExistsException;
import com.crimsonlogic.onlinebiddingsystem.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private CategoryDto categoryDto;
    private ProductCategory category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize CategoryDto
        categoryDto = new CategoryDto();
        categoryDto.setCategoryName("Electronics");
        categoryDto.setCategoryDescription("Devices and gadgets");

        // Initialize ProductCategory
        category = new ProductCategory();
        category.setCategoryName("Electronics");
        category.setCategoryDescription("Devices and gadgets");
        category.setStatus("active");
    }

    @Test
    void testAddCategory_Success() {
        // Arrange
        when(modelMapper.map(any(CategoryDto.class), eq(ProductCategory.class))).thenReturn(category);
        when(categoryRepository.save(any(ProductCategory.class))).thenReturn(category);

        // Act
        categoryService.addCategory(categoryDto);

        // Assert
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testAddCategory_CategoryAlreadyExists() {
        // Arrange
        when(modelMapper.map(any(CategoryDto.class), eq(ProductCategory.class))).thenReturn(category);
        doThrow(new DataIntegrityViolationException("Unique constraint violation"))
                .when(categoryRepository).save(any(ProductCategory.class));

        // Act & Assert
        CategoryAlreadyExistsException exception = assertThrows(
            CategoryAlreadyExistsException.class,
            () -> categoryService.addCategory(categoryDto)
        );
        assertEquals("Category with name 'Electronics' already exists.", exception.getMessage());
    }

    @Test
    void testGetAllCategories_Success() {
        // Arrange
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(any(ProductCategory.class), eq(CategoryDto.class))).thenReturn(categoryDto);

        // Act
        List<CategoryDto> result = categoryService.getAllCategories();

        // Assert
        assertEquals(1, result.size());
        assertEquals(categoryDto.getCategoryName(), result.get(0).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategoryStatus_Success() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        category.setStatus("inactive");

        // Act
        categoryService.updateCategoryStatus(category.getCategoryId(), "inactive");

        // Assert
        verify(categoryRepository, times(1)).save(category);
        assertEquals("inactive", category.getStatus());
    }

    @Test
    void testUpdateCategoryStatus_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.updateCategoryStatus(category.getCategoryId(), "inactive"));
    }

    @Test
    void testGetActiveCategories_Success() {
        // Arrange
        List<ProductCategory> categories = new ArrayList<>();
        category.setStatus("active");
        categories.add(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(any(ProductCategory.class), eq(CategoryDto.class))).thenReturn(categoryDto);

        // Act
        List<CategoryDto> result = categoryService.getActiveCategories();

        // Assert
        assertEquals(1, result.size());
        assertEquals(categoryDto.getCategoryName(), result.get(0).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory_Success() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));

        // Act
        categoryService.updateCategory(category.getCategoryId(), categoryDto);

        // Assert
        verify(categoryRepository, times(1)).save(category);
        assertEquals(categoryDto.getCategoryDescription(), category.getCategoryDescription());
    }

    @Test
    void testUpdateCategory_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(category.getCategoryId(), categoryDto));
    }

    @Test
    void testGetCategoryById_Success() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        when(modelMapper.map(any(ProductCategory.class), eq(CategoryDto.class))).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.getCategoryById(category.getCategoryId());

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getCategoryName(), result.getCategoryName());
    }

    @Test
    void testGetCategoryById_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(category.getCategoryId()));
    }
}
