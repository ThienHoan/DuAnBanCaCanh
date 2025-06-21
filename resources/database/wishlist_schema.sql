-- =====================================================
-- WISHLIST SCHEMA FOR SQL SERVER
-- Tạo bảng wishlist cho chức năng yêu thích sản phẩm
-- File này có thể chạy trực tiếp trên SQL Server Management Studio
-- =====================================================

-- Kiểm tra và xóa các objects nếu tồn tại (để test)
IF OBJECT_ID('dbo.sp_IsInWishlist', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_IsInWishlist;
GO

IF OBJECT_ID('dbo.sp_GetWishlistCount', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_GetWishlistCount;
GO

IF OBJECT_ID('dbo.sp_GetUserWishlist', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_GetUserWishlist;
GO

IF OBJECT_ID('dbo.sp_RemoveFromWishlist', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_RemoveFromWishlist;
GO

IF OBJECT_ID('dbo.sp_AddToWishlist', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_AddToWishlist;
GO

IF OBJECT_ID('dbo.TR_wishlist_updated_at', 'TR') IS NOT NULL
    DROP TRIGGER dbo.TR_wishlist_updated_at;
GO

IF OBJECT_ID('dbo.v_wishlist_details', 'V') IS NOT NULL
    DROP VIEW dbo.v_wishlist_details;
GO

IF OBJECT_ID('dbo.wishlist', 'U') IS NOT NULL
    DROP TABLE dbo.wishlist;
GO

-- Tạo bảng wishlist
CREATE TABLE dbo.wishlist (
    wishlist_id INT IDENTITY(1,1) NOT NULL,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at DATETIME2(7) NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2(7) NOT NULL DEFAULT GETDATE(),
    
    -- Primary Key
    CONSTRAINT PK_wishlist PRIMARY KEY CLUSTERED (wishlist_id),
    
    -- Foreign Keys (sẽ được thêm sau khi kiểm tra tồn tại bảng tham chiếu)
    -- CONSTRAINT FK_wishlist_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    -- CONSTRAINT FK_wishlist_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    
    -- Unique constraint - một user chỉ có thể thêm một sản phẩm vào wishlist một lần
    CONSTRAINT UQ_wishlist_user_product UNIQUE NONCLUSTERED (user_id, product_id)
);
GO

-- Tạo các index để tối ưu hóa truy vấn
CREATE NONCLUSTERED INDEX IX_wishlist_user_id 
ON dbo.wishlist(user_id)
INCLUDE (product_id, created_at);
GO

CREATE NONCLUSTERED INDEX IX_wishlist_product_id 
ON dbo.wishlist(product_id)
INCLUDE (user_id, created_at);
GO

CREATE NONCLUSTERED INDEX IX_wishlist_created_at 
ON dbo.wishlist(created_at DESC)
INCLUDE (user_id, product_id);
GO

CREATE NONCLUSTERED INDEX IX_wishlist_user_created 
ON dbo.wishlist(user_id, created_at DESC)
INCLUDE (product_id);
GO

-- Tạo trigger để tự động cập nhật updated_at
CREATE TRIGGER TR_wishlist_updated_at
ON dbo.wishlist
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    UPDATE dbo.wishlist 
    SET updated_at = GETDATE()
    FROM dbo.wishlist w
    INNER JOIN inserted i ON w.wishlist_id = i.wishlist_id;
END;
GO

-- Tạo view để lấy thông tin wishlist với thông tin sản phẩm
CREATE VIEW dbo.v_wishlist_details AS
SELECT 
    w.wishlist_id,
    w.user_id,
    w.product_id,
    w.created_at,
    w.updated_at,
    p.name as product_name,
    p.description,
    p.price,
    p.sale_price,
    p.quantity as stock_quantity,
    p.status as product_status,
    p.featured,
    c.name as category_name,
    u.username,
    u.full_name as user_full_name
FROM dbo.wishlist w
LEFT JOIN dbo.Products p ON w.product_id = p.product_id
LEFT JOIN dbo.Categories c ON p.category_id = c.category_id
LEFT JOIN dbo.Users u ON w.user_id = u.user_id
WHERE (p.status = 'active' OR p.status IS NULL) AND (p.is_deleted = 0 OR p.is_deleted IS NULL);
GO

-- Thêm Foreign Keys (bỏ comment nếu các bảng tham chiếu đã tồn tại)
/*
ALTER TABLE dbo.wishlist
ADD CONSTRAINT FK_wishlist_user 
FOREIGN KEY (user_id) REFERENCES dbo.Users(user_id) ON DELETE CASCADE;
GO

ALTER TABLE dbo.wishlist
ADD CONSTRAINT FK_wishlist_product 
FOREIGN KEY (product_id) REFERENCES dbo.Products(product_id) ON DELETE CASCADE;
GO
*/

-- =====================================================
-- STORED PROCEDURES CHO WISHLIST
-- =====================================================

-- Procedure thêm sản phẩm vào wishlist
CREATE PROCEDURE sp_AddToWishlist
    @user_id INT,
    @product_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    BEGIN TRY
        INSERT INTO dbo.wishlist (user_id, product_id)
        VALUES (@user_id, @product_id);
        
        SELECT 'SUCCESS' as Status, 'Product added to wishlist' as Message;
    END TRY
    BEGIN CATCH
        IF ERROR_NUMBER() = 2627 -- Unique constraint violation
            SELECT 'ERROR' as Status, 'Product already in wishlist' as Message;
        ELSE
            SELECT 'ERROR' as Status, ERROR_MESSAGE() as Message;
    END CATCH
END;
GO

-- Procedure xóa sản phẩm khỏi wishlist
CREATE PROCEDURE sp_RemoveFromWishlist
    @user_id INT,
    @product_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    DELETE FROM dbo.wishlist 
    WHERE user_id = @user_id AND product_id = @product_id;
    
    IF @@ROWCOUNT > 0
        SELECT 'SUCCESS' as Status, 'Product removed from wishlist' as Message;
    ELSE
        SELECT 'ERROR' as Status, 'Product not found in wishlist' as Message;
END;
GO

-- Procedure lấy wishlist của user
CREATE PROCEDURE sp_GetUserWishlist
    @user_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        wishlist_id,
        user_id,
        product_id,
        created_at,
        updated_at,
        product_name,
        description,
        price,
        sale_price,
        stock_quantity,
        product_status,
        featured,
        category_name
    FROM dbo.v_wishlist_details
    WHERE user_id = @user_id
    ORDER BY created_at DESC;
END;
GO

-- Procedure đếm số lượng wishlist của user
CREATE PROCEDURE sp_GetWishlistCount
    @user_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT COUNT(*) as wishlist_count
    FROM dbo.wishlist
    WHERE user_id = @user_id;
END;
GO

-- Procedure kiểm tra sản phẩm có trong wishlist không
CREATE PROCEDURE sp_IsInWishlist
    @user_id INT,
    @product_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    IF EXISTS (SELECT 1 FROM dbo.wishlist WHERE user_id = @user_id AND product_id = @product_id)
        SELECT 1 as is_in_wishlist;
    ELSE
        SELECT 0 as is_in_wishlist;
END;
GO

-- =====================================================
-- DỮ LIỆU MẪU (TÙY CHỌN)
-- =====================================================

-- Thêm dữ liệu mẫu cho test (bỏ comment nếu cần)
/*
INSERT INTO dbo.wishlist (user_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 2);
*/

-- =====================================================
-- THÔNG BÁO HOÀN THÀNH
-- =====================================================
PRINT 'Wishlist schema created successfully!';
PRINT 'Tables: wishlist';
PRINT 'Views: v_wishlist_details';
PRINT 'Indexes: IX_wishlist_user_id, IX_wishlist_product_id, IX_wishlist_created_at, IX_wishlist_user_created';
PRINT 'Trigger: TR_wishlist_updated_at';
PRINT 'Stored Procedures: sp_AddToWishlist, sp_RemoveFromWishlist, sp_GetUserWishlist, sp_GetWishlistCount, sp_IsInWishlist';
PRINT 'Note: Uncomment Foreign Key constraints if users and products tables exist';
