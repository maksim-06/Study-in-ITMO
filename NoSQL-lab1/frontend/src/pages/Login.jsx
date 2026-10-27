import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './ForgotPassword.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await api.post('/users/login', { email, password });
            const { token, role } = response.data;

            sessionStorage.setItem('token', token);
            sessionStorage.setItem('role', role);

            navigate('/orders');
        } catch (err) {
            setError(err.response?.data?.message || err.response?.data || 'Invalid email or password.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2 className="auth-title">Sign In</h2>

                {error && <div className="error-alert">{error}</div>}

                <form onSubmit={handleLogin}>
                    <div className="input-group">
                        <label className="auth-label">Email Address:</label>
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="auth-input"
                            placeholder="example@yandex.ru"
                            disabled={loading}
                        />
                    </div>

                    <div className="input-group">
                        <label className="auth-label">Password:</label>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="auth-input"
                            placeholder="••••••••"
                            disabled={loading}
                        />
                    </div>

                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? 'Processing...' : 'Login'}
                    </button>
                </form>

                <div className="auth-footer">
          <span className="auth-link" onClick={() => navigate('/forgot-password')}>
            Forgot Password?
          </span>
                    <br /><br />
                    <span style={{ fontSize: '14px', color: '#64748b' }}>
            Don't have an account?{' '}
                        <span className="auth-link" onClick={() => navigate('/register')}>
              Sign Up
            </span>
          </span>
                </div>
            </div>
        </div>
    );
};

export default Login;
