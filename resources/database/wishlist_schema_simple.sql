-- Tạo bảng wishlist cho chức năng yêu thích sản phẩm (SQL Server - Simple Version)
-- File này có thể chạy trực tiếp trên SQL Server Management Studio

-- Tạo bảng wishlist
CREATE TABLE wishlist (
    wishlist_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    
    -- Ràng buộc foreign key
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    
    -- Đảm bảo một user chỉ có thể thêm một sản phẩm vào wishlist một lần
    CONSTRAINT UQ_wishlist_user_product UNIQUE(user_id, product_id)
);

-- Tạo index để tối ưu hóa truy vấn
CREATE INDEX idx_wishlist_user_id ON wishlist(user_id);
CREATE INDEX idx_wishlist_product_id ON wishlist(product_id);
CREATE INDEX idx_wishlist_created_at ON wishlist(created_at);

-- Tạo view để lấy thông tin wishlist với thông tin sản phẩm
CREATE VIEW v_wishlist_details AS
SELECT 
    w.wishlist_id,
    w.user_id,
    w.product_id,
    w.created_at,
    p.product_name,
    p.description,
    p.price,
    p.image_url,
    p.stock_quantity,
    p.is_active as product_active,
    pc.category_name
FROM wishlist w
LEFT JOIN products p ON w.product_id = p.product_id
LEFT JOIN product_categories pc ON p.category_id = pc.category_id
WHERE p.is_active = 1;
