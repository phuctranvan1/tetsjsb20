package com.example.productmanagement.controller;

import com.example.productmanagement.dto.ProductDTO;
import com.example.productmanagement.service.ProductService;
import com.example.productmanagement.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Lấy danh sách sản phẩm với tùy chọn lọc theo tên, khoảng giá.
     * 
     * @param name     Tên sản phẩm để lọc.
     * @param minPrice Giá tối thiểu để lọc.
     * @param maxPrice Giá tối đa để lọc.
     * @return Danh sách sản phẩm phù hợp với tiêu chí lọc.
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductDTO> products = productService.getAllProducts(name, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Thêm mới một sản phẩm.
     * 
     * @param productDTO Thông tin sản phẩm cần thêm mới.
     * @return Sản phẩm vừa được thêm mới.
     */
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        final ProductDTO addedProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    /**
     * Cập nhật thông tin của một sản phẩm.
     * 
     * @param id         ID của sản phẩm cần cập nhật.
     * @param productDTO Thông tin sản phẩm đã được cập nhật.
     * @return Sản phẩm đã được cập nhật.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Xóa một sản phẩm dựa trên ID.
     * 
     * @param id ID của sản phẩm cần xóa.
     * @return HTTP status 204 (No Content) nếu thành công.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy chi tiết của một sản phẩm dựa trên ID.
     * 
     * @param id ID của sản phẩm cần lấy thông tin.
     * @return Sản phẩm với ID đã cho.
     * @throws ResourceNotFoundException Nếu sản phẩm không tìm thấy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        return ResponseEntity.ok(product);
    }
}
