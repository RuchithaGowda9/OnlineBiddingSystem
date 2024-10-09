package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductTest {

    private Product product;
    private ProductCategory category;
    private User seller;

    @BeforeEach
    void setUp() {
        product = new Product();

        // Initialize ProductCategory and User instances
        category = new ProductCategory(); // Assuming ProductCategory has a no-arg constructor
        seller = new User(); // Assuming User has a no-arg constructor

        // Set up sample data
        category.setCategoryId(1L); // Assuming ProductCategory has setCategoryId method
        seller.setUserId(2L); // Assuming User has setUserId method

        product.setProductName("Sample Product");
        product.setProductDescription("This is a sample product.");
        product.setCategory(category);
        product.setAskingPrice(100.0);
        product.setEndTime(LocalDateTime.now().plusDays(1));
        product.setSellerId(seller);
        product.setProductStatus("Available");
        product.setImage("path/to/image.jpg");
    }

    @Test
    void testProductCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(product.getCategory());
        assertNotNull(product.getSellerId());
        assertEquals("Sample Product", product.getProductName());
        assertEquals("This is a sample product.", product.getProductDescription());
        assertEquals(100.0, product.getAskingPrice());
        assertNotNull(product.getEndTime());
        assertEquals("Available", product.getProductStatus());
        assertEquals("path/to/image.jpg", product.getImage());
    }

    @Test
    void testProductProperties() {
        // Assert initial properties
        assertEquals("Sample Product", product.getProductName());
        assertEquals("This is a sample product.", product.getProductDescription());
        assertEquals(category, product.getCategory());
        assertEquals(100.0, product.getAskingPrice());
        assertNotNull(product.getEndTime());
        assertEquals(seller, product.getSellerId());
        assertEquals("Available", product.getProductStatus());
        assertEquals("path/to/image.jpg", product.getImage());
    }

    @Test
    void testProductStatusUpdate() {
        // Arrange
        String newStatus = "Sold";

        // Act
        product.setProductStatus(newStatus);

        // Assert
        assertEquals(newStatus, product.getProductStatus());
    }

    @Test
    void testAskingPriceUpdate() {
        // Arrange
        Double newAskingPrice = 150.0;

        // Act
        product.setAskingPrice(newAskingPrice);

        // Assert
        assertEquals(newAskingPrice, product.getAskingPrice());
    }
}
