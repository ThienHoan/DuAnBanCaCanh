# Script don gian thay doi anh hero background
# Cach su dung: .\quick-change-hero-fixed.ps1 "duong_dan_den_anh_moi.jpg"

param(
    [Parameter(Mandatory=$true)]
    [string]$NewImagePath
)

Write-Host "Dang thay doi anh hero background..." -ForegroundColor Green

# Duong dan
$ImagesPath = "..\images"
$TargetPath = "$ImagesPath\hero_bg.jpg"
$BackupPath = "$ImagesPath\hero_bg_backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').jpg"

# Backup file cu
if (Test-Path $TargetPath) {
    Copy-Item $TargetPath $BackupPath
    Write-Host "Backup file cu thanh cong" -ForegroundColor Green
}

# Thay the file moi
Copy-Item $NewImagePath $TargetPath -Force
Write-Host "Thay the anh thanh cong!" -ForegroundColor Green
Write-Host "Anh moi: $NewImagePath" -ForegroundColor Cyan
Write-Host "Backup: $BackupPath" -ForegroundColor Yellow
Write-Host "Dich: $TargetPath" -ForegroundColor Cyan

Write-Host ""
Write-Host "Luu y: Refresh trang web de xem thay doi!" -ForegroundColor Yellow 