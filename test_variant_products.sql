-- Create the Color and Size attributes if they don't exist
INSERT INTO Product_attributes (name) 
SELECT 'Color' 
WHERE NOT EXISTS (SELECT 1 FROM Product_attributes WHERE name = 'Color');

INSERT INTO Product_attributes (name) 
SELECT 'Size' 
WHERE NOT EXISTS (SELECT 1 FROM Product_attributes WHERE name = 'Size');

-- Get attribute IDs for later use
DECLARE @ColorAttributeId INT = (SELECT attribute_id FROM Product_attributes WHERE name = 'Color');
DECLARE @SizeAttributeId INT = (SELECT attribute_id FROM Product_attributes WHERE name = 'Size');

-- Let's create 3 variants of a Betta fish product: Red Small, Red Medium, Blue Small
-- First, let's create the base products (if they don't exist already)

-- Create Red Small Betta
INSERT INTO Products (category_id, name, description, short_description, price, 
                      sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted)
SELECT 1, 'Betta Halfmoon', 'Beautiful halfmoon betta fish', 'Halfmoon Betta with vibrant colors', 150000, 
       120000, 10, 'BETTA-RED-S', 'active', 1, GETDATE(), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM Products WHERE sku = 'BETTA-RED-S');

-- Create Red Medium Betta
INSERT INTO Products (category_id, name, description, short_description, price, 
                      sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted)
SELECT 1, 'Betta Halfmoon', 'Beautiful halfmoon betta fish', 'Halfmoon Betta with vibrant colors', 180000, 
       150000, 8, 'BETTA-RED-M', 'active', 1, GETDATE(), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM Products WHERE sku = 'BETTA-RED-M');

-- Create Blue Small Betta
INSERT INTO Products (category_id, name, description, short_description, price, 
                      sale_price, quantity, sku, status, featured, created_at, updated_at, is_deleted)
SELECT 1, 'Betta Halfmoon', 'Beautiful halfmoon betta fish', 'Halfmoon Betta with vibrant colors', 160000, 
       130000, 5, 'BETTA-BLUE-S', 'active', 1, GETDATE(), GETDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM Products WHERE sku = 'BETTA-BLUE-S');

-- Now add attribute values for each product
-- Red Small Betta
DECLARE @RedSmallId INT = (SELECT product_id FROM Products WHERE sku = 'BETTA-RED-S');

-- Add Color attribute (Red)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @RedSmallId, @ColorAttributeId, 'Red', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @RedSmallId AND attribute_id = @ColorAttributeId
);

-- Add Size attribute (Small)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @RedSmallId, @SizeAttributeId, 'Small', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @RedSmallId AND attribute_id = @SizeAttributeId
);

-- Red Medium Betta
DECLARE @RedMediumId INT = (SELECT product_id FROM Products WHERE sku = 'BETTA-RED-M');

-- Add Color attribute (Red)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @RedMediumId, @ColorAttributeId, 'Red', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @RedMediumId AND attribute_id = @ColorAttributeId
);

-- Add Size attribute (Medium)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @RedMediumId, @SizeAttributeId, 'Medium', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @RedMediumId AND attribute_id = @SizeAttributeId
);

-- Blue Small Betta
DECLARE @BlueSmallId INT = (SELECT product_id FROM Products WHERE sku = 'BETTA-BLUE-S');

-- Add Color attribute (Blue)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @BlueSmallId, @ColorAttributeId, 'Blue', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @BlueSmallId AND attribute_id = @ColorAttributeId
);

-- Add Size attribute (Small)
INSERT INTO Product_attribute_values (product_id, attribute_id, value, is_deleted)
SELECT @BlueSmallId, @SizeAttributeId, 'Small', 0
WHERE NOT EXISTS (
    SELECT 1 FROM Product_attribute_values 
    WHERE product_id = @BlueSmallId AND attribute_id = @SizeAttributeId
);

-- Add product images for each variant (optional)
-- This would depend on your product_images table structure
-- Example:
-- INSERT INTO Product_images (product_id, image_url, is_main, created_at, updated_at, is_deleted)
-- VALUES (@RedSmallId, 'assets/images/home-04/betta_red_small.jpg', 1, GETDATE(), GETDATE(), 0); 