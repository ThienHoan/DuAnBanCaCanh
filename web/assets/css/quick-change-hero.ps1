# Script đơn giản thay đổi ảnh hero background
# Cách sử dụng: .\quick-change-hero.ps1 "đường_dẫn_đến_ảnh_mới.jpg"

param(
    [Parameter(Mandatory=$true)]
    [string]$NewImagePath
)

Write-Host "🔄 Đang thay đổi ảnh hero background..." -ForegroundColor Green

# Đường dẫn
$ImagesPath = "..\images"
$TargetPath = "$ImagesPath\hero_bg.jpg"
$BackupPath = "$ImagesPath\hero_bg_backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').jpg"

# Backup file cũ
if (Test-Path $TargetPath) {
    Copy-Item $TargetPath $BackupPath
    Write-Host "✅ Backup file cũ thành công" -ForegroundColor Green
}

# Thay thế file mới
Copy-Item $NewImagePath $TargetPath -Force
Write-Host "✅ Thay thế ảnh thành công!" -ForegroundColor Green
Write-Host "🖼️ Ảnh mới: $NewImagePath" -ForegroundColor Cyan
Write-Host "📁 Backup: $BackupPath" -ForegroundColor Yellow
Write-Host "🎯 Đích: $TargetPath" -ForegroundColor Cyan

Write-Host ""
Write-Host "💡 Lưu ý: Refresh trang web để xem thay đổi!" -ForegroundColor Yellow 