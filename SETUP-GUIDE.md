# InvestTrack - Setup Guide

## Current Status

✅ **Running Services:**
- Config Server (Port 8888) - ACTIVE
- Eureka Server (Port 8761) - ACTIVE

⚠️ **Issues:**
- Market Service (Port 8081) - Compilation Error
- Wallet Service (Port 8082) - Compilation Error  
- Gateway Service (Port 8080) - Compilation Error
- Frontend (Port 3000) - Not Started

## Problem

The application services are failing to compile due to a **Java 25 + Lombok + Maven Compiler Plugin** compatibility issue.

**Error:** `Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`

This is a known issue where:
- System is running Java 25.0.1
- Maven Compiler Plugin (3.11.0/3.13.0) doesn't fully support Java 25
- Lombok annotation processing fails

## Prerequisites

Before running the application, ensure you have:

1. **Java 17 LTS** (Required for backend services)
   - Download: [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17)

2. **Node.js 18+ and npm** (Required for frontend)
   - Download: [Node.js Official Site](https://nodejs.org/)
   - Verify: `node --version` and `npm --version`

3. **Maven 3.9+** (Should already be installed)
   - Verify: `mvn --version`

4. **Git** (For cloning and managing repositories)
   - Verify: `git --version`

## Solution Options

### Option 1: Install Java 17 (Recommended)

1. Download and install Java 17 LTS from:
   - [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
   - [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17)

2. Set JAVA_HOME environment variable:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
   $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
   ```

3. Verify Java version:
   ```powershell
   java -version
   # Should show: java version "17.x.x"
   ```

4. Then run the services:
   ```powershell
   cd c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack
   .\start-all-services.ps1
   ```

### Option 2: Use IntelliJ IDEA or Eclipse

Modern IDEs have better Lombok support with Java 25:

1. Open the `investtrack` folder in IntelliJ IDEA or Eclipse
2. Install Lombok plugin (if not already installed)
3. Enable annotation processing in IDE settings
4. Right-click each service and select "Run" or "Debug"

### Option 3: Wait for Updates

- Upgrade to Spring Boot 3.3+ when available (better Java 25 support)
- Use Lombok Edge release: `1.18.36` (already configured)
- Wait for Maven Compiler Plugin 3.14+ release

## Quick Start (When Java 17 is Installed)

### 1. Start Backend Services

```powershell
# Start all backend services
cd c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack
.\start-all-services.ps1

# Or start individually:
cd config-server; mvn spring-boot:run  # Terminal 1
cd eureka-server; mvn spring-boot:run  # Terminal 2
cd market-service; mvn spring-boot:run # Terminal 3
cd wallet-service; mvn spring-boot:run # Terminal 4
cd gateway-service; mvn spring-boot:run# Terminal 5
```

### 2. Start Frontend

```powershell
cd c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\frontend
npm install
npm start
```

### 3. Access Application

- **Frontend:** http://localhost:3000
- **Gateway API:** http://localhost:8080
- **Eureka Dashboard:** http://localhost:8761
- **Config Server:** http://localhost:8888

## Service Health Checks

```powershell
# Check Config Server
Invoke-RestMethod http://localhost:8888/actuator/health | ConvertTo-Json

# Check Eureka Server
Invoke-RestMethod http://localhost:8761/actuator/health | ConvertTo-Json

# Check Market Service
Invoke-RestMethod http://localhost:8081/actuator/health | ConvertTo-Json

# Check Wallet Service
Invoke-RestMethod http://localhost:8082/actuator/health | ConvertTo-Json

# Check Gateway
Invoke-RestMethod http://localhost:8080/actuator/health | ConvertTo-Json
```

## Test API Endpoints

See [API-DOCUMENTATION.md](API-DOCUMENTATION.md) for complete API reference.

### Example: Get All Assets
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/assets" -Method GET | ConvertTo-Json
```

### Example: Create Asset
```powershell
$body = @{
    symbol = "AAPL"
    name = "Apple Inc."
    currentPrice = 150.00
    type = "STOCK"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/assets" `
    -Method POST `
    -Body $body `
    -ContentType "application/json" | ConvertTo-Json
```

## Architecture

```
┌─────────────┐
│   Frontend  │  http://localhost:3000
│  (React)    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Gateway   │  http://localhost:8080
│  (Port 8080)│
└──────┬──────┘
       │
       ├──────────┬─────────────┬──────────────┐
       ▼          ▼             ▼              ▼
   ┌────────┐ ┌────────┐ ┌────────────┐ ┌───────────┐
   │ Market │ │ Wallet │ │   Eureka   │ │  Config   │
   │  8081  │ │  8082  │ │    8761    │ │   8888    │
   └────────┘ └────────┘ └────────────┘ └───────────┘
       │          │              │              │
       ▼          ▼              ▼              ▼
     H2 DB      H2 DB    Service Registry   GitHub Repo
```

## Troubleshooting

### Port Already in Use
```powershell
# Find and kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Clean Build
```powershell
cd market-service
mvn clean install -DskipTests
```

### Clear Maven Cache
```powershell
Remove-Item -Recurse -Force "$env:USERPROFILE\.m2\repository\com\investtrack"
```

## Next Steps

1. ✅ Install Java 17
2. ✅ Start all backend services
3. ✅ Install frontend dependencies (`npm install`)
4. ✅ Start frontend (`npm start`)
5. ✅ Test application at http://localhost:3000
6. ✅ Review API documentation

## Support

- GitHub Repository: https://github.com/TNT-747/investtrack
- Config Repository: https://github.com/TNT-747/investtrack-config-repo
- API Documentation: [API-DOCUMENTATION.md](API-DOCUMENTATION.md)
