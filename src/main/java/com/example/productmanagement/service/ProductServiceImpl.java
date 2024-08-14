package com.example.productmanagement.service;

import com.example.productmanagement.dto.ProductDTO;
import com.example.productmanagement.entity.Product;
import com.example.productmanagement.exception.ResourceNotFoundException;
import com.example.productmanagement.repository.ProductRepository;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Triển khai dịch vụ cho sản phẩm.
 * Cung cấp các phương thức nghiệp vụ để thao tác với sản phẩm.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Lấy danh sách tất cả sản phẩm với các tiêu chí lọc và phân trang.
     * 
     * @param name     Tên sản phẩm để lọc (tùy chọn).
     * @param minPrice Giá tối thiểu để lọc (tùy chọn).
     * @param maxPrice Giá tối đa để lọc (tùy chọn).
     * @param page     Số trang (mặc định là 0).
     * @param size     Kích thước trang (mặc định là 10).
     * @return Danh sách sản phẩm theo các tiêu chí lọc và phân trang.
     */
    @Override
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
     * Lấy thông tin sản phẩm theo ID.
     * 
     * @param id ID của sản phẩm.
     * @return DTO của sản phẩm.
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     */
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tìm thấy với ID: " + id));
        return convertToDTO(product);
    }

    /**
     * Tạo một sản phẩm mới.
     * 
     * @param productDTO DTO của sản phẩm mới.
     * @return DTO của sản phẩm đã tạo.
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    /**
     * Cập nhật thông tin sản phẩm theo ID.
     * 
     * @param id         ID của sản phẩm cần cập nhật.
     * @param productDTO DTO của sản phẩm với thông tin cập nhật.
     * @return DTO của sản phẩm đã cập nhật.
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     */
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tìm thấy với ID: " + id));

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
     * @throws ResourceNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     */
    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tìm thấy với ID: " + id));
        productRepository.delete(existingProduct);
    }

    /**
     * Chuyển đổi từ đối tượng Product sang ProductDTO.
     * 
     * @param product Đối tượng Product.
     * @return DTO của sản phẩm.
     */
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

    /**
     * Chuyển đổi từ ProductDTO sang Product.
     * 
     * @param productDTO DTO của sản phẩm.
     * @return Đối tượng Product.
     */
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

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
