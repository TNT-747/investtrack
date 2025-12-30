# Service Health Check Script

Write-Host "=== InvestTrack Services Health Check ===" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{Name="Config Server"; Port=8888; Path="/actuator/health"},
    @{Name="Eureka Server"; Port=8761; Path="/actuator/health"},
    @{Name="Gateway Service"; Port=8080; Path="/actuator/health"},
    @{Name="Market Service"; Port=8081; Path="/actuator/health"},
    @{Name="Wallet Service"; Port=8082; Path="/actuator/health"}
)

foreach ($service in $services) {
    $serviceName = $service.Name
    $port = $service.Port
    Write-Host "Checking $serviceName (port $port)..." -ForegroundColor Yellow -NoNewline
    
    try {
        $url = "http://localhost:$port" + $service.Path
        $response = Invoke-WebRequest -Uri $url -TimeoutSec 5 -UseBasicParsing
        if ($response.StatusCode -eq 200) {
            $content = [System.Text.Encoding]::UTF8.GetString($response.Content) | ConvertFrom-Json
            $status = $content.status
            if ($status -eq "UP") {
                Write-Host " OK - HEALTHY" -ForegroundColor Green
            } else {
                Write-Host " WARNING - $status" -ForegroundColor Yellow
            }
        } else {
            Write-Host " ERROR - Status: $($response.StatusCode)" -ForegroundColor Red
        }
    } catch {
        Write-Host " ERROR - NOT RESPONDING" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Check complete!" -ForegroundColor Cyan
Write-Host ""
