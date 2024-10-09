import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../resources/images/logo.png';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const CustomerNavbar = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true });
            localStorage.removeItem('user'); // Clear user data from local storage
            navigate('/login'); // Redirect to login page
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    const handleLogin = async () => {
        // You can create a login form or handle login in a different way
        const userData = {
            email: 'user@example.com', // Replace with actual data from a form
            password: 'password123' // Replace with actual data from a form
        };
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', userData, { withCredentials: true });
            if (response.status === 200) {
                // Store user data if needed
                localStorage.setItem('user', JSON.stringify(response.data)); // Adjust this based on your API response
                navigate('/customer/dashboard'); // Redirect to customer dashboard
            }
        } catch (error) {
            console.error('Login failed:', error);
        }
    };

    return (
        <nav className="navbar navbar-expand-lg" style={{ backgroundColor: '#5c23a6', height: '60px' }}>
            <div className="container d-flex align-items-center">
                <Link className="navbar-brand" to="/customer/dashboard" style={{ marginRight: '80px' }}>
                    <img src={logo} alt="Online Bidding System Logo" style={{ height: '200px' }} />
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item dropdown">
                            <button className="nav-link dropdown-toggle" id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Profile
                            </button>
                            <ul className="dropdown-menu" aria-labelledby="profileDropdown" style={{ backgroundColor: '#5c23a6' }}>
                                <li><Link className="dropdown-item" to="/customer/buyers-profile">Buyer's Profile</Link></li>
                                <li><Link className="dropdown-item" to="/customer/sellers-profile">Seller's Profile</Link></li>
                            </ul>
                        </li>
                        <li className="nav-item">
                            <button className="nav-link" onClick={handleLogout} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer' }}>Logout</button>
                        </li>
                    </ul>
                </div>
            </div>

            <style jsx>{`
                .nav-link, .dropdown-item {
                    transition: background-color 0.3s, box-shadow 0.3s;
                    border-radius: 5px;
                }
                .nav-link:hover, .dropdown-item:hover {
                    background-color: rgba(255, 255, 255, 0.2);
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
                    color: white;
                }
                .dropdown-item {
                    color: white;
                }
            `}</style>
        </nav>
    );
};

export default CustomerNavbar;
