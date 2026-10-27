import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './ForgotPassword.css';

const Register = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await api.post('/users/register', { name, email, password });
            const { token, role } = response.data;

            sessionStorage.setItem('token', token);
            sessionStorage.setItem('role', role);

            navigate('/orders');
        } catch (err) {
            const serverMessage = err.response?.data?.message || err.response?.data || err.message;
            setError(`Error: ${serverMessage}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2 className="auth-title">Sign Up</h2>

                {error && <div className="error-alert">{error}</div>}

                <form onSubmit={handleRegister}>
                    <div className="input-group">
                        <label className="auth-label">Full Name:</label>
                        <input
                            type="text"
                            required
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="auth-input"
                            placeholder="Ivan Ivanov"
                            disabled={loading}
                        />
                    </div>

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
                        {loading ? 'Creating account...' : 'Register'}
                    </button>
                </form>

                <div className="auth-footer">
          <span style={{ fontSize: '14px', color: '#64748b' }}>
            Already have an account?{' '}
              <span className="auth-link" onClick={() => navigate('/login')}>
              Sign In
            </span>
          </span>
                </div>
            </div>
        </div>
    );
};

export default Register;
