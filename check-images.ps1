# Check Product Images
Write-Host "=== CHECK PRODUCT IMAGES ===" -ForegroundColor Green

# 1. Check no-image.jpg
Write-Host "`n1. Checking no-image.jpg..." -ForegroundColor Yellow
$noImagePath = "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\assets\images\no-image.jpg"
if (Test-Path $noImagePath) {
    Write-Host "OK: File no-image.jpg exists" -ForegroundColor Green
} else {
    Write-Host "ERROR: File no-image.jpg missing" -ForegroundColor Red
}

# 2. Check products directory
Write-Host "`n2. Checking products directory..." -ForegroundColor Yellow
$productsPath = "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\assets\images\products"
if (Test-Path $productsPath) {
    $productImages = Get-ChildItem $productsPath -Filter "*.jpg" | Select-Object Name
    Write-Host "OK: Products directory exists with $($productImages.Count) images" -ForegroundColor Green
} else {
    Write-Host "ERROR: Products directory missing" -ForegroundColor Red
}

# 3. Check JSP files
Write-Host "`n3. Checking JSP files..." -ForegroundColor Yellow
$jspFiles = @(
    "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\home.jsp",
    "C:\Users\Admin\Documents\swp\DuAnBanCaCanh\web\category1.jsp"
)

foreach ($file in $jspFiles) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw
        if ($content -match "no-image\.jpg") {
            Write-Host "OK: $($file.Split('\')[-1]) - Has no-image.jpg reference" -ForegroundColor Green
        } else {
            Write-Host "WARNING: $($file.Split('\')[-1]) - No no-image.jpg reference" -ForegroundColor Yellow
        }
    } else {
        Write-Host "ERROR: $($file.Split('\')[-1]) - File missing" -ForegroundColor Red
    }
}

Write-Host "`n=== SUMMARY ===" -ForegroundColor Green
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Check database Product_images table" -ForegroundColor White
Write-Host "2. Add images for products without images" -ForegroundColor White
Write-Host "3. Test website again" -ForegroundColor White

Write-Host "`n=== COMPLETED ===" -ForegroundColor Green 