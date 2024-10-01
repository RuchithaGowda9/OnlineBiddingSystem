import React from 'react'; 
import { Link } from 'react-router-dom';
import logo from '../../resources/images/logo.png'; // Adjusted path

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg" style={{ backgroundColor: '#5c23a6', height: '60px' }}>
            <div className="container d-flex align-items-center">
                <Link className="navbar-brand" to="/" style={{ marginRight: '80px' }}>
                    <img src={logo} alt="Online Bidding System Logo" style={{ height: '200px' }} /> {/* Adjust logo height */}
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <Link className="nav-link" to="/login">Login</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/register">Register</Link>
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

export default Navbar;
