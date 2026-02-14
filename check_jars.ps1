# AI Agent JAR Download Script
# ============================

Write-Host "🤖 AI Agent JAR Download Helper" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host

# Đường dẫn thư mục lib
$libPath = "d:\Folder_Hoc_DuAn\Ki5\SWP\DuAnBanCaCanh\build\web\WEB-INF\lib"

Write-Host "📁 Kiểm tra thư mục lib: $libPath" -ForegroundColor Yellow

if (Test-Path $libPath) {
    Write-Host "✅ Thư mục lib tồn tại" -ForegroundColor Green
} else {
    Write-Host "❌ Thư mục lib không tồn tại!" -ForegroundColor Red
    Write-Host "💡 Hãy build project trước trong NetBeans" -ForegroundColor Yellow
    exit 1
}

Write-Host
Write-Host "🔍 Kiểm tra các Jackson JAR hiện có..." -ForegroundColor Yellow

# Kiểm tra các JAR Jackson
$jacksonDatabind = Join-Path $libPath "jackson-databind-2.19.1.jar"
$jacksonCore = Join-Path $libPath "jackson-core-2.19.1.jar"
$jacksonAnnotations = Join-Path $libPath "jackson-annotations-2.19.1.jar"

if (Test-Path $jacksonDatabind) {
    Write-Host "✅ jackson-databind-2.19.1.jar - Có sẵn" -ForegroundColor Green
} else {
    Write-Host "❌ jackson-databind-2.19.1.jar - Thiếu" -ForegroundColor Red
}

if (Test-Path $jacksonCore) {
    Write-Host "✅ jackson-core-2.19.1.jar - Có sẵn" -ForegroundColor Green
} else {
    Write-Host "❌ jackson-core-2.19.1.jar - THIẾU (cần download)" -ForegroundColor Red
}

if (Test-Path $jacksonAnnotations) {
    Write-Host "✅ jackson-annotations-2.19.1.jar - Có sẵn" -ForegroundColor Green
} else {
    Write-Host "❌ jackson-annotations-2.19.1.jar - THIẾU (cần download)" -ForegroundColor Red
}

Write-Host
Write-Host "📥 HƯỚNG DẪN DOWNLOAD CÁC JAR THIẾU:" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan

if (!(Test-Path $jacksonCore)) {
    Write-Host
    Write-Host "1️⃣ jackson-core-2.19.1.jar:" -ForegroundColor Yellow
    Write-Host "   🌐 URL: https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.19.1/jackson-core-2.19.1.jar"
    Write-Host "   💾 Download về: $jacksonCore"
}

if (!(Test-Path $jacksonAnnotations)) {
    Write-Host
    Write-Host "2️⃣ jackson-annotations-2.19.1.jar:" -ForegroundColor Yellow
    Write-Host "   🌐 URL: https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.19.1/jackson-annotations-2.19.1.jar"
    Write-Host "   💾 Download về: $jacksonAnnotations"
}

Write-Host
Write-Host "📋 HOẶC TÌM TRÊN MAVEN CENTRAL:" -ForegroundColor Cyan
Write-Host "===============================" -ForegroundColor Cyan
Write-Host "1. Truy cập: https://mvnrepository.com/"
Write-Host "2. Tìm kiếm: 'jackson-core 2.19.1'"
Write-Host "3. Tìm kiếm: 'jackson-annotations 2.19.1'"
Write-Host "4. Download các file JAR"
Write-Host "5. Copy vào thư mục: $libPath"

Write-Host
Write-Host "🚀 SAU KHI DOWNLOAD XONG:" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green
Write-Host "1. Clean and Build project trong NetBeans (Shift+F11)"
Write-Host "2. Run project (F6)"
Write-Host "3. Truy cập: http://localhost:8080/DuAnBanCaCanh/ai-agent.jsp"
Write-Host "4. Test cả Demo Mode và Real Mode"

Write-Host
Write-Host "✨ Chúc bạn thành công!" -ForegroundColor Magenta
