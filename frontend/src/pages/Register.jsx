import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import '../styles/global.css';

const Register = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await api.post('/auth/register', { email, password });
            alert("Registration Successful! Please Login.");
            navigate('/');
        } catch (err) {
            alert("Registration Failed. This email might already be in use.");
        }
    };

    return (
        <div className="auth-wrapper">
            <div className="auth-box">
                <h2>Create Account</h2>
                <form onSubmit={handleRegister}>
                    <input 
                        type="email" 
                        placeholder="Email Address" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        required 
                    />
                    <input 
                        type="password" 
                        placeholder="Password" 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                        required 
                    />
                    <button type="submit">Register</button>
                </form>
                <div className="auth-footer">
                    Already have an account? <Link to="/">Login</Link>
                </div>
            </div>
        </div>
    );
};

export default Register;