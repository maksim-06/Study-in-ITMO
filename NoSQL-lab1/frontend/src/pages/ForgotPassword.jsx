import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './ForgotPassword.css';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [newPassword, setNewPassword] = useState('');

    const [isCodeSent, setIsCodeSent] = useState(false);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleRequestCode = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setMessage('');

        try {
            await api.post('/users/forgot-password', { email });
            setIsCodeSent(true);
            setMessage('A one-time code has been successfully sent to your email. It is valid for 5 minutes.');
        } catch (err) {
            setError(err.response?.data?.message || 'User with this email not found.');
        } finally {
            setLoading(false);
        }
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setMessage('');

        try {
            await api.post('/users/reset-password', { email, code, newPassword });
            setMessage('Password successfully changed! You will now be redirected to the login page.');

            setTimeout(() => {
                navigate('/login');
            }, 3000);
        } catch (err) {
            setError(err.response?.data?.message || 'Invalid code or its lifetime in NoSQL has expired.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2 className="auth-title">Reset Password</h2>

                {message && <div className="success-alert">{message}</div>}
                {error && <div className="error-alert">{error}</div>}

                {!isCodeSent ? (
                    <form onSubmit={handleRequestCode}>
                        <div className="input-group">
                            <label className="auth-label">Enter your Email:</label>
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
                        <button type="submit" className="auth-button" disabled={loading}>
                            {loading ? 'Sending...' : 'Get Confirmation Code'}
                        </button>
                    </form>
                ) : (
                    <form onSubmit={handleResetPassword}>
                        <div className="input-group">
                            <label className="auth-label">Code from email (4 digits):</label>
                            <input
                                type="text"
                                required
                                maxLength="4"
                                value={code}
                                onChange={(e) => setCode(e.target.value)}
                                className="auth-input"
                                placeholder="0000"
                                disabled={loading}
                            />
                        </div>
                        <div className="input-group">
                            <label className="auth-label">Create a new password:</label>
                            <input
                                type="password"
                                required
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                className="auth-input"
                                placeholder="••••••••"
                                disabled={loading}
                            />
                        </div>
                        <button type="submit" className="auth-button" disabled={loading}>
                            {loading ? 'Saving...' : 'Reset Password'}
                        </button>
                    </form>
                )}

                <div className="auth-footer">
                    <span className="auth-link" onClick={() => navigate('/login')}>Back to Login</span>
                </div>
            </div>
        </div>
    );
};

export default ForgotPassword;
