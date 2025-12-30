# InvestTrack Services Health Check Script

Write-Host "===== InvestTrack Services Health Check =====" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{Name="Config Server"; Port=8888; Url="http://localhost:8888/actuator/health"},
    @{Name="Eureka Server"; Port=8761; Url="http://localhost:8761/actuator/health"},
    @{Name="Market Service"; Port=8081; Url="http://localhost:8081/actuator/health"},
    @{Name="Wallet Service"; Port=8082; Url="http://localhost:8082/actuator/health"},
    @{Name="Gateway Service"; Port=8080; Url="http://localhost:8080/actuator/health"}
)

$runningCount = 0
$totalServices = $services.Count

foreach ($service in $services) {
    try {
        $response = Invoke-WebRequest -Uri $service.Url -TimeoutSec 3 -UseBasicParsing -ErrorAction Stop
        $status = ($response.Content | ConvertFrom-Json).status
        
        if ($status -eq "UP") {
            Write-Host "[OK] $($service.Name) (port $($service.Port)): UP" -ForegroundColor Green
            $runningCount++
        } else {
            Write-Host "[WARN] $($service.Name) (port $($service.Port)): $status" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "[DOWN] $($service.Name) (port $($service.Port)): DOWN" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "===== Summary =====" -ForegroundColor Cyan
Write-Host "$runningCount / $totalServices services running" -ForegroundColor $(if ($runningCount -eq $totalServices) {"Green"} else {"Yellow"})

if ($runningCount -ge 2) {
    Write-Host ""
    Write-Host "Testing Config Server configuration fetch..." -ForegroundColor Yellow
    try {
        $configTest = Invoke-WebRequest -Uri "http://localhost:8888/market-service/default" -TimeoutSec 3 -UseBasicParsing
        Write-Host "[OK] Config Server successfully fetching configurations from GitHub" -ForegroundColor Green
    } catch {
        Write-Host "[FAIL] Config Server configuration fetch failed" -ForegroundColor Red
    }
}

if ($runningCount -eq $totalServices) {
    Write-Host ""
    Write-Host "Testing Eureka service registration..." -ForegroundColor Yellow
    try {
        $eurekaApps = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -Headers @{Accept="application/json"} -TimeoutSec 3 -UseBasicParsing
        $appsJson = $eurekaApps.Content | ConvertFrom-Json
        $registeredApps = $appsJson.applications.application
        Write-Host "[OK] Services registered in Eureka: $($registeredApps.Count)" -ForegroundColor Green
        foreach ($app in $registeredApps) {
            Write-Host "  - $($app.name)" -ForegroundColor Cyan
        }
    } catch {
        Write-Host "[FAIL] Could not fetch Eureka registrations" -ForegroundColor Red
    }
}

Write-Host ""
