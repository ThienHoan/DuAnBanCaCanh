# 🚀 Setup Script for New Users
# Run this script to quickly setup the AI Agent

param(
    [Parameter(Mandatory=$false)]
    [string]$ApiKey
)

Write-Host "🤖 AI Agent Setup Script" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra nếu có API key được truyền vào
if ($ApiKey) {
    if ($ApiKey.StartsWith("pplx-")) {
        Write-Host "✅ Valid API key format detected" -ForegroundColor Green
        
        # Set environment variable
        [Environment]::SetEnvironmentVariable("PERPLEXITY_API_KEY", $ApiKey, "User")
        $env:PERPLEXITY_API_KEY = $ApiKey
        
        Write-Host "🔧 API key has been set as environment variable" -ForegroundColor Green
        Write-Host "   You may need to restart your IDE/Terminal" -ForegroundColor Yellow
        
        # Also update agent.config
        $configContent = Get-Content "agent.config"
        $configContent = $configContent -replace "PERPLEXITY_API_KEY=.*", "PERPLEXITY_API_KEY=$ApiKey"
        $configContent | Set-Content "agent.config"
        
        Write-Host "🔧 Updated agent.config file" -ForegroundColor Green
        
    } else {
        Write-Host "❌ Invalid API key format. Must start with 'pplx-'" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "ℹ️  No API key provided. Please get one first:" -ForegroundColor Yellow
    Write-Host "   1. Visit: https://www.perplexity.ai/settings/api" -ForegroundColor White
    Write-Host "   2. Create a new API key (free)" -ForegroundColor White
    Write-Host "   3. Run: .\setup-new-user.ps1 -ApiKey 'pplx-your-key-here'" -ForegroundColor White
    Write-Host ""
    
    # Kiểm tra xem có API key nào đã được set chưa
    Write-Host "🔍 Checking existing configuration..." -ForegroundColor Cyan
    & ".\check-api-key.ps1"
    exit 0
}

# Kiểm tra cấu hình sau khi setup
Write-Host ""
Write-Host "🔍 Verifying configuration..." -ForegroundColor Cyan
& ".\check-api-key.ps1"

# Hướng dẫn bước tiếp theo
Write-Host ""
Write-Host "🚀 Next Steps:" -ForegroundColor Cyan
Write-Host "   1. Build project:" -ForegroundColor White
Write-Host "      ant clean build" -ForegroundColor Gray
Write-Host ""
Write-Host "   2. Deploy to Tomcat and access:" -ForegroundColor White  
Write-Host "      http://localhost:8080/yourproject/ai-agent.jsp" -ForegroundColor Gray
Write-Host ""
Write-Host "   3. Test the AI Agent by asking about aquarium fish!" -ForegroundColor White
Write-Host ""
Write-Host "📖 For more details, see: SETUP_FOR_NEW_USER.md" -ForegroundColor Cyan
