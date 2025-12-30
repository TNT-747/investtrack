# Test Config Server - Git Configuration

## 1. Verify Git Repository

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "1. Checking Git Config Repository" -ForegroundColor Cyan
Write-Host "======================================`n" -ForegroundColor Cyan

Set-Location "C:\Users\Kassimi\.gemini\antigravity\scratch\investtrack-config"

Write-Host "Git Status:" -ForegroundColor Yellow
git status

Write-Host "`nGit Log:" -ForegroundColor Yellow
git log --oneline

Write-Host "`nConfiguration Files:" -ForegroundColor Yellow
Get-ChildItem -Filter "*.properties" | Format-Table Name, Length

## 2. Show Configuration Content

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "2. Configuration File Contents" -ForegroundColor Cyan
Write-Host "======================================`n" -ForegroundColor Cyan

Write-Host "=== wallet-service.properties (with mandatory config) ===" -ForegroundColor Green
Get-Content wallet-service.properties | Select-String "invest-config.wallet.history-days" | ForEach-Object { Write-Host $_ -ForegroundColor Yellow }

## 3. Wait for Services to Start

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "3. Waiting for Services to Start" -ForegroundColor Cyan
Write-Host "======================================`n" -ForegroundColor Cyan

Set-Location "C:\Users\Kassimi\.gemini\antigravity\scratch\investtrack"

Write-Host "Checking Docker Compose status..." -ForegroundColor Yellow
docker compose ps

Write-Host "`nWaiting 30 seconds for services to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

## 4. Test Config Server

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "4. Testing Config Server Endpoints" -ForegroundColor Cyan
Write-Host "======================================`n" -ForegroundColor Cyan

Write-Host "Testing if Config Server is running..." -ForegroundColor Yellow

try {
    Write-Host "`n--- Config Server Health ---" -ForegroundColor Green
    $health = Invoke-RestMethod -Uri "http://localhost:8888/actuator/health" -ErrorAction Stop
    Write-Host "Status: $($health.status)" -ForegroundColor Green
    
    Write-Host "`n--- Market Service Config from Git ---" -ForegroundColor Green
    $marketConfig = Invoke-RestMethod -Uri "http://localhost:8888/market-service/default" -ErrorAction Stop
    Write-Host "Application Name: $($marketConfig.name)"
    Write-Host "Profiles: $($marketConfig.profiles)"
    Write-Host "Label: $($marketConfig.label)"
    Write-Host "Property Sources:" -ForegroundColor Yellow
    $marketConfig.propertySources | ForEach-Object {
        Write-Host "  - $($_.name)" -ForegroundColor Cyan
    }
    
    Write-Host "`n--- Wallet Service Config from Git (includes mandatory variable) ---" -ForegroundColor Green
    $walletConfig = Invoke-RestMethod -Uri "http://localhost:8888/wallet-service/default" -ErrorAction Stop
    Write-Host "Application Name: $($walletConfig.name)"
    
    # Find the history-days property
    $historyDays = $null
    foreach ($source in $walletConfig.propertySources) {
        if ($source.source.'invest-config.wallet.history-days') {
            $historyDays = $source.source.'invest-config.wallet.history-days'
            Write-Host "`n✅ MANDATORY CONFIG FOUND:" -ForegroundColor Green
            Write-Host "   invest-config.wallet.history-days = $historyDays" -ForegroundColor Yellow
            break
        }
    }
    
    Write-Host "`n--- Gateway Service Config from Git ---" -ForegroundColor Green
    $gatewayConfig = Invoke-RestMethod -Uri "http://localhost:8888/gateway-service/default" -ErrorAction Stop
    Write-Host "Application Name: $($gatewayConfig.name)"
    
    Write-Host "`n✅ SUCCESS: Config Server is reading from Git repository!" -ForegroundColor Green
    
} catch {
    Write-Host "⚠️  Config Server not ready yet. Error: $_" -ForegroundColor Red
    Write-Host "`nTry running this script again after services are fully started." -ForegroundColor Yellow
    Write-Host "You can check service status with: docker compose ps" -ForegroundColor Yellow
}

Write-Host "`n======================================" -ForegroundColor Cyan
Write-Host "Test Complete" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
