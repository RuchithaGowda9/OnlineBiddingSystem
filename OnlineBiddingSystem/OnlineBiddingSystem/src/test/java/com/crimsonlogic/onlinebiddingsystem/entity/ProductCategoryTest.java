package com.crimsonlogic.onlinebiddingsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductCategoryTest {

    private ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        productCategory = new ProductCategory();
        productCategory.setCategoryName("Electronics");
        productCategory.setCategoryDescription("All electronic items.");
        productCategory.setStatus("active");
    }

    @Test
    void testProductCategoryCreation() {
        // Act
        // No ID generation to call here

        // Assert
        assertNotNull(productCategory.getCategoryName());
        assertNotNull(productCategory.getCategoryDescription());
        assertEquals("Electronics", productCategory.getCategoryName());
        assertEquals("All electronic items.", productCategory.getCategoryDescription());
        assertEquals("active", productCategory.getStatus());
    }

    @Test
    void testProductCategoryProperties() {
        // Assert initial properties
        assertEquals("Electronics", productCategory.getCategoryName());
        assertEquals("All electronic items.", productCategory.getCategoryDescription());
        assertEquals("active", productCategory.getStatus());
    }

    @Test
    void testStatusUpdate() {
        // Arrange
        String newStatus = "inactive";

        // Act
        productCategory.setStatus(newStatus);

        // Assert
        assertEquals(newStatus, productCategory.getStatus());
    }

    @Test
    void testCategoryDescriptionUpdate() {
        // Arrange
        String newDescription = "Updated description for electronics.";

        // Act
        productCategory.setCategoryDescription(newDescription);

        // Assert
        assertEquals(newDescription, productCategory.getCategoryDescription());
    }
}
