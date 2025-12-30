# InvestTrack Frontend

Modern React frontend for InvestTrack portfolio management system.

## Features

- 🔐 **Authentication** - JWT-based login system
- 📊 **Market Management** - Create, update, delete assets
- 💰 **Portfolio Tracking** - View holdings and total value
- 📈 **Trading** - Execute buy/sell trades
- 📜 **Transaction History** - View last 30 days of trades
- 🎨 **Modern UI** - Gradient design with Lucide icons

## Installation

```bash
cd frontend
npm install
```

## Development

```bash
npm start
```

Runs on [http://localhost:3000](http://localhost:3000)

## Build

```bash
npm run build
```

## Backend Connection

The frontend connects to the backend gateway at `http://localhost:8080`

Ensure the following services are running:
- Gateway Service (port 8080)
- Market Service (port 8081)
- Wallet Service (port 8082)
- Eureka Server (port 8761)
- Config Server (port 8888)

## Demo Users

- user1 / password
- user2 / password
- admin / admin123

## Pages

- `/login` - Authentication page
- `/dashboard` - Main dashboard with overview
- `/market` - Asset management (CRUD operations)
- `/portfolio` - Portfolio and trading interface
