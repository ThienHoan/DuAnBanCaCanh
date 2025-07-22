-- Tạo bảng Notifications nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Notifications')
BEGIN
    CREATE TABLE Notifications (
        notification_id INT PRIMARY KEY IDENTITY(1,1),
        user_id INT NOT NULL,
        reference_id INT,  -- ID của đối tượng liên quan (đơn hàng, sản phẩm, ...)
        reference_type NVARCHAR(50), -- Loại đối tượng liên quan (order, product, ...)
        message NVARCHAR(500) NOT NULL, -- Nội dung thông báo
        type NVARCHAR(50) NOT NULL, -- Loại thông báo (payment_success, order_status, ...)
        is_read BIT DEFAULT 0, -- Đã đọc hay chưa
        created_at DATETIME DEFAULT GETDATE(), -- Thời gian tạo
        FOREIGN KEY (user_id) REFERENCES Users(user_id)
    );
    
    PRINT 'Notifications table created successfully';
END
ELSE
BEGIN
    PRINT 'Notifications table already exists';
END 