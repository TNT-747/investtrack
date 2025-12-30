import React, { useState, useEffect } from 'react';
import { marketService } from '../services/api';
import { Plus, Edit2, Trash2, TrendingUp, DollarSign } from 'lucide-react';

const MarketPage = () => {
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingAsset, setEditingAsset] = useState(null);
  const [formData, setFormData] = useState({
    symbol: '',
    name: '',
    currentPrice: '',
    type: 'STOCK'
  });

  useEffect(() => {
    fetchAssets();
  }, []);

  const fetchAssets = async () => {
    try {
      setLoading(true);
      const data = await marketService.getAllAssets();
      setAssets(data);
      setError('');
    } catch (err) {
      setError('Failed to fetch assets');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingAsset) {
        await marketService.updateAsset(editingAsset.id, formData);
      } else {
        await marketService.createAsset(formData);
      }
      setShowModal(false);
      setEditingAsset(null);
      setFormData({ symbol: '', name: '', currentPrice: '', type: 'STOCK' });
      fetchAssets();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save asset');
    }
  };

  const handleEdit = (asset) => {
    setEditingAsset(asset);
    setFormData({
      symbol: asset.symbol,
      name: asset.name,
      currentPrice: asset.currentPrice,
      type: asset.type
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this asset?')) {
      try {
        await marketService.deleteAsset(id);
        fetchAssets();
      } catch (err) {
        setError('Failed to delete asset');
      }
    }
  };

  const handleUpdatePrice = async (id) => {
    const newPrice = prompt('Enter new price:');
    if (newPrice && !isNaN(newPrice)) {
      try {
        await marketService.updateAssetPrice(id, parseFloat(newPrice));
        fetchAssets();
      } catch (err) {
        setError('Failed to update price');
      }
    }
  };

  return (
    <div className="container">
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
          <div>
            <h2 style={{ fontSize: '24px', fontWeight: '700', color: '#111827', marginBottom: '4px' }}>
              Market Assets
            </h2>
            <p style={{ color: '#6b7280' }}>Manage financial assets and prices</p>
          </div>
          <button 
            className="btn btn-primary"
            onClick={() => {
              setEditingAsset(null);
              setFormData({ symbol: '', name: '', currentPrice: '', type: 'STOCK' });
              setShowModal(true);
            }}
          >
            <Plus size={16} style={{ marginRight: '8px', display: 'inline' }} />
            Add Asset
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {loading ? (
          <div className="loading">Loading assets...</div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="table">
              <thead>
                <tr>
                  <th>Symbol</th>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Current Price</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {assets.length === 0 ? (
                  <tr>
                    <td colSpan="5" style={{ textAlign: 'center', padding: '40px', color: '#6b7280' }}>
                      No assets found. Create your first asset!
                    </td>
                  </tr>
                ) : (
                  assets.map((asset) => (
                    <tr key={asset.id}>
                      <td><strong>{asset.symbol}</strong></td>
                      <td>{asset.name}</td>
                      <td>
                        <span className={`badge ${
                          asset.type === 'STOCK' ? 'badge-info' : 
                          asset.type === 'CRYPTO' ? 'badge-success' : 'badge-danger'
                        }`}>
                          {asset.type}
                        </span>
                      </td>
                      <td>
                        <strong style={{ color: '#10b981' }}>
                          ${asset.currentPrice.toFixed(2)}
                        </strong>
                      </td>
                      <td>
                        <div style={{ display: 'flex', gap: '8px' }}>
                          <button
                            className="btn btn-secondary"
                            style={{ padding: '6px 12px', fontSize: '12px' }}
                            onClick={() => handleUpdatePrice(asset.id)}
                            title="Update Price"
                          >
                            <DollarSign size={14} />
                          </button>
                          <button
                            className="btn btn-secondary"
                            style={{ padding: '6px 12px', fontSize: '12px' }}
                            onClick={() => handleEdit(asset)}
                            title="Edit"
                          >
                            <Edit2 size={14} />
                          </button>
                          <button
                            className="btn btn-danger"
                            style={{ padding: '6px 12px', fontSize: '12px' }}
                            onClick={() => handleDelete(asset.id)}
                            title="Delete"
                          >
                            <Trash2 size={14} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3 style={{ fontSize: '20px', fontWeight: '700', marginBottom: '20px' }}>
              {editingAsset ? 'Edit Asset' : 'Create New Asset'}
            </h3>
            <form onSubmit={handleSubmit}>
              <div className="input-group">
                <label>Symbol</label>
                <input
                  type="text"
                  value={formData.symbol}
                  onChange={(e) => setFormData({ ...formData, symbol: e.target.value.toUpperCase() })}
                  placeholder="e.g., AAPL"
                  required
                />
              </div>
              <div className="input-group">
                <label>Name</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g., Apple Inc."
                  required
                />
              </div>
              <div className="input-group">
                <label>Type</label>
                <select
                  value={formData.type}
                  onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                  required
                >
                  <option value="STOCK">Stock</option>
                  <option value="CRYPTO">Crypto</option>
                  <option value="COMMODITY">Commodity</option>
                </select>
              </div>
              <div className="input-group">
                <label>Current Price</label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.currentPrice}
                  onChange={(e) => setFormData({ ...formData, currentPrice: e.target.value })}
                  placeholder="0.00"
                  required
                />
              </div>
              <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                <button type="submit" className="btn btn-primary" style={{ flex: 1 }}>
                  {editingAsset ? 'Update' : 'Create'}
                </button>
                <button 
                  type="button" 
                  className="btn btn-secondary" 
                  style={{ flex: 1 }}
                  onClick={() => setShowModal(false)}
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

export default MarketPage;
