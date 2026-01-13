-- Debug script for cart functionality
-- User ID = 4 (người đang đăng nhập)
-- Product ID có thể là bất kỳ số nào (sản phẩm muốn mua)

-- 1. Check if user exists
SELECT 'USER CHECK' as step, user_id, username, email FROM Users WHERE user_id = 4;

-- 2. Check if cart exists for user 4  
SELECT 'CART CHECK' as step, * FROM Carts WHERE user_id = 4;

-- 3. Check all available products (you can buy any of these)
SELECT 'AVAILABLE PRODUCTS' as step, product_id, name, price FROM Products WHERE product_id IN (1,2,3,4,5,6,7,8,9,10);

-- 4. Check all carts in system
SELECT 'ALL CARTS' as step, cart_id, user_id, created_at FROM Carts ORDER BY cart_id DESC;

-- 5. Check all cart items
SELECT 'ALL CART ITEMS' as step, cart_item_id, cart_id, product_id, quantity FROM Cart_items ORDER BY cart_item_id DESC;

-- 6. Try to manually create a cart for user 4 (if not exists)
IF NOT EXISTS (SELECT 1 FROM Carts WHERE user_id = 4)
BEGIN
    INSERT INTO Carts (user_id, created_at, updated_at) VALUES (4, GETDATE(), GETDATE());
    SELECT 'CART CREATED FOR USER 4' as step, @@IDENTITY as new_cart_id;
END
ELSE
BEGIN
    SELECT 'CART ALREADY EXISTS FOR USER 4' as step, cart_id FROM Carts WHERE user_id = 4;
END
