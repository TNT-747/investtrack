import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, TrendingUp, Wallet, BarChart3 } from 'lucide-react';

const Layout = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Navigation Bar */}
      <nav style={{
        background: 'white',
        boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
        padding: '16px 0'
      }}>
        <div className="container" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '32px' }}>
            <Link to="/dashboard" style={{ textDecoration: 'none' }}>
              <h1 style={{ 
                fontSize: '24px', 
                fontWeight: '700', 
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                margin: 0
              }}>
                InvestTrack
              </h1>
            </Link>
            
            <div style={{ display: 'flex', gap: '8px' }}>
              <Link 
                to="/dashboard" 
                style={{ textDecoration: 'none' }}
              >
                <button className={`btn ${isActive('/dashboard') ? 'btn-primary' : 'btn-secondary'}`}>
                  <BarChart3 size={16} style={{ marginRight: '8px', display: 'inline' }} />
                  Dashboard
                </button>
              </Link>
              <Link 
                to="/market" 
                style={{ textDecoration: 'none' }}
              >
                <button className={`btn ${isActive('/market') ? 'btn-primary' : 'btn-secondary'}`}>
                  <TrendingUp size={16} style={{ marginRight: '8px', display: 'inline' }} />
                  Market
                </button>
              </Link>
              <Link 
                to="/portfolio" 
                style={{ textDecoration: 'none' }}
              >
                <button className={`btn ${isActive('/portfolio') ? 'btn-primary' : 'btn-secondary'}`}>
                  <Wallet size={16} style={{ marginRight: '8px', display: 'inline' }} />
                  Portfolio
                </button>
              </Link>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <span style={{ color: '#6b7280', fontSize: '14px' }}>
              Welcome, <strong>{user?.username}</strong>
            </span>
            <button className="btn btn-secondary" onClick={handleLogout}>
              <LogOut size={16} style={{ marginRight: '8px', display: 'inline' }} />
              Logout
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main style={{ flex: 1, paddingTop: '24px', paddingBottom: '24px' }}>
        {children}
      </main>

      {/* Footer */}
      <footer style={{
        background: 'white',
        borderTop: '1px solid #e5e7eb',
        padding: '16px 0',
        textAlign: 'center',
        color: '#6b7280',
        fontSize: '14px'
      }}>
        <div className="container">
          InvestTrack © 2025 - Portfolio Management System
        </div>
      </footer>
    </div>
  );
};

export default Layout;
