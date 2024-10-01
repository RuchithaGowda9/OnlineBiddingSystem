import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import logo from '../../resources/images/logo.png';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const CustomerNavbar = () => {
    const [activeCategories, setActiveCategories] = useState([]);

    useEffect(() => {
        const fetchActiveCategories = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/categories/active');
                setActiveCategories(response.data);
            } catch (error) {
                console.error("Error fetching active categories:", error);
            }
        };

        fetchActiveCategories();
    }, []);

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
                            <button className="nav-link dropdown-toggle" id="shopByCategoryDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Shop by Category
                            </button>
                            <ul className="dropdown-menu" aria-labelledby="shopByCategoryDropdown" style={{ backgroundColor: '#5c23a6' }}>
                                {activeCategories.length > 0 ? (
                                    activeCategories.map(category => (
                                        <li key={category.categoryId}>
                                            <Link className="dropdown-item" to={`/category/${category.categoryName.toLowerCase()}`}>
                                                {category.categoryName}
                                            </Link>
                                        </li>
                                    ))
                                ) : (
                                    <li><span className="dropdown-item">No categories available</span></li>
                                )}
                            </ul>
                        </li>
                        <li className="nav-item dropdown">
                            <button className="nav-link dropdown-toggle" id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                Profile
                            </button>
                            <ul className="dropdown-menu" aria-labelledby="profileDropdown" style={{ backgroundColor: '#5c23a6' }}>
                                <li><Link className="dropdown-item" to="/customer/buyers-profile">Buyer's Profile</Link></li>
                                <li><Link className="dropdown-item" to="/sellers-profile">Seller's Profile</Link></li>
                            </ul>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/logout" style={{ color: 'white' }}>Logout</Link>
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
