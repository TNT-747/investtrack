# InvestTrack API Documentation

Complete API reference for all microservices endpoints.

**Base URL (Gateway):** `http://localhost:8080`

---

## Authentication Service

### POST /api/auth/login
Authenticate user and receive JWT token.

**Request:**
```json
{
  "username": "user1",
  "password": "password"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "user1",
  "message": "Login successful"
}
```

**Response (401 Unauthorized):**
```json
{
  "token": null,
  "username": null,
  "message": "Invalid username or password"
}
```

**Demo Users:**
- `user1` / `password`
- `user2` / `password`
- `admin` / `admin123`

---

### GET /api/auth/health
Health check endpoint for gateway service.

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "gateway-service"
}
```

---

## Market Service

All market endpoints require JWT authentication via `Authorization: Bearer {token}` header.

### GET /api/assets
Get all available assets.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "currentPrice": 150.00,
    "type": "STOCK"
  },
  {
    "id": 2,
    "symbol": "BTC",
    "name": "Bitcoin",
    "currentPrice": 45000.00,
    "type": "CRYPTO"
  }
]
```

---

### GET /api/assets/{id}
Get asset by ID.

**Path Parameters:**
- `id` (Long) - Asset ID

**Response (200 OK):**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 150.00,
  "type": "STOCK"
}
```

**Response (404 Not Found):**
```json
{
  "message": "Asset not found with id: 1"
}
```

---

### GET /api/assets/symbol/{symbol}
Get asset by symbol.

**Path Parameters:**
- `symbol` (String) - Asset symbol (e.g., "AAPL")

**Response (200 OK):**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 150.00,
  "type": "STOCK"
}
```

**Response (404 Not Found):**
```json
{
  "message": "Asset not found with symbol: AAPL"
}
```

---

### GET /api/assets/type/{type}
Get assets by type.

**Path Parameters:**
- `type` (String) - Asset type: `STOCK`, `CRYPTO`, or `COMMODITY`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "currentPrice": 150.00,
    "type": "STOCK"
  },
  {
    "id": 3,
    "symbol": "GOOGL",
    "name": "Alphabet Inc.",
    "currentPrice": 2800.00,
    "type": "STOCK"
  }
]
```

---

### POST /api/assets
Create a new asset.

**Request:**
```json
{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 150.00,
  "type": "STOCK"
}
```

**Validation Rules:**
- `symbol`: Required, uppercase
- `name`: Required
- `currentPrice`: Required, must be > 0
- `type`: Required, must be one of: STOCK, CRYPTO, COMMODITY

**Response (201 Created):**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 150.00,
  "type": "STOCK"
}
```

**Response (400 Bad Request):**
```json
{
  "message": "Asset with symbol AAPL already exists"
}
```

---

### PUT /api/assets/{id}
Update an existing asset (full update).

**Path Parameters:**
- `id` (Long) - Asset ID

**Request:**
```json
{
  "symbol": "AAPL",
  "name": "Apple Inc. (Updated)",
  "currentPrice": 155.00,
  "type": "STOCK"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "name": "Apple Inc. (Updated)",
  "currentPrice": 155.00,
  "type": "STOCK"
}
```

**Response (404 Not Found):**
```json
{
  "message": "Asset not found with id: 1"
}
```

---

### PATCH /api/assets/{id}/price
Update only the asset price.

**Path Parameters:**
- `id` (Long) - Asset ID

**Request:**
```json
{
  "currentPrice": 155.50
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 155.50,
  "type": "STOCK"
}
```

**Response (404 Not Found):**
```json
{
  "message": "Asset not found with id: 1"
}
```

---

### DELETE /api/assets/{id}
Delete an asset.

**Path Parameters:**
- `id` (Long) - Asset ID

**Response (204 No Content):**
No response body

**Response (404 Not Found):**
```json
{
  "message": "Asset not found with id: 1"
}
```

---

## Wallet Service

All wallet endpoints require JWT authentication via `Authorization: Bearer {token}` header.

### GET /api/wallets/user/{userId}
Get user's portfolio (all holdings).

**Path Parameters:**
- `userId` (String) - User identifier (username)

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "userId": "user1",
    "assetSymbol": "AAPL",
    "quantity": 10.00,
    "averageBuyPrice": 150.00
  },
  {
    "id": 2,
    "userId": "user1",
    "assetSymbol": "BTC",
    "quantity": 0.5,
    "averageBuyPrice": 45000.00
  }
]
```

---

### POST /api/wallets/trade
Execute a buy or sell trade.

**Request:**
```json
{
  "userId": "user1",
  "assetSymbol": "AAPL",
  "quantity": 10,
  "type": "BUY"
}
```

**Trade Types:**
- `BUY` - Purchase asset
- `SELL` - Sell asset (must have sufficient quantity)

**Validation Rules:**
- `userId`: Required
- `assetSymbol`: Required, must exist in market service
- `quantity`: Required, must be > 0
- `type`: Required, must be BUY or SELL

**Response (201 Created) - Success:**
```json
{
  "success": true,
  "message": "Buy order executed successfully",
  "wallet": {
    "id": 1,
    "userId": "user1",
    "assetSymbol": "AAPL",
    "quantity": 10.00,
    "averageBuyPrice": 150.00
  },
  "transaction": {
    "id": 1,
    "walletId": 1,
    "type": "BUY",
    "assetSymbol": "AAPL",
    "quantity": 10,
    "price": 150.00,
    "timestamp": "2025-12-30T08:16:12.336861"
  }
}
```

**Response (503 Service Unavailable) - Market Service Down:**
```json
{
  "success": false,
  "message": "Market Service is currently unavailable. Please try again later.",
  "wallet": null,
  "transaction": null
}
```

**Response (400 Bad Request) - Insufficient Quantity:**
```json
{
  "success": false,
  "message": "Insufficient quantity for sell order",
  "wallet": null,
  "transaction": null
}
```

**Response (404 Not Found) - Asset Not Found:**
```json
{
  "success": false,
  "message": "Asset not found: INVALID",
  "wallet": null,
  "transaction": null
}
```

---

### GET /api/wallets/user/{userId}/transactions
Get user's transaction history (filtered by configured history-days).

**Path Parameters:**
- `userId` (String) - User identifier (username)

**Configuration:**
- Returns transactions from the last N days (configured in `invest-config.wallet.history-days`, default: 30)

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "walletId": 1,
    "type": "BUY",
    "assetSymbol": "AAPL",
    "quantity": 10.00,
    "price": 150.00,
    "timestamp": "2025-12-30T08:16:12.336861"
  },
  {
    "id": 2,
    "walletId": 1,
    "type": "SELL",
    "assetSymbol": "AAPL",
    "quantity": 5.00,
    "price": 155.00,
    "timestamp": "2025-12-30T10:30:45.123456"
  }
]
```

---

## Error Responses

All endpoints may return the following error responses:

### 401 Unauthorized
Missing or invalid JWT token.

```json
{
  "message": "Unauthorized"
}
```

### 500 Internal Server Error
Server-side error.

```json
{
  "message": "An unexpected error occurred"
}
```

---

## Infrastructure Services

### Config Server (Port 8888)

Centralized configuration management server using Spring Cloud Config.

#### GET /{application}/{profile}
Get configuration for a specific application and profile.

**URL:** `http://localhost:8888/{application}/{profile}`

**Examples:**
```bash
# Get market-service default profile config
curl http://localhost:8888/market-service/default

# Get wallet-service default profile config
curl http://localhost:8888/wallet-service/default

# Get gateway-service default profile config
curl http://localhost:8888/gateway-service/default
```

**Response (200 OK):**
```json
{
  "name": "market-service",
  "profiles": ["default"],
  "label": null,
  "version": "10a15d68ff204d7f95193540",
  "state": null,
  "propertySources": [
    {
      "name": "https://github.com/TNT-747/investtrack-config-repo.git/market-service.properties",
      "source": {
        "server.port": "8081",
        "spring.application.name": "market-service",
        "spring.datasource.url": "jdbc:h2:mem:marketdb"
      }
    }
  ]
}
```

#### GET /actuator/health
Check config server health status.

**URL:** `http://localhost:8888/actuator/health`

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 135771664384,
        "free": 88237924352,
        "threshold": 10485760
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

### Eureka Server (Port 8761)

Service discovery and registry server.

#### GET /
Eureka dashboard - Web UI showing all registered services.

**URL:** `http://localhost:8761`

#### GET /eureka/apps
Get all registered applications (XML format by default).

**URL:** `http://localhost:8761/eureka/apps`

**Response Headers:**
- Accept: application/json (for JSON response)

**Response (200 OK):**
```json
{
  "applications": {
    "application": [
      {
        "name": "MARKET-SERVICE",
        "instance": [
          {
            "instanceId": "host.docker.internal:market-service:8081",
            "hostName": "host.docker.internal",
            "app": "MARKET-SERVICE",
            "ipAddr": "10.0.0.4",
            "status": "UP",
            "port": {
              "$": 8081,
              "@enabled": "true"
            }
          }
        ]
      },
      {
        "name": "WALLET-SERVICE",
        "instance": [
          {
            "instanceId": "host.docker.internal:wallet-service:8082",
            "hostName": "host.docker.internal",
            "app": "WALLET-SERVICE",
            "ipAddr": "10.0.0.5",
            "status": "UP",
            "port": {
              "$": 8082,
              "@enabled": "true"
            }
          }
        ]
      }
    ]
  }
}
```

#### GET /eureka/apps/{appName}
Get specific application registration details.

**URL:** `http://localhost:8761/eureka/apps/MARKET-SERVICE`

**Path Parameters:**
- `appName` (String) - Application name in UPPERCASE

#### GET /actuator/health
Check Eureka server health status.

**URL:** `http://localhost:8761/actuator/health`

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "discoveryComposite": {
      "status": "UP",
      "components": {
        "discoveryClient": {
          "status": "UP",
          "details": {
            "services": []
          }
        },
        "eureka": {
          "description": "Remote status from Eureka server",
          "status": "UNKNOWN"
        }
      }
    }
  }
}
```

---

## Service Health Endpoints

All services expose Actuator health endpoints:

### Config Server Health
```bash
curl http://localhost:8888/actuator/health
```

### Eureka Server Health
```bash
curl http://localhost:8761/actuator/health
```

### Market Service Health
```bash
curl http://localhost:8081/actuator/health
```

### Wallet Service Health
```bash
curl http://localhost:8082/actuator/health
```

### Gateway Service Health
```bash
curl http://localhost:8080/actuator/health
```

**Standard Health Response:**
```json
{
  "status": "UP",
  "components": {
    "clientConfigServer": {
      "status": "UP"
    },
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "discoveryComposite": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## Service Architecture

### Service Discovery (Eureka)
- **URL:** http://localhost:8761
- All services register with Eureka for dynamic discovery
- Dashboard available at Eureka URL

### Config Server
- **URL:** http://localhost:8888
- Fetches configuration from GitHub: https://github.com/TNT-747/investtrack-config-repo.git
- Bootstrap properties connect services to config server

### Direct Service Access (Bypass Gateway)

**Market Service:**
- URL: http://localhost:8081
- Endpoints: `/api/assets/*`

**Wallet Service:**
- URL: http://localhost:8082
- Endpoints: `/api/wallets/*`

**Note:** Direct access bypasses JWT authentication. Use gateway for production.

---

## Circuit Breaker

Wallet Service uses Resilience4j Circuit Breaker for Market Service calls:

**Configuration:**
- Sliding window size: 10 requests
- Failure rate threshold: 50%
- Wait duration in open state: 10 seconds
- Half-open state calls: 3

**Behavior:**
When Market Service is unavailable, the circuit breaker opens and returns a fallback response instead of failing.

---

## Authentication Flow

1. **Login:** POST `/api/auth/login` with credentials
2. **Receive Token:** Store JWT token from response
3. **Authenticated Requests:** Include `Authorization: Bearer {token}` header
4. **Logout:** Remove stored token (client-side)

---

## Example Usage with cURL

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password"}'
```

### Create Asset
```bash
curl -X POST http://localhost:8080/api/assets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","name":"Apple Inc.","currentPrice":150.00,"type":"STOCK"}'
```

### Get All Assets
```bash
curl http://localhost:8080/api/assets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Execute Trade
```bash
curl -X POST http://localhost:8080/api/wallets/trade \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":"user1","assetSymbol":"AAPL","quantity":10,"type":"BUY"}'
```

### Get Portfolio
```bash
curl http://localhost:8080/api/wallets/user/user1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Rate Limits

Currently no rate limiting is implemented. Recommended for production deployment.

---

## API Versioning

Current version: v1 (implicit)
Future versions will use URL path versioning: `/api/v2/assets`

---

## Support

For issues or questions, refer to the main project README.md
