import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth Service
export const authService = {
  login: async (username, password) => {
    const response = await api.post('/api/auth/login', { username, password });
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('username', response.data.username);
    }
    return response.data;
  },
  
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  },
  
  getUsername: () => localStorage.getItem('username'),
  
  isAuthenticated: () => !!localStorage.getItem('token'),
};

// Market Service
export const marketService = {
  getAllAssets: async () => {
    const response = await api.get('/api/assets');
    return response.data;
  },
  
  getAssetById: async (id) => {
    const response = await api.get(`/api/assets/${id}`);
    return response.data;
  },
  
  getAssetBySymbol: async (symbol) => {
    const response = await api.get(`/api/assets/symbol/${symbol}`);
    return response.data;
  },
  
  getAssetsByType: async (type) => {
    const response = await api.get(`/api/assets/type/${type}`);
    return response.data;
  },
  
  createAsset: async (asset) => {
    const response = await api.post('/api/assets', asset);
    return response.data;
  },
  
  updateAsset: async (id, asset) => {
    const response = await api.put(`/api/assets/${id}`, asset);
    return response.data;
  },
  
  updateAssetPrice: async (id, currentPrice) => {
    const response = await api.patch(`/api/assets/${id}/price`, { currentPrice });
    return response.data;
  },
  
  deleteAsset: async (id) => {
    await api.delete(`/api/assets/${id}`);
  },
};

// Wallet Service
export const walletService = {
  getUserPortfolio: async (userId) => {
    const response = await api.get(`/api/wallets/user/${userId}`);
    return response.data;
  },
  
  executeTrade: async (trade) => {
    const response = await api.post('/api/wallets/trade', trade);
    return response.data;
  },
  
  getUserTransactions: async (userId) => {
    const response = await api.get(`/api/wallets/user/${userId}/transactions`);
    return response.data;
  },
};

export default api;
