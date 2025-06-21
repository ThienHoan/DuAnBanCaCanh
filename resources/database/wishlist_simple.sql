-- =====================================================
-- WISHLIST TABLE - SIMPLE VERSION FOR SQL SERVER
-- Chỉ tạo bảng cơ bản, không có trigger, stored procedure
-- Phù hợp với database fishshopp hiện tại
-- =====================================================

-- Tạo bảng wishlist đơn giản
CREATE TABLE dbo.wishlist (
    wishlist_id INT IDENTITY(1,1) NOT NULL,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at DATETIME2(7) NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2(7) NOT NULL DEFAULT GETDATE(),
    
    -- Primary Key
    CONSTRAINT PK_wishlist PRIMARY KEY CLUSTERED (wishlist_id),
    
    -- Unique constraint
    CONSTRAINT UQ_wishlist_user_product UNIQUE NONCLUSTERED (user_id, product_id),
    
    -- Foreign Keys (tương thích với database hiện tại)
    CONSTRAINT FK_wishlist_user FOREIGN KEY (user_id) REFERENCES dbo.Users(user_id) ON DELETE CASCADE,
    CONSTRAINT FK_wishlist_product FOREIGN KEY (product_id) REFERENCES dbo.Products(product_id) ON DELETE CASCADE
);

-- Tạo index cơ bản
CREATE NONCLUSTERED INDEX IX_wishlist_user_id ON dbo.wishlist(user_id);
CREATE NONCLUSTERED INDEX IX_wishlist_product_id ON dbo.wishlist(product_id);

-- Thông báo
PRINT 'Simple wishlist table created successfully!';
