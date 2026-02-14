# Script tải thư viện Kotlin cho OkHttp
# Chạy từ thư mục gốc dự án: powershell -ExecutionPolicy Bypass -File scripts\download-kotlin-libs.ps1

Write-Host "🔽 Downloading Kotlin libraries for OkHttp..." -ForegroundColor Green

# Tạo thư mục lib nếu chưa có
$libDir = "web\WEB-INF\lib"
if (!(Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir -Force
    Write-Host "✅ Created lib directory: $libDir" -ForegroundColor Green
}

# Danh sách thư viện Kotlin cần thiết cho OkHttp 4.x
$kotlinLibs = @(
    @{
        name = "kotlin-stdlib-1.8.22.jar"
        url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib/1.8.22/kotlin-stdlib-1.8.22.jar"
    },
    @{
        name = "kotlin-stdlib-common-1.8.22.jar" 
        url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib-common/1.8.22/kotlin-stdlib-common-1.8.22.jar"
    },
    @{
        name = "kotlin-stdlib-jdk8-1.8.22.jar"
        url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-stdlib-jdk8/1.8.22/kotlin-stdlib-jdk8-1.8.22.jar"
    }
)

foreach ($lib in $kotlinLibs) {
    $filePath = Join-Path $libDir $lib.name
    
    if (Test-Path $filePath) {
        Write-Host "⏭️ Skipping $($lib.name) (already exists)" -ForegroundColor Yellow
        continue
    }
    
    try {
        Write-Host "📥 Downloading $($lib.name)..." -ForegroundColor Cyan
        Invoke-WebRequest -Uri $lib.url -OutFile $filePath -UseBasicParsing
        
        if (Test-Path $filePath) {
            $fileSize = (Get-Item $filePath).Length
            Write-Host "✅ Downloaded $($lib.name) ($fileSize bytes)" -ForegroundColor Green
        } else {
            Write-Host "❌ Failed to download $($lib.name)" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "❌ Error downloading $($lib.name): $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "📋 Current libraries in $libDir:" -ForegroundColor Blue
Get-ChildItem $libDir -Name | Sort-Object

Write-Host ""
Write-Host "🎯 Now try building the project:" -ForegroundColor Green
Write-Host "   ant clean compile" -ForegroundColor White
Write-Host ""
Write-Host "🔧 If still having issues, check these versions:" -ForegroundColor Yellow
Write-Host "   - OkHttp: 4.12.0 (current)" -ForegroundColor White
Write-Host "   - Kotlin: 1.8.22 (downloading)" -ForegroundColor White
Write-Host "   - Make sure no conflicting Kotlin versions" -ForegroundColor White
