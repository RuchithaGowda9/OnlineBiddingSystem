import React from 'react'; 
import { Link } from 'react-router-dom'; 
import 'bootstrap/dist/css/bootstrap.min.css'; 

const Footer = () => {
    return (
        <footer className="bg-light text-center py-4 mt-5">
            <div className="container">
                <p className="mb-1" style={{ color: '#5c23a6' }}>© 2024 Online Bidding System. All rights reserved.</p>
                
                {/* Social Media Links */}
                <div className="social-links mb-2">
                    <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className="me-3" style={{ color: '#5c23a6' }}>
                        <i className="fab fa-facebook"></i>
                    </a>
                    <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className="me-3" style={{ color: '#5c23a6' }}>
                        <i className="fab fa-twitter"></i>
                    </a>
                    <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" style={{ color: '#5c23a6' }}>
                        <i className="fab fa-instagram"></i>
                    </a>
                </div>

                {/* Footer Links */}
                <div className="mb-3">
                    <Link to="/about" className="me-3" style={{ color: '#5c23a6' }}>About Us</Link>
                    <Link to="/contact" className="me-3" style={{ color: '#5c23a6' }}>Contact</Link>
                    <Link to="/privacy-policy" className="me-3" style={{ color: '#5c23a6' }}>Privacy Policy</Link>
                    <Link to="/terms-of-use" style={{ color: '#5c23a6' }}>Terms of Use</Link>
                </div>

                {/* Contact Information */}
                <div className="mb-3">
                    <p style={{ color: '#5c23a6' }}>
                        Contact Us: <a href="mailto:info@onlinebiddingsystem.com" style={{ color: '#5c23a6' }}>info@onlinebiddingsystem.com</a>
                    </p>
                    <p style={{ color: '#5c23a6' }}>Phone: +1 (555) 123-4567</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
