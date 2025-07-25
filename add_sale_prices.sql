-- Add sale prices to products for testing "On Sale" functionality
-- Based on the product management data shown

-- Update Cá Betta Fancy (productId = 1) - From 100,000 to 90,000
UPDATE Products 
SET salePrice = 90000.00 
WHERE name LIKE '%Betta Fancy%' OR productId = 1;

-- Update Cá Guppy (productId = 2) - From 30,000 to 25,000  
UPDATE Products 
SET salePrice = 25000.00 
WHERE name LIKE '%Guppy%' OR productId = 2;

-- Update Máy lọc nước mini (productId = 3) - From 200,000 to 180,000
UPDATE Products 
SET salePrice = 180000.00 
WHERE name LIKE '%lọc nước%' OR productId = 3;

-- Also update any other products that might need sale prices
-- Tinh dầu trầm - no sale price (0đ in image)
UPDATE Products 
SET salePrice = NULL 
WHERE name LIKE '%Tinh dầu trầm%' OR productId = 4;

-- ZXCZC - no sale price (0đ in image)  
UPDATE Products 
SET salePrice = NULL 
WHERE name LIKE '%ZXCZC%' OR productId = 6;

-- Verify the changes
SELECT 
    productId,
    name,
    price as originalPrice,
    salePrice,
    CASE 
        WHEN salePrice IS NOT NULL AND salePrice > 0 THEN 
            ROUND(((price - salePrice) / price) * 100, 2)
        ELSE 0
    END as discountPercent
FROM Products 
WHERE isActive = 1
ORDER BY productId;

-- Check if we have products with sale prices for "On Sale" tab
SELECT COUNT(*) as onSaleProductsCount
FROM Products 
WHERE isActive = 1 AND salePrice IS NOT NULL AND salePrice > 0;
