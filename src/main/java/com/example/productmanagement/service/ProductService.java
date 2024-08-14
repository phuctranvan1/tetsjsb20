package com.example.productmanagement.service;

import com.example.productmanagement.dto.ProductDTO;
import com.example.productmanagement.entity.Product;
import com.example.productmanagement.exception.ResourceNotFoundException;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dịch vụ cho sản phẩm.
 * Cung cấp các phương thức nghiệp vụ để thao tác với sản phẩm.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy danh sách tất cả sản phẩm với phân trang và lọc.
     * 
     * @param name     Tên sản phẩm để lọc.
     * @param minPrice Giá tối thiểu để lọc.
     * @param maxPrice Giá tối đa để lọc.
     * @param page     Số trang.
     * @param size     Kích thước trang.
     * @return Danh sách sản phẩm theo các tiêu chí lọc và phân trang.
     */
    public Page<ProductDTO> getAllProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<Product> productPage;

        if (name != null && minPrice != null && maxPrice != null) {
            productPage = productRepository.findByNameContainingIgnoreCaseAndPriceBetween(name, minPrice, maxPrice,
                    pageable);
        } else if (name != null) {
            productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (minPrice != null && maxPrice != null) {
            productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::convertToDTO);
    }

    /**
     * Lấy sản phẩm theo ID.
     * 
     * @param id ID của sản phẩm.
     * @return DTO của sản phẩm.
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm.
     */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return convertToDTO(product);
    }

    /**
     * Tạo mới sản phẩm.
     * 
     * @param productDTO DTO của sản phẩm mới.
     * @return DTO của sản phẩm đã tạo.
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    /**
     * Cập nhật sản phẩm theo ID.
     * 
     * @param id         ID của sản phẩm cần cập nhật.
     * @param productDTO DTO của sản phẩm với các thông tin cập nhật.
     * @return DTO của sản phẩm đã cập nhật.
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm.
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscountPrice(productDTO.getDiscountPrice());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setStatus(productDTO.getStatus());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    /**
     * Xóa sản phẩm theo ID.
     * 
     * @param id ID của sản phẩm cần xóa.
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm.
     */
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(existingProduct);
    }

    // Chuyển đổi từ Product sang ProductDTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscountPrice(product.getDiscountPrice());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setStatus(product.getStatus());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }

    // Chuyển đổi từ ProductDTO sang Product
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPrice(productDTO.getDiscountPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setStatus(productDTO.getStatus());
        product.setCreatedAt(productDTO.getCreatedAt());
        product.setUpdatedAt(productDTO.getUpdatedAt());
        return product;
    }

    public List<ProductDTO> getAllProducts(String name, Double minPrice, Double maxPrice) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllProducts'");
    }
}
