# 🖼️ FIX PRODUCT IMAGES - DỰ ÁN BÁN CÁ CẢNH
# Script PowerShell để kiểm tra và sửa lỗi ảnh sản phẩm

Write-Host "=== KIỂM TRA VÀ SỬA LỖI ẢNH SẢN PHẨM ===" -ForegroundColor Green

# 1. Kiểm tra file no-image.jpg
Write-Host "`n1. Kiểm tra file no-image.jpg..." -ForegroundColor Yellow
$noImagePath = "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\assets\images\no-image.jpg"
if (Test-Path $noImagePath) {
    Write-Host "✅ File no-image.jpg tồn tại" -ForegroundColor Green
} else {
    Write-Host "❌ File no-image.jpg không tồn tại" -ForegroundColor Red
    Write-Host "Đang tạo file no-image.jpg..." -ForegroundColor Yellow
    Copy-Item "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\assets\images\404-bg.jpg" $noImagePath
    Write-Host "✅ Đã tạo file no-image.jpg" -ForegroundColor Green
}

# 2. Kiểm tra thư mục products
Write-Host "`n2. Kiểm tra thư mục products..." -ForegroundColor Yellow
$productsPath = "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\assets\images\products"
if (Test-Path $productsPath) {
    $productImages = Get-ChildItem $productsPath -Filter "*.jpg" | Select-Object Name
    Write-Host "✅ Thư mục products tồn tại với $($productImages.Count) ảnh" -ForegroundColor Green
    Write-Host "Các ảnh có sẵn:" -ForegroundColor Cyan
    $productImages | ForEach-Object { Write-Host "  - $($_.Name)" -ForegroundColor Gray }
} else {
    Write-Host "❌ Thư mục products không tồn tại" -ForegroundColor Red
}

# 3. Kiểm tra JSP files
Write-Host "`n3. Kiểm tra JSP files..." -ForegroundColor Yellow
$jspFiles = @(
    "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\home.jsp",
    "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\category1.jsp",
    "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\product_detail.jsp"
)

foreach ($file in $jspFiles) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw
        if ($content -match "no-image\.jpg") {
            Write-Host "✅ $($file.Split('\')[-1]) - Có reference đến no-image.jpg" -ForegroundColor Green
        } else {
            Write-Host "⚠️ $($file.Split('\')[-1]) - Không có reference đến no-image.jpg" -ForegroundColor Yellow
        }
    } else {
        Write-Host "❌ $($file.Split('\')[-1]) - File không tồn tại" -ForegroundColor Red
    }
}

# 4. Tạo script SQL để kiểm tra database
Write-Host "`n4. Tạo script SQL để kiểm tra database..." -ForegroundColor Yellow
$sqlScript = @"
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

-- 2. TÌM SẢN PHẨM "Cá Neon Tetra" VÀ "Tetra Vacation Feeder"
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

-- 3. THÊM ẢNH MẶC ĐỊNH CHO SẢN PHẨM KHÔNG CÓ ẢNH
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

PRINT '=== HOÀN THÀNH KIỂM TRA ==='
PRINT 'Để sửa lỗi, uncomment phần INSERT ở trên'
"@

$sqlScriptPath = "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\fix-product-images.sql"
$sqlScript | Out-File -FilePath $sqlScriptPath -Encoding UTF8
Write-Host "✅ Đã tạo script SQL: $sqlScriptPath" -ForegroundColor Green

# 5. Tóm tắt
Write-Host "`n=== TÓM TẮT ===" -ForegroundColor Green
Write-Host "✅ Đã kiểm tra file no-image.jpg" -ForegroundColor Green
Write-Host "✅ Đã kiểm tra thư mục products" -ForegroundColor Green
Write-Host "✅ Đã kiểm tra JSP files" -ForegroundColor Green
Write-Host "✅ Đã tạo script SQL để kiểm tra database" -ForegroundColor Green

Write-Host "`n📋 CÁC BƯỚC TIẾP THEO:" -ForegroundColor Cyan
Write-Host "1. Chạy script SQL để kiểm tra database" -ForegroundColor White
Write-Host "2. Thêm ảnh cho sản phẩm không có ảnh" -ForegroundColor White
Write-Host "3. Test lại trang web" -ForegroundColor White

Write-Host "`n🎯 Để sửa lỗi hoàn toàn, cần:" -ForegroundColor Yellow
Write-Host "- Kiểm tra database Product_images" -ForegroundColor White
Write-Host "- Thêm ảnh cho sản phẩm 'Cá Neon Tetra' và 'Tetra Vacation Feeder'" -ForegroundColor White
Write-Host "- Đảm bảo path ảnh đúng format: 'assets/images/products/filename.jpg'" -ForegroundColor White

Write-Host "`n=== HOÀN THÀNH ===" -ForegroundColor Green 