# Kiem tra tat ca script thay doi anh hero
Write-Host "=== KIEM TRA SCRIPT THAY DOI ANH HERO ===" -ForegroundColor Green
Write-Host ""

# Danh sach script
$Scripts = @("change-hero-simple.ps1", "quick-change-hero-fixed.ps1", "change-hero-image.ps1")

foreach ($Script in $Scripts) {
    Write-Host "Script: $Script" -ForegroundColor Yellow
    
    if (Test-Path $Script) {
        Write-Host "  ✅ Ton tai" -ForegroundColor Green
        
        # Kiem tra kich thuoc
        $Size = (Get-Item $Script).Length
        Write-Host "  📏 Kich thuoc: $Size bytes" -ForegroundColor White
        
        # Kiem tra dong dau tien
        $FirstLine = Get-Content $Script -First 1
        Write-Host "  📝 Dong dau: $FirstLine" -ForegroundColor White
    } else {
        Write-Host "  ❌ Khong ton tai" -ForegroundColor Red
    }
    Write-Host ""
}

# Kiem tra thu muc images
Write-Host "=== KIEM TRA THU MUC IMAGES ===" -ForegroundColor Green
$ImagesPath = "..\images"
$TargetPath = "$ImagesPath\hero_bg.jpg"

if (Test-Path $ImagesPath) {
    Write-Host "✅ Thu muc images ton tai" -ForegroundColor Green
} else {
    Write-Host "❌ Thu muc images khong ton tai" -ForegroundColor Red
}

if (Test-Path $TargetPath) {
    $File = Get-Item $TargetPath
    $SizeKB = [math]::Round($File.Length/1KB, 2)
    Write-Host "✅ File hero_bg.jpg ton tai ($SizeKB KB)" -ForegroundColor Green
} else {
    Write-Host "❌ File hero_bg.jpg khong ton tai" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== HUONG DAN SU DUNG ===" -ForegroundColor Green
Write-Host "1. Script don gian: .\change-hero-simple.ps1 'duong_dan_anh'" -ForegroundColor Cyan
Write-Host "2. Script moi: .\quick-change-hero-fixed.ps1 'duong_dan_anh'" -ForegroundColor Cyan
Write-Host "3. Script chi tiet: .\change-hero-image.ps1 -NewImagePath 'duong_dan_anh'" -ForegroundColor Cyan 