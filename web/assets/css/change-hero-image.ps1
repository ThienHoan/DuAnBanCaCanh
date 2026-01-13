# Script thay đổi ảnh hero background
# Tác giả: AI Assistant
# Ngày tạo: $(Get-Date)

param(
    [Parameter(Mandatory=$true)]
    [string]$NewImagePath,
    
    [string]$BackupName = "hero_bg_backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').jpg",
    [string]$TargetName = "hero_bg.jpg"
)

# Đường dẫn thư mục images
$ImagesPath = Join-Path $PSScriptRoot "..\images"
$TargetPath = Join-Path $ImagesPath $TargetName

Write-Host "=== SCRIPT THAY ĐỔI ẢNH HERO BACKGROUND ===" -ForegroundColor Green
Write-Host ""

# Kiểm tra file ảnh mới có tồn tại không
if (-not (Test-Path $NewImagePath)) {
    Write-Host "❌ LỖI: Không tìm thấy file ảnh mới tại: $NewImagePath" -ForegroundColor Red
    Write-Host "Vui lòng kiểm tra đường dẫn và thử lại!" -ForegroundColor Yellow
    exit 1
}

# Kiểm tra thư mục images
if (-not (Test-Path $ImagesPath)) {
    Write-Host "❌ LỖI: Không tìm thấy thư mục images tại: $ImagesPath" -ForegroundColor Red
    exit 1
}

Write-Host "📁 Thư mục images: $ImagesPath" -ForegroundColor Cyan
Write-Host "🖼️ Ảnh mới: $NewImagePath" -ForegroundColor Cyan
Write-Host "🎯 Ảnh đích: $TargetPath" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra file hiện tại
if (Test-Path $TargetPath) {
    $CurrentFile = Get-Item $TargetPath
    Write-Host "📄 File hiện tại:" -ForegroundColor Yellow
    Write-Host "   Tên: $($CurrentFile.Name)" -ForegroundColor White
    Write-Host "   Kích thước: $([math]::Round($CurrentFile.Length/1KB, 2)) KB" -ForegroundColor White
    Write-Host "   Ngày tạo: $($CurrentFile.CreationTime)" -ForegroundColor White
    Write-Host ""
    
    # Backup file hiện tại
    $BackupPath = Join-Path $ImagesPath $BackupName
    Write-Host "💾 Đang backup file hiện tại..." -ForegroundColor Yellow
    try {
        Copy-Item $TargetPath $BackupPath
        Write-Host "✅ Backup thành công: $BackupName" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ Lỗi khi backup: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}
else {
    Write-Host "⚠️  Không tìm thấy file hero_bg.jpg hiện tại" -ForegroundColor Yellow
    Write-Host "   Sẽ tạo file mới..." -ForegroundColor White
}

# Thay thế file
Write-Host ""
Write-Host "🔄 Đang thay thế ảnh..." -ForegroundColor Yellow
try {
    Copy-Item $NewImagePath $TargetPath -Force
    Write-Host "✅ Thay thế thành công!" -ForegroundColor Green
    
    # Hiển thị thông tin file mới
    $NewFile = Get-Item $TargetPath
    Write-Host ""
    Write-Host "📄 Thông tin file mới:" -ForegroundColor Green
    Write-Host "   Tên: $($NewFile.Name)" -ForegroundColor White
    Write-Host "   Kích thước: $([math]::Round($NewFile.Length/1KB, 2)) KB" -ForegroundColor White
    Write-Host "   Ngày cập nhật: $($NewFile.LastWriteTime)" -ForegroundColor White
}
catch {
    Write-Host "❌ Lỗi khi thay thế: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🎉 HOÀN THÀNH!" -ForegroundColor Green
Write-Host "📝 Lưu ý:" -ForegroundColor Yellow
Write-Host "   - File backup: $BackupName" -ForegroundColor White
Write-Host "   - Refresh trang web để xem thay đổi" -ForegroundColor White
Write-Host "   - Nếu không thấy thay đổi, hãy clear cache trình duyệt" -ForegroundColor White
Write-Host ""

# Gợi ý các bước tiếp theo
Write-Host "🔧 Các bước tiếp theo:" -ForegroundColor Cyan
Write-Host "   1. Mở website và kiểm tra hero section" -ForegroundColor White
Write-Host "   2. Nếu cần, điều chỉnh CSS (height, background-position)" -ForegroundColor White
Write-Host "   3. Test trên các thiết bị khác nhau" -ForegroundColor White 