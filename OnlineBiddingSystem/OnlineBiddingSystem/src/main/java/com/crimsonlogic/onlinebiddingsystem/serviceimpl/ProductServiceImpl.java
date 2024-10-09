package com.crimsonlogic.onlinebiddingsystem.serviceimpl;

import com.crimsonlogic.onlinebiddingsystem.dto.ProductDto;
import com.crimsonlogic.onlinebiddingsystem.entity.Product;
import com.crimsonlogic.onlinebiddingsystem.entity.ProductCategory;
import com.crimsonlogic.onlinebiddingsystem.entity.User;
import com.crimsonlogic.onlinebiddingsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.onlinebiddingsystem.repository.ProductRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.CategoryRepository;
import com.crimsonlogic.onlinebiddingsystem.repository.UserRepository;
import com.crimsonlogic.onlinebiddingsystem.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addProduct(ProductDto productDto) throws ResourceNotFoundException {
        Product product = modelMapper.map(productDto, Product.class);

        ProductCategory category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        product.setCategory(category);
        product.setSellerId(seller);
        product.setProductStatus("active");

        if (productDto.getImage() != null && !productDto.getImage().isEmpty()) {
            String imagePath = imageStorageService.saveImage(productDto.getImage());
            product.setImage(imagePath); 
        }

        productRepository.save(product);
    }
    public List<ProductDto> getProductsBySeller(Long sellerId) {
        List<Product> products = productRepository.findBySellerId_userId(sellerId);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getProductName());
        dto.setAskingPrice(product.getAskingPrice());
        dto.setEndTime(product.getEndTime());
        dto.setDescription(product.getProductDescription());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setStatus(product.getProductStatus());
        dto.setSellerId(product.getSellerId().getUserId());
        return dto;
    }


	
	@Override
    public byte[] getImageById(Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));
        String imagePath = product.getImage(); 
        
        try {           
            Path path = Paths.get("product-images/" + imagePath); 
            return Files.readAllBytes(path); 
        } catch (Exception e) {
            throw new RuntimeException("Could not read image: " + e.getMessage());
        }
    }
	
	@Override
	public List<ProductDto> getAllProducts() {
	    List<Product> products = productRepository.findAll();
	    return products.stream().map(this::convertToDto).collect(Collectors.toList());
	}
	@Override
    public ProductDto getProductById(Long productId) throws ResourceNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Product product = optionalProduct.get();
        return convertToDto(product);
    }



}
