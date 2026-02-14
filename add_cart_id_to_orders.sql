-- Thêm cột cart_id vào bảng Orders
ALTER TABLE Orders ADD cart_id INT NULL;

-- Thêm khóa ngoại tham chiếu đến bảng Carts (tùy chọn)
-- ALTER TABLE Orders ADD CONSTRAINT FK_Orders_Carts FOREIGN KEY (cart_id) REFERENCES Carts(cart_id);

-- Cập nhật OrderDAOImpl để không sử dụng cart_id nếu cột chưa được thêm
-- Xem OrderDAOImpl.java 