import React, { useEffect, useState } from 'react'; 
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Card, Button, Form, Dropdown } from 'react-bootstrap';
import '../../resources/css/CustomerDashboard.css'; 

const CustomerDashboard = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategoryId, setSelectedCategoryId] = useState(null);
    const [activeCategories, setActiveCategories] = useState([]);
    const [countdowns, setCountdowns] = useState({}); 
    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const PRODUCTS_URL = 'http://localhost:8080/api/auth/products/';
    const CATEGORIES_URL = 'http://localhost:8080/api/categories/active';
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setUserInfo(response.data);
            } catch (error) {
                console.error("Error fetching user info:", error);
                if (error.response && error.response.status === 401) {
                    navigate('/session-expired');
                }
            }
        };

        const fetchAllProducts = async () => {
            try {
                const response = await axios.get(PRODUCTS_URL, { withCredentials: true });
                setProducts(response.data);
                setFilteredProducts(response.data); // Initialize filtered products
            } catch (error) {
                console.error("Error fetching products:", error);
            }
        };

        const fetchActiveCategories = async () => {
            try {
                const response = await axios.get(CATEGORIES_URL);
                setActiveCategories(response.data);
            } catch (error) {
                console.error("Error fetching active categories:", error);
            }
        };

        fetchUserInfo();
        fetchAllProducts();
        fetchActiveCategories();
    }, [API_URL, PRODUCTS_URL, CATEGORIES_URL, navigate]);

    useEffect(() => {
        // Filter products based on search term and selected category
        const results = products.filter(product => {
            const matchesSearchTerm = product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                       product.description.toLowerCase().includes(searchTerm.toLowerCase());
            const matchesCategory = selectedCategoryId ? product.categoryId === selectedCategoryId : true;
            return matchesSearchTerm && matchesCategory;
        });
        setFilteredProducts(results);
    }, [searchTerm, products, selectedCategoryId]);

    useEffect(() => {
        // Initialize countdowns
        const newCountdowns = {};
        products.forEach(product => {
            const endTime = new Date(product.endTime).getTime();
            newCountdowns[product.productId] = endTime - Date.now();
        });
        setCountdowns(newCountdowns);

        const countdownInterval = setInterval(() => {
            setCountdowns(prevCountdowns => {
                const updatedCountdowns = { ...prevCountdowns };
                Object.keys(updatedCountdowns).forEach(productId => {
                    updatedCountdowns[productId] = updatedCountdowns[productId] - 1000; // Decrease by 1 second
                });
                return updatedCountdowns;
            });
        }, 1000);

        return () => clearInterval(countdownInterval); // Cleanup interval on unmount
    }, [products]);

    const handleCategorySelect = (categoryId) => {
        setSelectedCategoryId(categoryId); // Set the selected category
    };

    const handleBidNowClick = (productId) => {
        navigate(`/customer/productdetail/${productId}`); 
    };


    const formatTime = (time) => {
        const seconds = Math.floor((time / 1000) % 60);
        const minutes = Math.floor((time / (1000 * 60)) % 60);
        const hours = Math.floor((time / (1000 * 60 * 60)) % 24);
        const days = Math.floor(time / (1000 * 60 * 60 * 24));

        return `${days}d ${hours}h ${minutes}m ${seconds}s`;
    };

    return (
        <div className="text-center mt-5">
            
            {userInfo ? (
                <h1>Welcome, {userInfo.userInfo.firstName}!</h1>
            ) : (
                <p>Loading user information...</p>
            )}
            <h2>Available Products</h2>
            <Form className="mb-4">
                <Form.Group controlId="searchBar" className="d-flex align-items-center">
                    <Form.Control
                        type="text"
                        placeholder="Search for products..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="me-2"
                        style={{ 
                            borderRadius: '5px', 
                            backgroundColor: 'white', 
                            color: 'black', 
                            borderColor: '#5c23a6' 
                        }}
                    />
                    <Dropdown>
                        <Dropdown.Toggle variant="secondary" id="category-dropdown" style={{ 
                            backgroundColor: '#5c23a6', 
                            color: 'white', 
                            borderColor: '#5c23a6' 
                        }}>
                            Shop by Category
                        </Dropdown.Toggle>
                        <Dropdown.Menu style={{ backgroundColor: '#5c23a6' }}>
                            {activeCategories.length > 0 ? (
                                activeCategories.map(category => (
                                    <Dropdown.Item
                                        key={category.categoryId}
                                        onClick={() => handleCategorySelect(category.categoryId)}
                                        style={{ color: 'white' }}
                                    >
                                        {category.categoryName}
                                    </Dropdown.Item>
                                ))
                            ) : (
                                <Dropdown.Item disabled style={{ color: 'white' }}>No categories available</Dropdown.Item>
                            )}
                        </Dropdown.Menu>
                    </Dropdown>
                </Form.Group>
            </Form>
            <div className="row">
                {filteredProducts.length > 0 ? (
                    filteredProducts.map(product => {
                        const isAuctionEnded = countdowns[product.productId] <= 0;

                        return (
                            <div className="col-md-4 mb-3" key={product.productId}>
                                <Card>
                                    <Card.Img 
                                        variant="top" 
                                        src={`http://localhost:8080/api/auth/products/image/${product.productId}`} 
                                        className="card-img" // Apply CSS class for uniform size
                                    />
                                    <Card.Body>
                                        <Card.Title>{product.name}</Card.Title>
                                        <Card.Text>
                                            Price: Rs {product.askingPrice}<br />
                                            Description: {product.description}<br />
                                            Ends in: {isAuctionEnded ? "Auction ended" : formatTime(countdowns[product.productId])}
                                        </Card.Text>
                                        <Button className="custom-button" onClick={() => handleBidNowClick(product.productId)} disabled={isAuctionEnded}>
                                        {isAuctionEnded ? "Bid Ended" : "Bid Now"}
                                        </Button>

                                    </Card.Body>
                                </Card>
                            </div>
                        );
                    })
                ) : (
                    <p>No products available at the moment.</p>
                )}
            </div>
        </div>
    );
};

export default CustomerDashboard;
