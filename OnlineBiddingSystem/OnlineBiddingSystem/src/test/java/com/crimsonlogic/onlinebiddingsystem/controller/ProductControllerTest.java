package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.ProductDto;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private MultipartFile image;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setAskingPrice(100.0);
        productDto.setEndTime(LocalDateTime.now().plusDays(1));
        productDto.setDescription("Test Description");
        productDto.setCategoryId(1L);
    }

    @Test
    void testAddProduct_Success() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;

        // Act
        ResponseEntity<String> response = productController.addProduct(sellerId, 
            productDto.getName(), productDto.getAskingPrice(),
            productDto.getEndTime(), productDto.getDescription(),
            productDto.getCategoryId(), image);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product added successfully!", response.getBody());

        verify(productService, times(1)).addProduct(any(ProductDto.class));
    }

    @Test
    void testGetProductsBySellerId_Success() {
        // Arrange
        Long sellerId = 1L;
        List<ProductDto> productList = new ArrayList<>();
        productList.add(productDto);

        when(productService.getProductsBySeller(sellerId)).thenReturn(productList);

        // Act
        ResponseEntity<List<ProductDto>> response = productController.getProductsBySellerId(sellerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(productDto.getName(), response.getBody().get(0).getName());
    }

    @Test
    void testGetProductsBySellerId_NotFound() {
        // Arrange
        Long sellerId = 1L;

        when(productService.getProductsBySeller(sellerId)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<ProductDto>> response = productController.getProductsBySellerId(sellerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetImage_Success() throws ResourceNotFoundException {
        // Arrange
        Long productId = 1L;
        byte[] imageData = new byte[]{1, 2, 3};
        when(productService.getImageById(productId)).thenReturn(imageData);

        // Act
        ResponseEntity<byte[]> response = productController.getImage(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageData, response.getBody());
    }

    @Test
    void testGetImage_NotFound() throws ResourceNotFoundException {
        // Arrange
        Long productId = 1L;
        when(productService.getImageById(productId)).thenThrow(new ResourceNotFoundException("Image not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productController.getImage(productId));
    }

    @Test
    void testGetAllProducts_Success() {
        // Arrange
        List<ProductDto> productList = new ArrayList<>();
        productList.add(productDto);
        when(productService.getAllProducts()).thenReturn(productList);

        // Act
        ResponseEntity<List<ProductDto>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(productDto.getName(), response.getBody().get(0).getName());
    }

    @Test
    void testGetProductById_Success() throws ResourceNotFoundException {
        // Arrange
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(productDto);

        // Act
        ResponseEntity<ProductDto> response = productController.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto.getName(), response.getBody().getName());
    }

    @Test
    void testGetProductById_NotFound() throws ResourceNotFoundException {
        // Arrange
        Long productId = 1L;
        when(productService.getProductById(productId)).thenThrow(new ResourceNotFoundException("Product not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productController.getProductById(productId));
    }
}
