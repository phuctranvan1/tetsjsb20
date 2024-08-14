-- Tạo database SANPHAM
CREATE DATABASE SANPHAM;
GO

-- Sử dụng database SANPHAM
USE SANPHAM;
GO

-- Tạo bảng sanpham
CREATE TABLE sanpham (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    TenSanPham NVARCHAR(255) NOT NULL,
    GiaSanPham DECIMAL(18, 2) NOT NULL,
    GiaKhuyenMai DECIMAL(18, 2),
    HinhAnh NVARCHAR(MAX),
    MoTa NVARCHAR(MAX),
    DanhMucSanPham NVARCHAR(50) NOT NULL,
    TrangThai BIT NOT NULL DEFAULT 1
);
GO

-- Tạo ràng buộc CHECK cho DanhMucSanPham
ALTER TABLE sanpham
ADD CONSTRAINT CHK_DanhMucSanPham 
CHECK (DanhMucSanPham IN (N'Danh mục 1', N'Danh mục 2', N'Danh mục 3'));
GO

-- Tạo index cho tên sản phẩm để tối ưu tìm kiếm
CREATE INDEX IX_TenSanPham ON sanpham(TenSanPham);
GO

-- Tạo index cho giá sản phẩm để tối ưu tìm kiếm theo khoảng giá
CREATE INDEX IX_GiaSanPham ON sanpham(GiaSanPham);
GO