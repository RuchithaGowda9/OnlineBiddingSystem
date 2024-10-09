import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../resources/images/logo.png'; // Adjusted path
import axios from 'axios';

const DeliveryPersonNavbar = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true });
            localStorage.removeItem('user'); // Clear user data from local storage
            navigate('/login'); // Redirect to login page after logout
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    return (
        <nav className="navbar navbar-expand-lg" style={{ backgroundColor: '#5c23a6', height: '60px' }}>
            <div className="container d-flex align-items-center">
                <Link className="navbar-brand" to="/delivery/dashboard" style={{ marginRight: '80px' }}>
                    <img src={logo} alt="Delivery Person Dashboard Logo" style={{ height: '200px' }} />
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#deliveryPersonNavbarNav" aria-controls="deliveryPersonNavbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="deliveryPersonNavbarNav">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/delivery/my-orders" style={{ background: 'none', border: 'none', color: 'white' }}>My Orders</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/delivery/profile" style={{ background: 'none', border: 'none', color: 'white' }}>Profile</Link>
                        </li>
                        <li className="nav-item">
                            <button className="nav-link" onClick={handleLogout} style={{ background: 'none', border: 'none', color: 'white', cursor: 'pointer' }}>Logout</button>
                        </li>
                    </ul>
                </div>
            </div>

            {/* Custom styles for hover effects */}
            <style jsx>{`
                .nav-link {
                    transition: background-color 0.3s, box-shadow 0.3s;
                    border-radius: 5px; /* Optional: rounded corners */
                }
                .nav-link:hover {
                    background-color: rgba(255, 255, 255, 0.2); /* Light background on hover */
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3); /* Shadow effect on hover */
                    color: white; /* Maintain text color */
                }
            `}</style>
        </nav>
    );
};

export default DeliveryPersonNavbar;
