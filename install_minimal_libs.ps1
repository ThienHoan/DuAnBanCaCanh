# Script PowerShell để tải thư viện tối thiểu cho Module4 với Perplexity
Write-Host "=== MODULE4 MINIMAL LIBRARIES INSTALLER ===" -ForegroundColor Green
Write-Host "Tải thư viện tối thiểu cho Module4 với Perplexity API" -ForegroundColor Yellow

# Đường dẫn thư mục lib
$libPath = "web\WEB-INF\lib"

# Kiểm tra thư mục lib
if (-not (Test-Path $libPath)) {
    Write-Host "❌ Không tìm thấy thư mục $libPath" -ForegroundColor Red
    exit 1
}

Write-Host "📁 Thư mục lib: $libPath" -ForegroundColor Cyan

# Danh sách thư viện tối thiểu cần thiết (KHÔNG PHẢI KOTLIN)
$requiredLibraries = @(
    @{
        name = "SLF4J API"
        filename = "slf4j-api-2.0.6.jar"
        url = "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/slf4j-api-2.0.6.jar"
        required = $true
        description = "Logging API cho Module4"
    },
    @{
        name = "Javassist"
        filename = "javassist-3.29.2-GA.jar"
        url = "https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar"
        required = $true
        description = "Bytecode manipulation cho Reflections"
    }
)

Write-Host "`n🔍 Kiểm tra thư viện hiện có..." -ForegroundColor Cyan

# Kiểm tra thư viện đã có
Write-Host "📦 Thư viện đã có trong dự án:" -ForegroundColor Yellow
$existingLibs = @(
    "slf4j-simple-2.0.6.jar",
    "reflections-0.10.2.jar", 
    "jackson-core-2.19.1.jar",
    "jackson-databind-2.19.1.jar",
    "jackson-annotations-2.19.1.jar",
    "okhttp-4.10.0.jar",
    "gson-2.10.1.jar"
)

foreach ($lib in $existingLibs) {
    $filePath = Join-Path $libPath $lib
    if (Test-Path $filePath) {
        $size = (Get-Item $filePath).Length
        Write-Host "  ✅ $lib ($([math]::Round($size/1KB, 2)) KB)" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $lib (MISSING)" -ForegroundColor Red
    }
}

Write-Host "`n⏬ Tải thư viện còn thiếu..." -ForegroundColor Green

# Tải từng thư viện cần thiết
$downloadCount = 0
$errorCount = 0

foreach ($lib in $requiredLibraries) {
    $filePath = Join-Path $libPath $lib.filename
    
    # Kiểm tra file đã tồn tại chưa
    if (Test-Path $filePath) {
        $size = (Get-Item $filePath).Length
        Write-Host "✅ $($lib.name) đã có: $($lib.filename) ($([math]::Round($size/1KB, 2)) KB)" -ForegroundColor Green
        continue
    }
    
    Write-Host "⏬ Đang tải $($lib.name) - $($lib.description)..." -ForegroundColor Yellow
    
    try {
        # Tải file
        Invoke-WebRequest -Uri $lib.url -OutFile $filePath -ErrorAction Stop
        
        # Kiểm tra file đã tải thành công
        if (Test-Path $filePath) {
            $size = (Get-Item $filePath).Length
            if ($size -gt 5000) {  # File > 5KB
                Write-Host "✅ Tải thành công $($lib.name): $($lib.filename) ($([math]::Round($size/1KB, 2)) KB)" -ForegroundColor Green
                $downloadCount++
            } else {
                Write-Host "⚠️ File $($lib.filename) quá nhỏ ($size bytes), có thể lỗi" -ForegroundColor Yellow
                Remove-Item $filePath -Force
                $errorCount++
            }
        } else {
            Write-Host "❌ Không thể tải $($lib.name)" -ForegroundColor Red
            $errorCount++
        }
    }
    catch {
        Write-Host "❌ Lỗi khi tải $($lib.name): $($_.Exception.Message)" -ForegroundColor Red
        $errorCount++
    }
}

Write-Host "`n=== TÓM TẮT KẾT QUẢ ===" -ForegroundColor Green
Write-Host "📥 Đã tải: $downloadCount thư viện mới" -ForegroundColor Green
Write-Host "❌ Lỗi: $errorCount thư viện" -ForegroundColor Red

Write-Host "`n📋 DANH SÁCH TOÀN BỘ THƯ VIỆN:" -ForegroundColor Cyan
Get-ChildItem -Path $libPath -Filter "*.jar" | Sort-Object Name | ForEach-Object {
    $size = [math]::Round($_.Length/1KB, 2)
    $isCore = $_.Name -match "(slf4j|jackson|reflections|okhttp|gson|javassist)"
    $marker = if ($isCore) { "🟢" } else { "📦" }
    Write-Host "  $marker $($_.Name) ($size KB)"
}

Write-Host "`n=== CẤU HÌNH PERPLEXITY API ===" -ForegroundColor Green

Write-Host "🔑 Để sử dụng Perplexity API, bạn cần:" -ForegroundColor Yellow
Write-Host "1. Đăng ký tài khoản tại: https://www.perplexity.ai/" -ForegroundColor Gray
Write-Host "2. Lấy API key từ dashboard" -ForegroundColor Gray
Write-Host "3. Cấu hình một trong các cách sau:" -ForegroundColor Gray
Write-Host "`n   Option A: Environment Variable (khuyến nghị)" -ForegroundColor Cyan
Write-Host "   set PERPLEXITY_API_KEY=your-api-key-here" -ForegroundColor Gray
Write-Host "`n   Option B: Sửa trong LLM.java" -ForegroundColor Cyan
Write-Host "   Thay đổi dòng: this.apiKey = `"your-perplexity-api-key-here`";" -ForegroundColor Gray

Write-Host "`n=== HƯỚNG DẪN TIẾP THEO ===" -ForegroundColor Green

if ($errorCount -eq 0) {
    Write-Host "🎉 Tất cả thư viện cần thiết đã được cài đặt!" -ForegroundColor Green
    Write-Host "`n1️⃣ Cấu hình Perplexity API key (xem bên trên)" -ForegroundColor Yellow
    Write-Host "2️⃣ Restart Tomcat server" -ForegroundColor Yellow
    Write-Host "3️⃣ Test Module4 AI Agent:" -ForegroundColor Yellow
    Write-Host "   - Test page: http://localhost:8080/DuAnBanCaCanh/test-module4-integration.jsp" -ForegroundColor Gray
    Write-Host "   - AI Agent: http://localhost:8080/DuAnBanCaCanh/ai-agent.jsp" -ForegroundColor Gray
    Write-Host "4️⃣ Kiểm tra console log không còn NoClassDefFoundError" -ForegroundColor Yellow
    
    Write-Host "`n🔧 LƯU Ý QUAN TRỌNG:" -ForegroundColor Red
    Write-Host "- Module4 đã được sửa để dùng Perplexity thay vì OpenAI" -ForegroundColor Yellow
    Write-Host "- KHÔNG CẦN các thư viện Kotlin nữa!" -ForegroundColor Yellow
    Write-Host "- Nhẹ hơn và ổn định hơn nhiều" -ForegroundColor Yellow
} else {
    Write-Host "⚠️ Có $errorCount lỗi khi tải thư viện" -ForegroundColor Yellow
    Write-Host "📋 Hãy thử tải thủ công từ Maven Central:" -ForegroundColor Yellow
    Write-Host "   - https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.6/" -ForegroundColor Gray
    Write-Host "   - https://repo1.maven.org/maven2/org/javassist/javassist/3.29.2-GA/" -ForegroundColor Gray
}

Write-Host "`n🚀 Hoàn tất cài đặt Module4 với Perplexity!" -ForegroundColor Green
