# Script PowerShell để tự động tải và cài đặt thư viện Module4
# Chạy script này từ thư mục gốc của dự án

Write-Host "=== MODULE4 LIBRARY INSTALLER ===" -ForegroundColor Green
Write-Host "Đang tải các thư viện còn thiếu cho Module4..." -ForegroundColor Yellow

# Đường dẫn thư mục lib
$libPath = "web\WEB-INF\lib"

# Kiểm tra thư mục lib có tồn tại không
if (-not (Test-Path $libPath)) {
    Write-Host "Không tìm thấy thư mục $libPath" -ForegroundColor Red
    exit 1
}

Write-Host "Thư mục lib: $libPath" -ForegroundColor Cyan

# Danh sách thư viện cần tải
$libraries = @(
    @{
        name = "SLF4J API"
        filename = "slf4j-api-2.0.6.jar"
        url = "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar"
    },
    @{
        name = "Javassist"
        filename = "javassist-3.29.2-GA.jar"
        url = "https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar"
    }
)

# Tải từng thư viện
foreach ($lib in $libraries) {
    $filePath = Join-Path $libPath $lib.filename
    
    # Kiểm tra file đã tồn tại chưa
    if (Test-Path $filePath) {
        Write-Host "✅ $($lib.name) đã tồn tại: $($lib.filename)" -ForegroundColor Green
        continue
    }
    
    Write-Host "⏬ Đang tải $($lib.name)..." -ForegroundColor Yellow
    
    try {
        # Tải file
        Invoke-WebRequest -Uri $lib.url -OutFile $filePath -ErrorAction Stop
        
        # Kiểm tra file đã tải thành công
        if (Test-Path $filePath) {
            $size = (Get-Item $filePath).Length
            Write-Host "✅ Đã tải $($lib.name): $($lib.filename) ($([math]::Round($size/1KB, 2)) KB)" -ForegroundColor Green
        } else {
            Write-Host "❌ Lỗi: Không thể tải $($lib.name)" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "❌ Lỗi khi tải $($lib.name): $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n=== KIỂM TRA THƯ VIỆN ===" -ForegroundColor Green

# Liệt kê tất cả file JAR trong thư mục lib
Write-Host "Danh sách thư viện trong $libPath:" -ForegroundColor Cyan
Get-ChildItem -Path $libPath -Filter "*.jar" | Sort-Object Name | ForEach-Object {
    $size = [math]::Round($_.Length/1KB, 2)
    Write-Host "  📦 $($_.Name) ($size KB)"
}

Write-Host "`n=== HƯỚNG DẪN TIẾP THEO ===" -ForegroundColor Green
Write-Host "1. Restart Tomcat server" -ForegroundColor Yellow
Write-Host "2. Clean và rebuild project (nếu cần)" -ForegroundColor Yellow
Write-Host "3. Test tại: http://localhost:8080/DuAnBanCaCanh/test-module4-integration.jsp" -ForegroundColor Yellow
Write-Host "4. Kiểm tra AI Agent: http://localhost:8080/DuAnBanCaCanh/ai-agent.jsp" -ForegroundColor Yellow

Write-Host "`nHoàn tất!" -ForegroundColor Green
