-- Script để cập nhật một số sản phẩm thành Featured (nổi bật)
-- Chạy script này để test tab Featured

-- Đánh dấu một số sản phẩm có sẵn thành Featured
UPDATE products 
SET featured = 1, updated_at = GETDATE()
WHERE product_id IN (1, 2, 4);  -- Cá Betta Fancy, Cá Guppy, Tỉnh dầu trầm

-- Kiểm tra kết quả
SELECT product_id, name, price, sale_price, featured, status 
FROM products 
WHERE featured = 1 AND status = 'active'
ORDER BY product_id;

-- Hiển thị tất cả sản phẩm để so sánh
SELECT product_id, name, price, sale_price, featured, status 
FROM products 
WHERE status = 'active'
ORDER BY featured DESC, product_id;
