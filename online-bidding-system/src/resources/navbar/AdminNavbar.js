import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logo from '../../resources/images/logo.png'; // Adjusted path
import axios from 'axios';

const AdminNavbar = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/api/auth/logout', {}, { withCredentials: true });
            // Clear any user data from local storage or context
            localStorage.removeItem('user'); // Adjust this line based on how you're storing user data
            navigate('/login'); // Redirect to login page after logout
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    return (
        <nav className="navbar navbar-expand-lg" style={{ backgroundColor: '#5c23a6', height: '60px' }}>
            <div className="container d-flex align-items-center">
                <Link className="navbar-brand" to="/admin/dashboard" style={{ marginRight: '80px' }}>
                    <img src={logo} alt="Admin Dashboard Logo" style={{ height: '200px' }} />
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbarNav" aria-controls="adminNavbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="adminNavbarNav">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/admin/register" style={{ background: 'none', border: 'none', color: 'white' }}>Add Admin</Link>
                        </li>
                        <li className="nav-item dropdown">
                            <Link className="nav-link dropdown-toggle" to="#" id="manageCategoryDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false" style={{ background: 'none', border: 'none', color: 'white' }}>
                                Manage Categories
                            </Link>
                            <ul className="dropdown-menu" aria-labelledby="manageCategoryDropdown" style={{ backgroundColor: '#5c23a6' }}>
                                <li>
                                    <Link className="dropdown-item" to="/admin/add-category" style={{ color: 'white' }}>Add Category</Link>
                                </li>
                                <li>
                                    <Link className="dropdown-item" to="/admin/view-categories" style={{ color: 'white' }}>View All Categories</Link>
                                </li>
                            </ul>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/admin/all-orders" style={{ background: 'none', border: 'none', color: 'white' }}>All Orders</Link>
                        </li>
                        {/* <li className="nav-item">
                            <Link className="nav-link" to="/admin/all-delivery-persons" style={{ background: 'none', border: 'none', color: 'white' }}>Delivery Persons</Link>
                        </li> */}
                        <li className="nav-item">
                            <Link className="nav-link" to="/admin/all-products" style={{ background: 'none', border: 'none', color: 'white' }}>View All Products</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/admin/profile" style={{ background: 'none', border: 'none', color: 'white' }}>Profile</Link>
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
                .dropdown-item:hover {
                    background-color: rgba(255, 255, 255, 0.2); /* Light background on hover for dropdown items */
                    color: white; /* Maintain text color on hover */
                }
            `}</style>
        </nav>
    );
};

export default AdminNavbar;
