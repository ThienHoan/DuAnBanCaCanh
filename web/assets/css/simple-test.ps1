Write-Host "Test script thay doi anh hero" -ForegroundColor Green

$ImagesPath = "..\images"
$TargetPath = "$ImagesPath\hero_bg.jpg"

Write-Host "Kiem tra thu muc images..." -ForegroundColor Yellow
if (Test-Path $ImagesPath) {
    Write-Host "OK: Thu muc images ton tai" -ForegroundColor Green
} else {
    Write-Host "LOI: Thu muc images khong ton tai" -ForegroundColor Red
}

Write-Host "Kiem tra file hero_bg.jpg..." -ForegroundColor Yellow
if (Test-Path $TargetPath) {
    $File = Get-Item $TargetPath
    Write-Host "OK: File hero_bg.jpg ton tai" -ForegroundColor Green
    Write-Host "Kich thuoc: $([math]::Round($File.Length/1KB, 2)) KB" -ForegroundColor White
} else {
    Write-Host "LOI: File hero_bg.jpg khong ton tai" -ForegroundColor Red
}

Write-Host "Huong dan su dung:" -ForegroundColor Cyan
Write-Host ".\quick-change-hero.ps1 'duong_dan_anh_moi'" -ForegroundColor White 