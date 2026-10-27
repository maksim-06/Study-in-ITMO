import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import api from '../services/api';
import './Orders.css';

const Orders = () => {
    const [role] = useState(sessionStorage.getItem('role'));
    const [activeTab, setActiveTab] = useState('offers');
    const [orders, setOrders] = useState([]);
    const [offersList, setOffersList] = useState([]);
    const [loadTime, setLoadTime] = useState(0);
    const [dataSource, setDataSource] = useState('');

    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        if (!sessionStorage.getItem('token')) {
            navigate('/login');
            return;
        }
        fetchOrders();
        fetchOffers();
    }, [activeTab, role]);

    const fetchOffers = async () => {
        try {
            const response = await api.get('/offers');
            setOffersList(response.data.offers);
            setLoadTime(response.data.executionTimeMs);
            setDataSource(response.data.dataSource);
        } catch (err) {
            console.error("Failed to load offers:", err);
            setError(err.response?.data?.message || 'Failed to load fitness offers.');
        }
    };

    const fetchOrders = async () => {
        try {
            const endpoint = role === 'ROLE_MODERATOR' ? '/orders/all' : '/orders/user';
            const response = await api.get(endpoint);
            setOrders(response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to load orders.');
        }
    };

    const handleCreateOrder = async (offer) => {
        try {
            await api.post('/orders/create', {
                title: offer.title,
                price: offer.price
            });
            alert('Order successfully created!');
            fetchOrders();
        } catch (err) {
            alert(err.response?.data?.message || 'Failed to create order.');
        }
    };

    const handleApproveOrder = async (orderId) => {
        try {
            await api.patch(`/orders/change/${orderId}?status=COMPLETED`);
            fetchOrders();
        } catch (err) {
            alert(err.response?.data?.message || 'Failed to approve order.');
        }
    };

    const handleLogout = () => {
        sessionStorage.clear();
        navigate('/login');
    };

    return (
        <div className="dashboard-container">
            <div className="dashboard-header">
                <h1>{role === 'ROLE_MODERATOR' ? 'Moderator Panel (Cached via Etcd)' : 'Fitness Club Dashboard'}</h1>
                <button onClick={handleLogout} className="logout-btn">Logout</button>
            </div>

            {error && <div className="error-alert">{error}</div>}

            {role === 'ROLE_MODERATOR' ? (
                <div className="admin-panel">
                    <h2>All Client Applications</h2>
                    <table className="admin-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Price</th>
                            <th>Client Email</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {orders.map(order => (
                            <tr key={order.orderId}>
                                <td>{order.orderId}</td>
                                <td>{order.title}</td>
                                <td>{order.price} RUB</td>
                                <td>{order.contactEmail}</td>
                                <td>
                    <span className={`status-badge ${order.status === 'NEW' ? 'status-new' : 'status-approved'}`}>
                      {order.status}
                    </span>
                                </td>
                                <td>
                                    {order.status === 'NEW' && (
                                        <button onClick={() => handleApproveOrder(order.orderId)}
                                                className="action-btn">
                                            Approve
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="user-panel">
                    <div className="tabs-container">
                        <button
                            className={`tab-btn ${activeTab === 'offers' ? 'active' : ''}`}
                            onClick={() => setActiveTab('offers')}
                        >
                            Available Offers
                        </button>
                        <button
                            className={`tab-btn ${activeTab === 'my-orders' ? 'active' : ''}`}
                            onClick={() => setActiveTab('my-orders')}
                        >
                            My Applications
                        </button>
                    </div>

                    {activeTab === 'offers' && dataSource && (
                        <div style={{
                            background: '#e0f7fa',
                            padding: '12px',
                            borderRadius: '6px',
                            marginBottom: '20px',
                            color: '#006064',
                            fontWeight: 'bold',
                            border: '1px solid #b2ebf2',
                            display: 'flex',
                            gap: '15px',
                            justifyContent: 'center'
                        }}>
                            <span>Время загрузки: <span style={{color: '#ff5722'}}>{loadTime} мс</span></span>
                            <span>|</span>
                            <span>Источник: <span style={{color: '#004d40'}}>{dataSource}</span></span>
                        </div>
                    )}

                    {activeTab === 'offers' ? (
                        <div className="grid-container">
                            {offersList.map(offer => (
                                <div key={offer.id} className="product-card">
                                    <h3>{offer.title}</h3>
                                    <p className="product-price">{offer.price} RUB</p>
                                    <button onClick={() => handleCreateOrder(offer)} className="order-btn">
                                        Order Now
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <table className="admin-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Price</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            {orders.map(order => (
                                <tr key={order.orderId}>
                                    <td>{order.orderId}</td>
                                    <td>{order.title}</td>
                                    <td>{order.price} RUB</td>
                                    <td>
                      <span className={`status-badge ${order.status === 'NEW' ? 'status-new' : 'status-approved'}`}>
                        {order.status}
                      </span>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}
        </div>
    );
};

export default Orders;
