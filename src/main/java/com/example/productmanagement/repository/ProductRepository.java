package com.example.productmanagement.repository;

import com.example.productmanagement.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository cho bảng sản phẩm.
 * Cung cấp các phương thức để truy xuất và thao tác dữ liệu sản phẩm.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Tìm sản phẩm theo tên.
     * 
     * @param name Tên sản phẩm.
     * @return Danh sách sản phẩm khớp với tên cho trước.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Tìm sản phẩm theo khoảng giá.
     * 
     * @param minPrice Giá tối thiểu.
     * @param maxPrice Giá tối đa.
     * @return Danh sách sản phẩm trong khoảng giá cho trước.
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Tìm sản phẩm theo tên và khoảng giá.
     * 
     * @param name     Tên sản phẩm.
     * @param minPrice Giá tối thiểu.
     * @param maxPrice Giá tối đa.
     * @return Danh sách sản phẩm khớp với tiêu chí lọc.
     */
    List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, BigDecimal minPrice, BigDecimal maxPrice);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Có thể thêm các phương thức truy vấn khác nếu cần
}
