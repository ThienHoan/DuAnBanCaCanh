-- Test script để kiểm tra logic nhóm sản phẩm theo tên
-- Chạy script này để xem các sản phẩm có cùng tên có được nhóm lại không

-- 1. Xem tất cả sản phẩm trong một category
SELECT 
    product_id,
    category_id,
    name,
    price,
    sale_price,
    quantity,
    status
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
ORDER BY name ASC, product_id ASC;

-- 2. Đếm số lượng sản phẩm có cùng tên
SELECT 
    name,
    COUNT(*) as product_count,
    STRING_AGG(CAST(product_id AS VARCHAR), ', ') as product_ids
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
GROUP BY name
HAVING COUNT(*) > 1
ORDER BY name;

-- 3. Xem sản phẩm có cùng tên với các thuộc tính khác nhau
SELECT 
    p1.product_id as id1,
    p1.name,
    p1.price as price1,
    p1.sale_price as sale_price1,
    p2.product_id as id2,
    p2.price as price2,
    p2.sale_price as sale_price2
FROM Products p1
JOIN Products p2 ON p1.name = p2.name 
    AND p1.product_id < p2.product_id
WHERE p1.category_id = 1 
    AND p1.is_deleted = 0
    AND p2.category_id = 1 
    AND p2.is_deleted = 0
ORDER BY p1.name, p1.product_id;

-- 4. Test pagination với nhóm sản phẩm
-- Lấy 10 sản phẩm đầu tiên sau khi nhóm theo tên
WITH GroupedProducts AS (
    SELECT 
        product_id,
        category_id,
        name,
        price,
        sale_price,
        quantity,
        status,
        ROW_NUMBER() OVER (ORDER BY name ASC, product_id ASC) as rn
    FROM Products 
    WHERE category_id = 1 
        AND is_deleted = 0
)
SELECT * FROM GroupedProducts 
WHERE rn BETWEEN 1 AND 10
ORDER BY name ASC, product_id ASC;

-- 5. Test với tìm kiếm
SELECT 
    product_id,
    category_id,
    name,
    price,
    sale_price
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
    AND name LIKE '%cá%'
ORDER BY name ASC, product_id ASC; 