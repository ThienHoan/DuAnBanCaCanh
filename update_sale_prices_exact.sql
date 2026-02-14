-- Update sale prices for products based on the admin panel image
-- This script updates the sale_price column to match the promotion prices shown

-- Update Cá Betta Fancy (ID = 1) - Sale price: 90,000đ
UPDATE Products 
SET sale_price = 90000.00 
WHERE product_id = 1 AND name LIKE '%Betta%';

-- Update Cá Guppy (ID = 2) - Sale price: 25,000đ  
UPDATE Products 
SET sale_price = 25000.00 
WHERE product_id = 2 AND name LIKE '%Guppy%';

-- Update Máy lọc nước mini (ID = 3) - Sale price: 180,000đ
UPDATE Products 
SET sale_price = 180000.00 
WHERE product_id = 3 AND name LIKE '%lọc nước%';

-- Set sale_price to NULL for products without promotion
UPDATE Products 
SET sale_price = NULL 
WHERE product_id IN (4, 6) OR name LIKE '%Tinh dầu%' OR name LIKE '%ZXCZC%';

-- Verify the updates by selecting all products with their sale information
SELECT 
    product_id as ID,
    name as 'TÊN SẢN PHẨM',
    price as 'GIÁ GỐC',
    sale_price as 'GIÁ KHUYẾN MÃI',
    sku,
    status,
    CASE 
        WHEN sale_price IS NOT NULL AND sale_price > 0 THEN 
            CONCAT(ROUND(((price - sale_price) / price) * 100, 0), '%')
        ELSE 'Không giảm giá'
    END as 'PHẦN TRĂM GIẢM'
FROM Products 
WHERE is_deleted = 0 OR is_deleted IS NULL
ORDER BY product_id;

-- Count products with sale prices (for "On Sale" tab)
SELECT 
    COUNT(*) as 'Số sản phẩm có khuyến mãi',
    COUNT(CASE WHEN sale_price IS NULL OR sale_price = 0 THEN 1 END) as 'Số sản phẩm không khuyến mãi'
FROM Products 
WHERE (is_deleted = 0 OR is_deleted IS NULL) AND (status = 1 OR status = 'active');

-- Show products that will appear in "On Sale" tab
SELECT 
    product_id,
    name,
    price,
    sale_price,
    CONCAT(ROUND(((price - sale_price) / price) * 100, 0), '% OFF') as discount
FROM Products 
WHERE (is_deleted = 0 OR is_deleted IS NULL) 
    AND (status = 1 OR status = 'active')
    AND sale_price IS NOT NULL 
    AND sale_price > 0
ORDER BY ((price - sale_price) / price) DESC;
