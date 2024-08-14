package com.example.productmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ khi không tìm thấy tài nguyên.
 * Được sử dụng để trả về mã lỗi 404 Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor không tham số.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructor với thông điệp lỗi.
     * 
     * @param message Thông điệp lỗi.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor với thông điệp lỗi và nguyên nhân.
     * 
     * @param message Thông điệp lỗi.
     * @param cause   Nguyên nhân của lỗi.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor với nguyên nhân.
     * 
     * @param cause Nguyên nhân của lỗi.
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
