import React from 'react';
import { Link } from 'react-router-dom';
import { TrendingUp, Wallet, BarChart3, ArrowRight } from 'lucide-react';

const DashboardPage = () => {
  return (
    <div className="container">
      <div className="card" style={{ textAlign: 'center', marginBottom: '32px' }}>
        <h1 style={{ fontSize: '32px', fontWeight: '700', color: '#111827', marginBottom: '12px' }}>
          Welcome to InvestTrack
        </h1>
        <p style={{ fontSize: '18px', color: '#6b7280' }}>
          Manage your portfolio and track market assets
        </p>
      </div>

      <div className="grid grid-2">
        <Link to="/market" style={{ textDecoration: 'none' }}>
          <div className="card" style={{ 
            cursor: 'pointer', 
            transition: 'transform 0.3s ease, box-shadow 0.3s ease',
            border: '2px solid transparent'
          }}
          onMouseEnter={(e) => {
            e.currentTarget.style.transform = 'translateY(-4px)';
            e.currentTarget.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.15)';
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow = '0 4px 6px rgba(0, 0, 0, 0.1)';
          }}>
            <div style={{ 
              width: '64px', 
              height: '64px', 
              borderRadius: '12px', 
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '16px'
            }}>
              <TrendingUp size={32} color="white" />
            </div>
            <h3 style={{ fontSize: '20px', fontWeight: '700', color: '#111827', marginBottom: '8px' }}>
              Market Assets
            </h3>
            <p style={{ color: '#6b7280', marginBottom: '16px' }}>
              Browse and manage financial assets, update prices, and track market data
            </p>
            <div style={{ display: 'flex', alignItems: 'center', color: '#667eea', fontWeight: '600' }}>
              Go to Market <ArrowRight size={16} style={{ marginLeft: '8px' }} />
            </div>
          </div>
        </Link>

        <Link to="/portfolio" style={{ textDecoration: 'none' }}>
          <div className="card" style={{ 
            cursor: 'pointer', 
            transition: 'transform 0.3s ease, box-shadow 0.3s ease',
            border: '2px solid transparent'
          }}
          onMouseEnter={(e) => {
            e.currentTarget.style.transform = 'translateY(-4px)';
            e.currentTarget.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.15)';
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.transform = 'translateY(0)';
            e.currentTarget.style.boxShadow = '0 4px 6px rgba(0, 0, 0, 0.1)';
          }}>
            <div style={{ 
              width: '64px', 
              height: '64px', 
              borderRadius: '12px', 
              background: '#10b981',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: '16px'
            }}>
              <Wallet size={32} color="white" />
            </div>
            <h3 style={{ fontSize: '20px', fontWeight: '700', color: '#111827', marginBottom: '8px' }}>
              My Portfolio
            </h3>
            <p style={{ color: '#6b7280', marginBottom: '16px' }}>
              View your holdings, execute trades, and track transaction history
            </p>
            <div style={{ display: 'flex', alignItems: 'center', color: '#10b981', fontWeight: '600' }}>
              Go to Portfolio <ArrowRight size={16} style={{ marginLeft: '8px' }} />
            </div>
          </div>
        </Link>
      </div>

      <div className="card" style={{ marginTop: '32px', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <BarChart3 size={48} />
          <div>
            <h3 style={{ fontSize: '20px', fontWeight: '700', marginBottom: '8px' }}>
              Features
            </h3>
            <ul style={{ marginLeft: '20px', lineHeight: '1.8' }}>
              <li>Create and manage market assets (Stocks, Crypto, Commodities)</li>
              <li>Execute buy and sell trades</li>
              <li>Track portfolio performance</li>
              <li>View transaction history (last 30 days)</li>
              <li>Real-time price updates</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
