package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.dto.ProductDto;
import com.crimsonlogic.onlinebiddingsystem.entity.Product;
import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.repository.ProductRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.CategoryRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private ModelMapper modelMapper;

    private ProductDto productDto;
    private Product product;
    private User seller;
    private ProductCategory category;
    private MultipartFile imageFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        seller = new User();
        seller.setUserId(1L);

        category = new ProductCategory();
        category.setCategoryId(1L);

        productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setName("Test Product");
        productDto.setAskingPrice(100.0);
        productDto.setEndTime(null);
        productDto.setDescription("Test Description");
        productDto.setCategoryId(1L);
        productDto.setSellerId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setAskingPrice(100.0);
        product.setProductDescription("Test Description");
        product.setCategory(category);
        product.setSellerId(seller);
        product.setProductStatus("active");

        imageFile = mock(MultipartFile.class);
        when(imageFile.getOriginalFilename()).thenReturn("test-image.png");
    }

    @Test
    void testAddProduct_Success() throws ResourceNotFoundException {
        // Arrange
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(Optional.of(category));
        when(userRepository.findById(productDto.getSellerId())).thenReturn(Optional.of(seller));
        when(modelMapper.map(productDto, Product.class)).thenReturn(product);
        when(imageStorageService.saveImage(any(MultipartFile.class))).thenReturn("path/to/image.png");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        productService.addProduct(productDto);

        // Assert
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testAddProduct_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.addProduct(productDto);
        });
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void testAddProduct_SellerNotFound() {
        // Arrange
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(Optional.of(category));
        when(userRepository.findById(productDto.getSellerId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.addProduct(productDto);
        });
        assertEquals("Seller not found", exception.getMessage());
    }

    @Test
    void testGetProductsBySeller_Success() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(productRepository.findBySellerId_userId(1L)).thenReturn(products);
        when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        // Act
        List<ProductDto> result = productService.getProductsBySeller(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findBySellerId_userId(1L);
    }

    @Test
    void testGetImageById_Success() throws Exception {
        // Arrange
        product.setImage("test-image.png");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Path path = Paths.get("product-images/test-image.png");
        Files.createDirectories(path.getParent());
        Files.write(path, "image data".getBytes());

        // Act
        byte[] imageBytes = productService.getImageById(1L);

        // Assert
        assertArrayEquals("image data".getBytes(), imageBytes);
        Files.delete(path); // Clean up
    }

    @Test
    void testGetImageById_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getImageById(1L);
        });
        assertEquals("Product not found with id 1", exception.getMessage());
    }

    @Test
    void testGetProductById_Success() throws ResourceNotFoundException {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        // Act
        ProductDto result = productService.getProductById(1L);

        // Assert
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1L);
        });
        assertEquals("Product not found with id: 1", exception.getMessage());
    }
}
