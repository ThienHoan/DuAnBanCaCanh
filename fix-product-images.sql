-- 🖼️ FIX PRODUCT IMAGES - DỰ ÁN BÁN CÁ CẢNH
-- Script kiểm tra và sửa lỗi ảnh sản phẩm

-- 1. KIỂM TRA SẢN PHẨM KHÔNG CÓ ẢNH
PRINT '=== KIỂM TRA SẢN PHẨM KHÔNG CÓ ẢNH ==='
SELECT 
    p.product_id,
    p.name,
    p.price,
    p.sale_price,
    COUNT(pi.image_id) as image_count,
    CASE 
        WHEN COUNT(pi.image_id) = 0 THEN 'NO_IMAGES'
        WHEN COUNT(CASE WHEN pi.is_main = 1 THEN 1 END) = 0 THEN 'NO_MAIN_IMAGE'
        ELSE 'OK'
    END as status
FROM Products p 
LEFT JOIN Product_images pi ON p.product_id = pi.product_id AND pi.is_deleted = 0
WHERE p.is_deleted = 0
GROUP BY p.product_id, p.name, p.price, p.sale_price
HAVING COUNT(pi.image_id) = 0 OR COUNT(CASE WHEN pi.is_main = 1 THEN 1 END) = 0
ORDER BY p.product_id;

-- 2. KIỂM TRA ẢNH CÓ PATH KHÔNG ĐÚNG
PRINT '=== KIỂM TRA ẢNH CÓ PATH KHÔNG ĐÚNG ==='
SELECT 
    p.product_id,
    p.name,
    pi.image_url,
    pi.is_main,
    CASE 
        WHEN pi.image_url IS NULL OR pi.image_url = '' THEN 'EMPTY_PATH'
        WHEN pi.image_url NOT LIKE 'assets/images/products/%' THEN 'WRONG_PATH'
        ELSE 'OK'
    END as path_status
FROM Products p 
INNER JOIN Product_images pi ON p.product_id = pi.product_id 
WHERE pi.is_deleted = 0 
AND (pi.image_url IS NULL OR pi.image_url = '' OR pi.image_url NOT LIKE 'assets/images/products/%')
ORDER BY p.product_id;

-- 3. KIỂM TRA SẢN PHẨM CÓ SALE PRICE (CÁC SẢN PHẨM TRONG HÌNH)
PRINT '=== KIỂM TRA SẢN PHẨM CÓ SALE PRICE ==='
SELECT 
    p.product_id,
    p.name,
    p.price,
    p.sale_price,
    COUNT(pi.image_id) as image_count,
    pi.image_url
FROM Products p 
LEFT JOIN Product_images pi ON p.product_id = pi.product_id AND pi.is_deleted = 0 AND pi.is_main = 1
WHERE p.is_deleted = 0 
AND p.sale_price > 0
ORDER BY p.product_id;

-- 4. TÌM SẢN PHẨM "Cá Neon Tetra" VÀ "Tetra Vacation Feeder"
PRINT '=== TÌM SẢN PHẨM CỤ THỂ ==='
SELECT 
    p.product_id,
    p.name,
    p.price,
    p.sale_price,
    COUNT(pi.image_id) as image_count,
    pi.image_url
FROM Products p 
LEFT JOIN Product_images pi ON p.product_id = pi.product_id AND pi.is_deleted = 0 AND pi.is_main = 1
WHERE p.is_deleted = 0 
AND (p.name LIKE '%Neon Tetra%' OR p.name LIKE '%Tetra Vacation%')
ORDER BY p.product_id;

-- 5. SỬA LỖI: THÊM ẢNH MẶC ĐỊNH CHO SẢN PHẨM KHÔNG CÓ ẢNH
PRINT '=== THÊM ẢNH MẶC ĐỊNH CHO SẢN PHẨM KHÔNG CÓ ẢNH ==='

-- Tạo temporary table để lưu sản phẩm cần thêm ảnh
DECLARE @ProductsNeedingImages TABLE (
    product_id INT,
    product_name NVARCHAR(255)
);

-- Lấy danh sách sản phẩm không có ảnh
INSERT INTO @ProductsNeedingImages
SELECT p.product_id, p.name
FROM Products p 
LEFT JOIN Product_images pi ON p.product_id = pi.product_id AND pi.is_deleted = 0
WHERE p.is_deleted = 0
GROUP BY p.product_id, p.name
HAVING COUNT(pi.image_id) = 0;

-- Hiển thị sản phẩm cần thêm ảnh
SELECT * FROM @ProductsNeedingImages;

-- Thêm ảnh mặc định cho sản phẩm không có ảnh
-- (Uncomment để thực hiện)
/*
INSERT INTO Product_images (product_id, image_url, is_main, display_order, is_deleted)
SELECT 
    p.product_id,
    'assets/images/products/p-01.jpg',
    1,
    1,
    0
FROM @ProductsNeedingImages p;
*/

-- 6. SỬA LỖI: CẬP NHẬT PATH ẢNH KHÔNG ĐÚNG
PRINT '=== CẬP NHẬT PATH ẢNH KHÔNG ĐÚNG ==='

-- Hiển thị ảnh có path không đúng
SELECT 
    pi.image_id,
    p.name,
    pi.image_url,
    'assets/images/products/' + RIGHT(pi.image_url, CHARINDEX('/', REVERSE(pi.image_url)) - 1) as suggested_path
FROM Products p 
INNER JOIN Product_images pi ON p.product_id = pi.product_id 
WHERE pi.is_deleted = 0 
AND pi.image_url IS NOT NULL 
AND pi.image_url != '' 
AND pi.image_url NOT LIKE 'assets/images/products/%'
ORDER BY p.product_id;

-- Cập nhật path ảnh (Uncomment để thực hiện)
/*
UPDATE Product_images 
SET image_url = 'assets/images/products/' + RIGHT(image_url, CHARINDEX('/', REVERSE(image_url)) - 1)
WHERE is_deleted = 0 
AND image_url IS NOT NULL 
AND image_url != '' 
AND image_url NOT LIKE 'assets/images/products/%';
*/

PRINT '=== HOÀN THÀNH KIỂM TRA ==='
PRINT 'Để sửa lỗi, uncomment các phần INSERT và UPDATE ở trên' 