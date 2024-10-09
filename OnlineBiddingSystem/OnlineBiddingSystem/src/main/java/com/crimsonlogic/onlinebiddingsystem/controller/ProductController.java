package com.crimsonlogic.onlinebiddingsystem.controller;

import com.crimsonlogic.onlinebiddingsystem.dto.ProductDto;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.service.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addproducts/{sellerId}")
    public ResponseEntity<String> addProduct(
            @PathVariable Long sellerId, // Take sellerId as a path variable
            @RequestParam("name") String name,
            @RequestParam("askingPrice") Double askingPrice,
            @RequestParam("endTime") LocalDateTime endTime,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("image") MultipartFile image) throws ResourceNotFoundException {
    	System.out.println(sellerId);
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setAskingPrice(askingPrice);
        productDto.setEndTime(endTime);
        productDto.setDescription(description);
        productDto.setCategoryId(categoryId);
        productDto.setSellerId(sellerId); // Use the path variable
        productDto.setImage(image);

        productService.addProduct(productDto);

        return new ResponseEntity<>("Product added successfully!", HttpStatus.CREATED);
    }
    @GetMapping("product/{sellerId}")
    public ResponseEntity<List<ProductDto>> getProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductDto> products = productService.getProductsBySeller(sellerId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/image/{productId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long productId) throws ResourceNotFoundException {
        byte[] image = productService.getImageById(productId); 
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) throws ResourceNotFoundException {
        ProductDto product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    
    

}
