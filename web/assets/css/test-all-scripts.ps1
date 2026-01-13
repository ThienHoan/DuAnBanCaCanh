# Test tat ca script thay doi anh hero
Write-Host "=== TEST TAT CA SCRIPT THAY DOI ANH HERO ===" -ForegroundColor Green
Write-Host ""

# Danh sach script can test
$Scripts = @(
    "change-hero-simple.ps1",
    "quick-change-hero-fixed.ps1",
    "change-hero-image.ps1"
)

# Test tung script
foreach ($Script in $Scripts) {
    Write-Host "Testing: $Script" -ForegroundColor Yellow
    
    if (Test-Path $Script) {
        # Kiem tra syntax
        try {
            $null = [System.Management.Automation.PSParser]::Tokenize((Get-Content $Script -Raw), [ref]$null)
            Write-Host "  ✅ Syntax OK" -ForegroundColor Green
        }
        catch {
            Write-Host "  ❌ Syntax Error: $($_.Exception.Message)" -ForegroundColor Red
        }
        
        # Kiem tra parameter
        try {
            $Ast = [System.Management.Automation.Language.Parser]::ParseFile($Script, [ref]$null, [ref]$null)
            $Params = $Ast.ParamBlock.Parameters.Name.VariablePath.UserPath
            if ($Params) {
                Write-Host "  📝 Parameters: $($Params -join ', ')" -ForegroundColor Cyan
            }
        }
        catch {
            Write-Host "  ⚠️  Cannot parse parameters" -ForegroundColor Yellow
        }
    }
    else {
        Write-Host "  ❌ File not found" -ForegroundColor Red
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
    Write-Host "✅ File hero_bg.jpg ton tai ($([math]::Round($File.Length/1KB, 2)) KB)" -ForegroundColor Green
} else {
    Write-Host "❌ File hero_bg.jpg khong ton tai" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== HUONG DAN SU DUNG ===" -ForegroundColor Green
Write-Host "1. Su dung script don gian:" -ForegroundColor White
Write-Host "   .\change-hero-simple.ps1 'C:\duong\dan\anh\moi.jpg'" -ForegroundColor Cyan
Write-Host ""
Write-Host "2. Su dung script moi:" -ForegroundColor White
Write-Host "   .\quick-change-hero-fixed.ps1 'C:\duong\dan\anh\moi.jpg'" -ForegroundColor Cyan
Write-Host ""
Write-Host "3. Su dung script chi tiet:" -ForegroundColor White
Write-Host "   .\change-hero-image.ps1 -NewImagePath 'C:\duong\dan\anh\moi.jpg'" -ForegroundColor Cyan 