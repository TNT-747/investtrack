# InvestTrack Setup Instructions

## Current Issue: Java Version Incompatibility

Your system is running **Java 25**, but the project requires **Java 17** for proper Lombok annotation processing and compilation.

## Solution: Install Java 17

### Option 1: Download from Adoptium (Recommended)

1. **Download JDK 17:**
   - Visit: https://adoptium.net/temurin/releases/?version=17
   - Select:
     - Version: **17 - LTS**
     - Operating System: **Windows**
     - Architecture: **x64**
     - Package Type: **JDK**
     - Download: **`.msi` installer**

2. **Install JDK 17:**
   - Run the downloaded `.msi` file
   - During installation, make sure to check:
     - ✅ Set JAVA_HOME variable
     - ✅ Add to PATH
     - ✅ Associate .jar files with Java

3. **Verify Installation:**
   ```powershell
   # Close and reopen PowerShell/Terminal, then run:
   java -version
   # Should show: openjdk version "17.0.x"
   ```

### Option 2: Download from Oracle

1. Visit: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Download **Windows x64 Installer** 
3. Install and follow the prompts

## After Installing Java 17

### Set Java 17 as Default (if you want to keep Java 25 installed)

**Temporarily for current session:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

**Permanently:**
1. Press `Win + X` → System
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "System variables":
   - Find `JAVA_HOME` → Edit → Set to: `C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot`
   - Find `Path` → Edit → Move Java 17 bin folder to the top

## Start All Services

Once Java 17 is installed:

```powershell
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack"
.\start-all-services.ps1
```

This script will:
1. Check if Config Server and Eureka are running (already started ✓)
2. Start Market Service (port 8081)
3. Start Wallet Service (port 8082)
4. Start Gateway Service (port 8080)
5. Start React Frontend (port 3000)

## Manual Service Startup

If you prefer to start services individually:

### Backend Services (in separate terminals):

```powershell
# 1. Config Server (if not running)
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\config-server"
mvn spring-boot:run

# 2. Eureka Server (if not running)
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\eureka-server"
mvn spring-boot:run

# 3. Market Service
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\market-service"
mvn spring-boot:run

# 4. Wallet Service
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\wallet-service"
mvn spring-boot:run

# 5. Gateway Service
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\gateway-service"
mvn spring-boot:run
```

### Frontend:

```powershell
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack\frontend"
npm install  # First time only
npm start
```

## Check Service Health

```powershell
cd "c:\Users\Kassimi\.gemini\antigravity\scratch\investtrack"
.\check-services.ps1
```

## Access URLs

- **Frontend:** http://localhost:3000
- **API Gateway:** http://localhost:8080
- **Eureka Dashboard:** http://localhost:8761
- **Config Server:** http://localhost:8888
- **Market Service:** http://localhost:8081
- **Wallet Service:** http://localhost:8082

## API Documentation

See [API-DOCUMENTATION.md](API-DOCUMENTATION.md) for complete API reference with examples.

## Troubleshooting

### Services won't start
- Ensure Java 17 is being used: `java -version`
- Check if ports are already in use
- Review logs in terminal windows

### Frontend won't start
- Ensure Node.js is installed: `node --version`
- Run `npm install` in the frontend directory
- Check if port 3000 is available

### Compilation errors
- Verify Java 17 is active
- Clean build: `mvn clean install`
- Check JAVA_HOME: `echo $env:JAVA_HOME`
