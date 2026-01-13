# 🔍 API Key Checker - Kiểm tra cấu hình Perplexity API
# Chạy script này để verify API key đã được cấu hình đúng chưa

Write-Host "🔍 Checking Perplexity API Key Configuration..." -ForegroundColor Cyan
Write-Host ""

# Kiểm tra biến môi trường
Write-Host "1. Checking Environment Variable..." -ForegroundColor Yellow
$envKey = $env:PERPLEXITY_API_KEY
if ($envKey) {
    if ($envKey.StartsWith("pplx-")) {
        Write-Host "   ✅ Found valid API key in environment variable" -ForegroundColor Green
        Write-Host "   📝 Key: $($envKey.Substring(0,8))..." -ForegroundColor Gray
    } else {
        Write-Host "   ⚠️  Found API key but format seems wrong (should start with 'pplx-')" -ForegroundColor Red
        Write-Host "   📝 Key: $envKey" -ForegroundColor Gray
    }
} else {
    Write-Host "   ❌ No API key found in environment variable" -ForegroundColor Red
}

# Kiểm tra System Property (nếu Java đang chạy)
Write-Host ""
Write-Host "2. Checking System Property..." -ForegroundColor Yellow
Write-Host "   ℹ️  System property sẽ được kiểm tra khi chạy ứng dụng Java" -ForegroundColor Gray

# Kiểm tra file agent.config
Write-Host ""
Write-Host "3. Checking agent.config file..." -ForegroundColor Yellow
if (Test-Path "agent.config") {
    $configContent = Get-Content "agent.config" | Where-Object { $_ -match "PERPLEXITY_API_KEY=" }
    if ($configContent) {
        $configKey = ($configContent -split "=")[1]
        if ($configKey -and $configKey -ne "your_perplexity_api_key_here") {
            if ($configKey.StartsWith("pplx-")) {
                Write-Host "   ✅ Found valid API key in agent.config" -ForegroundColor Green
                Write-Host "   📝 Key: $($configKey.Substring(0,8))..." -ForegroundColor Gray
            } else {
                Write-Host "   ⚠️  Found API key but format seems wrong" -ForegroundColor Red
            }
        } else {
            Write-Host "   ⚠️  agent.config exists but API key is placeholder" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   ❌ agent.config exists but no PERPLEXITY_API_KEY found" -ForegroundColor Red
    }
} else {
    Write-Host "   ❌ agent.config file not found" -ForegroundColor Red
}

# Kiểm tra file perplexity.properties
Write-Host ""
Write-Host "4. Checking perplexity.properties..." -ForegroundColor Yellow
if (Test-Path "src/perplexity.properties") {
    $propsContent = Get-Content "src/perplexity.properties" | Where-Object { $_ -match "api\.key=" }
    if ($propsContent) {
        $propsKey = ($propsContent -split "=")[1]
        if ($propsKey -and $propsKey -ne "your_perplexity_api_key_here") {
            if ($propsKey.StartsWith("pplx-")) {
                Write-Host "   ✅ Found valid API key in perplexity.properties" -ForegroundColor Green
                Write-Host "   📝 Key: $($propsKey.Substring(0,8))..." -ForegroundColor Gray
            } else {
                Write-Host "   ⚠️  Found API key but format seems wrong" -ForegroundColor Red
            }
        } else {
            Write-Host "   ⚠️  perplexity.properties exists but API key is placeholder" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   ❌ perplexity.properties exists but no api.key found" -ForegroundColor Red
    }
} else {
    Write-Host "   ℹ️  perplexity.properties not found (optional)" -ForegroundColor Gray
}

# Tóm tắt và khuyến nghị
Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor Cyan

$hasValidKey = $false
if ($envKey -and $envKey.StartsWith("pplx-")) {
    $hasValidKey = $true
}
if ((Test-Path "agent.config") -and (Get-Content "agent.config" | Where-Object { $_ -match "PERPLEXITY_API_KEY=pplx-" })) {
    $hasValidKey = $true
}
if ((Test-Path "src/perplexity.properties") -and (Get-Content "src/perplexity.properties" | Where-Object { $_ -match "api\.key=pplx-" })) {
    $hasValidKey = $true
}

if ($hasValidKey) {
    Write-Host "   ✅ Configuration looks good! AI Agent should work." -ForegroundColor Green
    Write-Host ""
    Write-Host "🚀 Next Steps:" -ForegroundColor Cyan
    Write-Host "   1. Build project: ant clean build" -ForegroundColor White
    Write-Host "   2. Deploy to Tomcat" -ForegroundColor White
    Write-Host "   3. Test at: http://localhost:8080/yourproject/ai-agent.jsp" -ForegroundColor White
} else {
    Write-Host "   ❌ No valid API key found! Please configure one." -ForegroundColor Red
    Write-Host ""
    Write-Host "🔧 To fix this:" -ForegroundColor Cyan
    Write-Host "   1. Get API key from: https://www.perplexity.ai/settings/api" -ForegroundColor White
    Write-Host "   2. Set environment variable:" -ForegroundColor White
    Write-Host "      " -NoNewline; Write-Host "`$env:PERPLEXITY_API_KEY='pplx-your-key-here'" -ForegroundColor Gray
    Write-Host "   3. OR edit agent.config file" -ForegroundColor White
    Write-Host "   4. Run this script again to verify" -ForegroundColor White
}

Write-Host ""
Write-Host "📖 For detailed setup guide, see: SETUP_FOR_NEW_USER.md" -ForegroundColor Cyan
