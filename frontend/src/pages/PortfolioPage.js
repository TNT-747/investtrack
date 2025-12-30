import React, { useState, useEffect } from 'react';
import { walletService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Wallet, TrendingUp, TrendingDown, History } from 'lucide-react';

const PortfolioPage = () => {
  const [portfolio, setPortfolio] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showTradeModal, setShowTradeModal] = useState(false);
  const [tradeData, setTradeData] = useState({
    assetSymbol: '',
    quantity: '',
    type: 'BUY'
  });
  const [activeTab, setActiveTab] = useState('portfolio');
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      fetchData();
    }
  }, [user]);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [portfolioData, transactionsData] = await Promise.all([
        walletService.getUserPortfolio(user.username),
        walletService.getUserTransactions(user.username)
      ]);
      setPortfolio(portfolioData);
      setTransactions(transactionsData);
      setError('');
    } catch (err) {
      setError('Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  const handleTrade = async (e) => {
    e.preventDefault();
    try {
      const response = await walletService.executeTrade({
        userId: user.username,
        assetSymbol: tradeData.assetSymbol.toUpperCase(),
        quantity: parseFloat(tradeData.quantity),
        type: tradeData.type
      });
      
      if (response.success) {
        setShowTradeModal(false);
        setTradeData({ assetSymbol: '', quantity: '', type: 'BUY' });
        fetchData();
      } else {
        setError(response.message);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to execute trade');
    }
  };

  const calculateTotalValue = () => {
    return portfolio.reduce((sum, item) => sum + (item.quantity * item.averageBuyPrice), 0);
  };

  return (
    <div className="container">
      {/* Summary Cards */}
      <div className="grid grid-2" style={{ marginBottom: '24px' }}>
        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
            <div style={{ 
              width: '40px', 
              height: '40px', 
              borderRadius: '8px', 
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}>
              <Wallet size={20} color="white" />
            </div>
            <div>
              <p style={{ fontSize: '12px', color: '#6b7280' }}>Total Portfolio Value</p>
              <h3 style={{ fontSize: '24px', fontWeight: '700', color: '#111827' }}>
                ${calculateTotalValue().toFixed(2)}
              </h3>
            </div>
          </div>
        </div>

        <div className="card">
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' }}>
            <div style={{ 
              width: '40px', 
              height: '40px', 
              borderRadius: '8px', 
              background: '#10b981',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}>
              <TrendingUp size={20} color="white" />
            </div>
            <div>
              <p style={{ fontSize: '12px', color: '#6b7280' }}>Total Holdings</p>
              <h3 style={{ fontSize: '24px', fontWeight: '700', color: '#111827' }}>
                {portfolio.length}
              </h3>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button
              className={`btn ${activeTab === 'portfolio' ? 'btn-primary' : 'btn-secondary'}`}
              onClick={() => setActiveTab('portfolio')}
            >
              Portfolio
            </button>
            <button
              className={`btn ${activeTab === 'transactions' ? 'btn-primary' : 'btn-secondary'}`}
              onClick={() => setActiveTab('transactions')}
            >
              <History size={16} style={{ marginRight: '8px', display: 'inline' }} />
              Transactions
            </button>
          </div>
          <button 
            className="btn btn-success"
            onClick={() => setShowTradeModal(true)}
          >
            <TrendingUp size={16} style={{ marginRight: '8px', display: 'inline' }} />
            New Trade
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <div className="loading">Loading...</div>
        ) : (
          <>
            {activeTab === 'portfolio' ? (
              <div style={{ overflowX: 'auto' }}>
                <table className="table">
                  <thead>
                    <tr>
                      <th>Asset Symbol</th>
                      <th>Quantity</th>
                      <th>Avg Buy Price</th>
                      <th>Total Value</th>
                    </tr>
                  </thead>
                  <tbody>
                    {portfolio.length === 0 ? (
                      <tr>
                        <td colSpan="4" style={{ textAlign: 'center', padding: '40px', color: '#6b7280' }}>
                          No holdings yet. Execute your first trade!
                        </td>
                      </tr>
                    ) : (
                      portfolio.map((item, index) => (
                        <tr key={index}>
                          <td><strong>{item.assetSymbol}</strong></td>
                          <td>{item.quantity.toFixed(2)}</td>
                          <td>${item.averageBuyPrice.toFixed(2)}</td>
                          <td>
                            <strong style={{ color: '#10b981' }}>
                              ${(item.quantity * item.averageBuyPrice).toFixed(2)}
                            </strong>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            ) : (
              <div style={{ overflowX: 'auto' }}>
                <table className="table">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Type</th>
                      <th>Asset</th>
                      <th>Quantity</th>
                      <th>Price</th>
                      <th>Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.length === 0 ? (
                      <tr>
                        <td colSpan="6" style={{ textAlign: 'center', padding: '40px', color: '#6b7280' }}>
                          No transactions found
                        </td>
                      </tr>
                    ) : (
                      transactions.map((tx) => (
                        <tr key={tx.id}>
                          <td>{new Date(tx.timestamp).toLocaleDateString()}</td>
                          <td>
                            <span className={`badge ${tx.type === 'BUY' ? 'badge-success' : 'badge-danger'}`}>
                              {tx.type}
                            </span>
                          </td>
                          <td><strong>{tx.assetSymbol}</strong></td>
                          <td>{tx.quantity.toFixed(2)}</td>
                          <td>${tx.price.toFixed(2)}</td>
                          <td>
                            <strong style={{ color: tx.type === 'BUY' ? '#ef4444' : '#10b981' }}>
                              {tx.type === 'BUY' ? '-' : '+'}${(tx.quantity * tx.price).toFixed(2)}
                            </strong>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}
      </div>

      {/* Trade Modal */}
      {showTradeModal && (
        <div className="modal-overlay" onClick={() => setShowTradeModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3 style={{ fontSize: '20px', fontWeight: '700', marginBottom: '20px' }}>
              Execute Trade
            </h3>
            <form onSubmit={handleTrade}>
              <div className="input-group">
                <label>Trade Type</label>
                <select
                  value={tradeData.type}
                  onChange={(e) => setTradeData({ ...tradeData, type: e.target.value })}
                  required
                >
                  <option value="BUY">Buy</option>
                  <option value="SELL">Sell</option>
                </select>
              </div>
              <div className="input-group">
                <label>Asset Symbol</label>
                <input
                  type="text"
                  value={tradeData.assetSymbol}
                  onChange={(e) => setTradeData({ ...tradeData, assetSymbol: e.target.value.toUpperCase() })}
                  placeholder="e.g., AAPL"
                  required
                />
              </div>
              <div className="input-group">
                <label>Quantity</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={tradeData.quantity}
                  onChange={(e) => setTradeData({ ...tradeData, quantity: e.target.value })}
                  placeholder="0.00"
                  required
                />
              </div>
              <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                <button 
                  type="submit" 
                  className={`btn ${tradeData.type === 'BUY' ? 'btn-success' : 'btn-danger'}`}
                  style={{ flex: 1 }}
                >
                  {tradeData.type === 'BUY' ? 'Buy' : 'Sell'}
                </button>
                <button 
                  type="button" 
                  className="btn btn-secondary" 
                  style={{ flex: 1 }}
                  onClick={() => setShowTradeModal(false)}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PortfolioPage;
