-- Test script để kiểm tra logic phân trang theo nhóm sản phẩm
-- Mỗi nhóm tên sản phẩm được tính là 1 đơn vị phân trang

-- 1. Xem tất cả sản phẩm và nhóm theo tên
SELECT 
    name,
    COUNT(*) as product_count,
    STRING_AGG(CAST(product_id AS VARCHAR), ', ') as product_ids
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
GROUP BY name
ORDER BY name;

-- 2. Đếm tổng số nhóm sản phẩm
SELECT COUNT(DISTINCT name) as total_groups
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0;

-- 3. Mô phỏng phân trang theo nhóm (pageSize = 12)
WITH GroupedProducts AS (
    SELECT 
        name,
        COUNT(*) as product_count,
        STRING_AGG(CAST(product_id AS VARCHAR), ', ') as product_ids,
        ROW_NUMBER() OVER (ORDER BY name) as group_number
    FROM Products 
    WHERE category_id = 1 
        AND is_deleted = 0
    GROUP BY name
)
SELECT 
    group_number,
    name,
    product_count,
    product_ids,
    CASE 
        WHEN group_number <= 12 THEN 'Trang 1'
        WHEN group_number <= 24 THEN 'Trang 2'
        WHEN group_number <= 36 THEN 'Trang 3'
        ELSE 'Trang ' + CAST(((group_number - 1) / 12) + 1 AS VARCHAR)
    END as page_number
FROM GroupedProducts
ORDER BY name;

-- 4. Xem sản phẩm trong trang 1 (12 nhóm đầu tiên)
WITH GroupedProducts AS (
    SELECT 
        name,
        ROW_NUMBER() OVER (ORDER BY name) as group_number
    FROM Products 
    WHERE category_id = 1 
        AND is_deleted = 0
    GROUP BY name
),
Page1Groups AS (
    SELECT name 
    FROM GroupedProducts 
    WHERE group_number <= 12
)
SELECT 
    p.product_id,
    p.name,
    p.price,
    p.sale_price
FROM Products p
JOIN Page1Groups pg ON p.name = pg.name
WHERE p.category_id = 1 
    AND p.is_deleted = 0
ORDER BY p.name ASC, p.product_id ASC;

-- 5. Test với tìm kiếm
SELECT 
    name,
    COUNT(*) as product_count
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
    AND name LIKE '%cá%'
GROUP BY name
ORDER BY name;

-- 6. Đếm số nhóm sau khi tìm kiếm
SELECT COUNT(DISTINCT name) as total_groups_after_search
FROM Products 
WHERE category_id = 1 
    AND is_deleted = 0
    AND name LIKE '%cá%'; 