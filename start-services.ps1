# InvestTrack Services Startup Script
# This script starts all microservices with Java 17

Write-Host "=== InvestTrack Services Startup ===" -ForegroundColor Cyan
Write-Host ""

# Set Java 17
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "C:\Program Files\Java\jdk-17\bin;" + $env:PATH

Write-Host "Using Java:" (java -version 2>&1 | Select-Object -First 1) -ForegroundColor Green
Write-Host ""

# Start Config Server
Write-Host "[1/5] Starting Config Server on port 8888..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='C:\Program Files\Java\jdk-17'; `$env:PATH='C:\Program Files\Java\jdk-17\bin;' + `$env:PATH; cd '$PSScriptRoot\config-server'; mvn spring-boot:run"
Start-Sleep -Seconds 15

# Start Eureka Server
Write-Host "[2/5] Starting Eureka Server on port 8761..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='C:\Program Files\Java\jdk-17'; `$env:PATH='C:\Program Files\Java\jdk-17\bin;' + `$env:PATH; cd '$PSScriptRoot\eureka-server'; mvn spring-boot:run"
Start-Sleep -Seconds 15

# Start Market Service
Write-Host "[3/5] Starting Market Service on port 8081..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='C:\Program Files\Java\jdk-17'; `$env:PATH='C:\Program Files\Java\jdk-17\bin;' + `$env:PATH; cd '$PSScriptRoot\market-service'; mvn spring-boot:run"
Start-Sleep -Seconds 10

# Start Wallet Service
Write-Host "[4/5] Starting Wallet Service on port 8082..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='C:\Program Files\Java\jdk-17'; `$env:PATH='C:\Program Files\Java\jdk-17\bin;' + `$env:PATH; cd '$PSScriptRoot\wallet-service'; mvn spring-boot:run"
Start-Sleep -Seconds 10

# Start Gateway Service
Write-Host "[5/5] Starting Gateway Service on port 8080..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$env:JAVA_HOME='C:\Program Files\Java\jdk-17'; `$env:PATH='C:\Program Files\Java\jdk-17\bin;' + `$env:PATH; cd '$PSScriptRoot\gateway-service'; mvn spring-boot:run"

Write-Host ""
Write-Host "=== All services are starting! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Cyan
Write-Host "  Config Server:  http://localhost:8888" -ForegroundColor White
Write-Host "  Eureka Server:  http://localhost:8761" -ForegroundColor White  
Write-Host "  Gateway:        http://localhost:8080" -ForegroundColor White
Write-Host "  Market Service: http://localhost:8081" -ForegroundColor White
Write-Host "  Wallet Service: http://localhost:8082" -ForegroundColor White
Write-Host ""
Write-Host "  Market H2 Console: http://localhost:8081/h2-console" -ForegroundColor Yellow
Write-Host "  Wallet H2 Console: http://localhost:8082/h2-console" -ForegroundColor Yellow
Write-Host ""
Write-Host "Wait 30-60 seconds for all services to fully start" -ForegroundColor Magenta
