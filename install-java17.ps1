# Download and Install Java 17
Write-Host "===== Java 17 Installation =====" -ForegroundColor Cyan
Write-Host ""

# Check if Java 17 is already installed
$javaHome17 = "C:\Program Files\Java\jdk-17"
if (Test-Path $javaHome17) {
    Write-Host "Java 17 already installed at: $javaHome17" -ForegroundColor Green
    Write-Host "Setting JAVA_HOME..." -ForegroundColor Yellow
    $env:JAVA_HOME = $javaHome17
    $env:PATH = "$javaHome17\bin;$env:PATH"
    Write-Host "Done! Run 'java -version' to verify." -ForegroundColor Green
    exit 0
}

# Download URL for Eclipse Temurin JDK 17 (OpenJDK)
$downloadUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.msi"
$installerPath = "$env:TEMP\jdk-17-installer.msi"

Write-Host "Downloading Eclipse Temurin JDK 17 (OpenJDK)..." -ForegroundColor Yellow
Write-Host "URL: $downloadUrl" -ForegroundColor Gray
Write-Host ""

try {
    Invoke-WebRequest -Uri $downloadUrl -OutFile $installerPath -UseBasicParsing
    Write-Host "[OK] Downloaded successfully" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Download failed: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Alternative: Download manually from:" -ForegroundColor Yellow
    Write-Host "https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html" -ForegroundColor Cyan
    exit 1
}

Write-Host ""
Write-Host "Installing Java 17..." -ForegroundColor Yellow
Write-Host "This may take a few minutes..." -ForegroundColor Gray

try {
    # MSI installation
    Start-Process msiexec.exe -ArgumentList "/i `"$installerPath`" /quiet /norestart ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome" -Wait -NoNewWindow
    Write-Host "[OK] Installation complete" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Installation failed: $_" -ForegroundColor Red
    exit 1
}

# Clean up installer
Remove-Item $installerPath -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "===== Next Steps =====" -ForegroundColor Cyan
Write-Host "1. Close and reopen PowerShell/Terminal" -ForegroundColor Yellow
Write-Host "2. Verify installation: java -version" -ForegroundColor Yellow
Write-Host "3. If needed, set JAVA_HOME manually:" -ForegroundColor Yellow
Write-Host "   `$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'" -ForegroundColor Gray
Write-Host ""
