# Script test chức năng thay đổi ảnh hero
# Chỉ hiển thị thông tin, không thực sự thay đổi file

Write-Host "🧪 TEST SCRIPT THAY ĐỔI ẢNH HERO" -ForegroundColor Green
Write-Host ""

# Kiểm tra thư mục images
$ImagesPath = "..\images"
$TargetPath = "$ImagesPath\hero_bg.jpg"

Write-Host "📁 Kiểm tra thư mục images..." -ForegroundColor Yellow
if (Test-Path $ImagesPath) {
    Write-Host "✅ Thư mục images tồn tại: $ImagesPath" -ForegroundColor Green
} else {
    Write-Host "❌ Thư mục images không tồn tại: $ImagesPath" -ForegroundColor Red
}

Write-Host ""
Write-Host "🖼️ Kiểm tra file hero_bg.jpg..." -ForegroundColor Yellow
if (Test-Path $TargetPath) {
    $File = Get-Item $TargetPath
    Write-Host "✅ File hero_bg.jpg tồn tại:" -ForegroundColor Green
    Write-Host "   Tên: $($File.Name)" -ForegroundColor White
    Write-Host "   Kích thước: $([math]::Round($File.Length/1KB, 2)) KB" -ForegroundColor White
    Write-Host "   Ngày tạo: $($File.CreationTime)" -ForegroundColor White
} else {
    Write-Host "❌ File hero_bg.jpg không tồn tại" -ForegroundColor Red
}

Write-Host ""
Write-Host "🔧 Kiểm tra script..." -ForegroundColor Yellow
$Scripts = @("quick-change-hero.ps1", "change-hero-image.ps1")
foreach ($Script in $Scripts) {
    if (Test-Path $Script) {
        Write-Host "✅ Script $Script tồn tại" -ForegroundColor Green
    } else {
        Write-Host "❌ Script $Script không tồn tại" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "📋 Hướng dẫn sử dụng:" -ForegroundColor Cyan
Write-Host "1. Chuẩn bị ảnh mới (JPG/PNG)" -ForegroundColor White
Write-Host "2. Chạy: .\quick-change-hero.ps1 'đường_dẫn_ảnh_mới'" -ForegroundColor White
Write-Host "3. Kiểm tra website" -ForegroundColor White

Write-Host ""
Write-Host "🎯 Ví dụ:" -ForegroundColor Yellow
Write-Host "   .\quick-change-hero.ps1 'C:\Users\Admin\Desktop\hero_new.jpg'" -ForegroundColor White 