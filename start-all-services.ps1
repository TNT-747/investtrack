# InvestTrack Services Startup Script

Write-Host "===== InvestTrack Microservices Startup =====" -ForegroundColor Cyan
Write-Host ""

# Check if Config Server and Eureka Server are running
Write-Host "Checking infrastructure services..." -ForegroundColor Yellow
$configRunning = $false
$eurekaRunning = $false

try {
    $configResponse = Invoke-WebRequest -Uri "http://localhost:8888/actuator/health" -TimeoutSec 2 -UseBasicParsing
    if ($configResponse.StatusCode -eq 200) {
        Write-Host "[OK] Config Server is running on port 8888" -ForegroundColor Green
        $configRunning = $true
    }
} catch {
    Write-Host "[DOWN] Config Server is NOT running" -ForegroundColor Red
}

try {
    $eurekaResponse = Invoke-WebRequest -Uri "http://localhost:8761/actuator/health" -TimeoutSec 2 -UseBasicParsing
    if ($eurekaResponse.StatusCode -eq 200) {
        Write-Host "[OK] Eureka Server is running on port 8761" -ForegroundColor Green
        $eurekaRunning = $true
    }
} catch {
    Write-Host "[DOWN] Eureka Server is NOT running" -ForegroundColor Red
}

Write-Host ""

# Start infrastructure services if needed
if (-not $configRunning) {
    Write-Host "Starting Config Server..." -ForegroundColor Yellow
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\config-server'; mvn spring-boot:run"
    Write-Host "Waiting for Config Server to start..." -ForegroundColor Yellow
    Start-Sleep -Seconds 15
}

if (-not $eurekaRunning) {
    Write-Host "Starting Eureka Server..." -ForegroundColor Yellow
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\eureka-server'; mvn spring-boot:run"
    Write-Host "Waiting for Eureka Server to start..." -ForegroundColor Yellow
    Start-Sleep -Seconds 15
}

# Start microservices
Write-Host "Starting Market Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\market-service'; mvn spring-boot:run"

Write-Host "Waiting 10 seconds before starting next service..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Starting Wallet Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\wallet-service'; mvn spring-boot:run"

Write-Host "Waiting 10 seconds before starting next service..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Starting Gateway Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\gateway-service'; mvn spring-boot:run"

Write-Host ""
Write-Host "===== All Services Started =====" -ForegroundColor Cyan
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Yellow
Write-Host "  Config Server: http://localhost:8888"
Write-Host "  Eureka Dashboard: http://localhost:8761"
Write-Host "  Market Service: http://localhost:8081"
Write-Host "  Wallet Service: http://localhost:8082"
Write-Host "  Gateway Service: http://localhost:8080"
Write-Host ""
Write-Host "Press any key to start frontend..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Start frontend
Write-Host ""
Write-Host "Starting React Frontend..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\frontend'; npm start"

Write-Host ""
Write-Host "[SUCCESS] InvestTrack application is now running!" -ForegroundColor Green
Write-Host "  Frontend: http://localhost:3000"
