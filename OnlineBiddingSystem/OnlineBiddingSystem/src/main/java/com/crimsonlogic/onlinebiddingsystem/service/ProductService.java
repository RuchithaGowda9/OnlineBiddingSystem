package com.crimsonlogic.onlinebiddingsystem.service;


import java.util.List;

import com.crimsonlogic.onlinebiddingsystem.dto.ProductDto;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;

public interface ProductService {
    void addProduct(ProductDto productDto) throws ResourceNotFoundException;

	List<ProductDto> getProductsBySeller(Long sellerId);
	byte[] getImageById(Long imageId) throws ResourceNotFoundException;

	List<ProductDto> getAllProducts();

	ProductDto getProductById(Long productId) throws ResourceNotFoundException;
}
