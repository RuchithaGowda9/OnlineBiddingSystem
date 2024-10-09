import React, { useEffect, useState } from 'react'; 
import { Tabs, Tab, Button, Form, Alert, Modal, Card } from 'react-bootstrap';
import axios from 'axios';
import '../../resources/css/SellerProfile.css';

const SellerProfile = () => {
    const [key, setKey] = useState('productList');
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ text: '', type: '' });
    const [showProductModal, setShowProductModal] = useState(false);
    const [showBidsModal, setShowBidsModal] = useState(false);
    const [showOrderModal, setShowOrderModal] = useState(false); 
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [newProduct, setNewProduct] = useState({ name: '', askingPrice: '', endTime: '', description: '', categoryId: '', image: null });
    const [selectedProductBids, setSelectedProductBids] = useState([]);
    const [selectedStatus, setSelectedStatus] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [statuses, setStatuses] = useState(['active', 'deactivated', 'sold', 'unsold']);
    const [selectedDeliveryAgency, setSelectedDeliveryAgency] = useState('');
    const [deliveryAgencies, setDeliveryAgencies] = useState([]);
    const [deliveryPersons, setDeliveryPersons] = useState([]); // State to hold delivery persons
    const [selectedDeliveryPerson, setSelectedDeliveryPerson] = useState('');
    const [highestBidders, setHighestBidders] = useState({}); 
    const [selectedProductId, setSelectedProductId] = useState(null);
    const [isOrderCreated, setIsOrderCreated] = useState(false); 
    const [orderStatus, setOrderStatus] = useState({});
    const [validationErrors, setValidationErrors] = useState({});

    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const CATEGORIES_URL = 'http://localhost:8080/api/categories/active';
    const AGENCY_URL = 'http://localhost:8080/api/agencies'; // Endpoint to fetch delivery agencies
    const ORDERS_URL = 'http://localhost:8080/api/createorder'; // Endpoint to create orders

    const fetchProducts = async (sellerId) => {
        if (!sellerId) return;
        try {
            const response = await axios.get(`http://localhost:8080/api/auth/products/product/${sellerId}`);
            setProducts(response.data);
            setFilteredProducts(response.data);
            console.log("Fetched products:", response.data);
            response.data.forEach(product => fetchBidsForProduct(product.productId)); 
        } catch (error) {
            console.error("Error fetching products:", error);
        }
    };

    const fetchCategories = async () => {
        try {
            const response = await axios.get(CATEGORIES_URL);
            setCategories(response.data);
        } catch (error) {
            console.error("Error fetching categories:", error);
        }
    };

    const fetchBidsForProduct = async (productId) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/auth/bids/product/${productId}`);
            setSelectedProductBids(response.data);
            determineHighestBidder(productId, response.data); // Pass productId along with bids
        } catch (error) {
            console.error("Error fetching bids:", error);
        }
    };
    
    const determineHighestBidder = (productId, bids) => { // Accept productId as the first parameter
        if (bids && bids.length > 0) { // Check if bids is defined and has length
            const highestBid = bids.reduce((max, bid) => (bid.bidPrice > max.bidPrice ? bid : max), bids[0]);
            setHighestBidders((prev) => ({ ...prev, [productId]: highestBid })); // Store by productId
        } else {
            setHighestBidders((prev) => ({ ...prev, [productId]: null })); // Reset for this productId
        }
    };
    
    const fetchDeliveryAgencies = async () => {
        try {
            const response = await axios.get(AGENCY_URL);
            console.log("Delivery agencies response:", response.data);
            
            // Transform the array of names into an array of objects
            const agencies = response.data.map((name, index) => ({
                id: index + 1, // Assuming you can derive an ID (or you could generate unique IDs)
                name: name
            }));
            
            setDeliveryAgencies(agencies);
        } catch (error) {
            console.error("Error fetching delivery agencies:", error);
        }
    };
    
    const fetchDeliveryPersons = async (agencyName) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/delivery-agents?agencyName=${agencyName}`);
            setDeliveryPersons(response.data);
        } catch (error) {
            console.error("Error fetching delivery persons:", error);
        }
        console.log(deliveryPersons)
    };

    const handleDeliveryAgencyChange = (e) => {
        const selectedAgencyName = e.target.value;
        console.log(selectedAgencyName)
        setSelectedDeliveryAgency(selectedAgencyName);
        fetchDeliveryPersons(selectedAgencyName); // Fetch delivery persons based on agency name
        setSelectedDeliveryPerson(''); // Reset selected delivery person
    };

    const createOrder = async () => {
        if (!selectedDeliveryAgency || !selectedDeliveryPerson) {
            setMessage({ text: 'Please select a delivery agency and a delivery person.', type: 'danger' });
            return;
        }
    
        const highestBidder = highestBidders[selectedProductId];
    
        if (!highestBidder) {
            setMessage({ text: 'No highest bidder found for this product.', type: 'danger' });
            return;
        }
    
        const orderData = {
            product: { productId: highestBidder.product.productId },
            buyerId: highestBidder.bidderId.userId, 
            bidAmount:highestBidder.bidPrice,
            sellerId: profile.user.id, 
            deliveryPersonId: Number(selectedDeliveryPerson), 
            orderStatus: 'Confirmed' 
        };
        console.log("Order Data:", orderData);
        try {
            await axios.post(ORDERS_URL, orderData, { withCredentials: true });
            setMessage({ text: 'Order created successfully!', type: 'success' });
            setIsOrderCreated(true);
            setOrderStatus(prev => ({ ...prev, [selectedProductId]: 'sold' }));
            setShowOrderModal(false);
            fetchBidsForProduct(selectedProductId);
        } catch (error) {
            console.error("Error creating order:", error);
            setMessage({ text: 'Failed to create order: ' + (error.response?.data || error.message), type: 'error' });
        }
    };
    

    const isAuctionEnded = (endTime) => {
        return new Date(endTime) < new Date(); // Compare endTime with the current date
    };
    
    useEffect(() => {
        const fetchSellerInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setProfile(response.data);
                console.log("Seller Profile:", response.data);
                await fetchCategories();
            } catch (error) {
                console.error("Error fetching seller info:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchSellerInfo();
        fetchProducts();
    }, [API_URL]);

    useEffect(() => {
        if (profile) {
            const sellerId = profile.user.id;
            console.log("Fetching products for sellerId:", sellerId);
            fetchProducts(sellerId);
            fetchDeliveryAgencies();
        }
    }, [profile]);

    const validateProduct = () => {
        const errors = {};
        if (newProduct.name.length > 50) {
            errors.name = "Product name must not exceed 50 characters.";
        }
        if (newProduct.description.length > 50) {
            errors.description = "Description must not exceed 50 characters.";
        }
        if (newProduct.askingPrice < 1 || newProduct.askingPrice > 100000) {
            errors.askingPrice = "Asking price must be between 1 and 100,000.";
        }
        if (new Date(newProduct.endTime) <= new Date()) {
            errors.endTime = "End time must be in the future.";
        }
        setValidationErrors(errors);
        return Object.keys(errors).length === 0; // Returns true if no errors
    };

    const handleAddProduct = async () => {
        if (!validateProduct()) return;
        const formData = new FormData();
        formData.append('name', newProduct.name);
        formData.append('askingPrice', newProduct.askingPrice);
        formData.append('endTime', newProduct.endTime);
        formData.append('description', newProduct.description);
        formData.append('categoryId', newProduct.categoryId);
        formData.append('sellerId', profile.user.id);
        formData.append('image', newProduct.image);

        const PRODUCTS_URL = `http://localhost:8080/api/auth/products/addproducts/${profile.user.id}`;

        try {
            await axios.post(PRODUCTS_URL, formData, { withCredentials: true, headers: { 'Content-Type': 'multipart/form-data' } });
            setMessage({ text: 'Product added successfully!', type: 'success' });
            resetProductForm();
        } catch (error) {
            console.error("Error adding product:", error);
            setMessage({ text: 'Failed to add product: ' + (error.response?.data || error.message), type: 'error' });
        }
    };

    const resetProductForm = () => {
        setNewProduct({ name: '', askingPrice: '', endTime: '', description: '', categoryId: '', image: null });
        setShowProductModal(false);
    };

    const handleViewBids = (productId) => {
        fetchBidsForProduct(productId);
        setShowBidsModal(true);
    };

    const handleStatusChange = (e) => {
        setSelectedStatus(e.target.value);
        filterProducts(e.target.value, searchTerm);
    };

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
        filterProducts(selectedStatus, e.target.value);
    };

    const filterProducts = (status, name) => {
        const filtered = products.filter(product => {
            const matchesStatus = status === '' || product.status === status;
            const matchesName = product.name.toLowerCase().includes(name.toLowerCase());
            return matchesStatus && matchesName;
        });
        setFilteredProducts(filtered);
    };

    if (loading) {
        return <p>Loading seller information...</p>;
    }

    if (!profile) {
        return <p>No profile data found.</p>;
    }

    return (
        <div className="container mt-5">
            <h2 style={{ color: '#5c23a6' }}>Seller's Profile</h2>
            {message.text && (
                <Alert variant={message.type === 'success' ? 'success' : 'danger'}>
                    {message.text}
                </Alert>
            )}
            <Tabs activeKey={key} onSelect={(k) => setKey(k)} id="seller-profile-tabs" className="mb-3 custom-tabs">
                <Tab eventKey="productList" title="My Products">
                    <div>
                        <h5>Your Products:</h5>

                        {/* Search Bar and Filter by Status */}
                        <div className="mb-3 d-flex align-items-center">
                            <Form.Group controlId="productSearch" className="me-2">
                                <Form.Control
                                    type="text"
                                    placeholder="Search by name"
                                    value={searchTerm}
                                    onChange={handleSearchChange}
                                />
                            </Form.Group>
                            <Form.Group controlId="productStatus" className="me-2">
                                <Form.Control as="select" value={selectedStatus} onChange={handleStatusChange}>
                                    <option value="">All</option>
                                    <option value="active">Active</option>
                                    <option value="deactivated">Deactivated</option>
                                    <option value="sold">Sold</option>
                                    <option value="unsold">Unsold</option>
                                </Form.Control>
                            </Form.Group>
                        </div>

                        {filteredProducts.length > 0 ? (
                            <div className="row mt-3">
                                {filteredProducts.map(product => (
                                    <div className="col-md-4 mb-3" key={product.productId}>
                                        <Card>
                                            <Card.Img variant="top" src={`http://localhost:8080/api/auth/products/image/${product.productId}`}  className='fixed-image'/>
                                            <Card.Body>
                                                <Card.Title>{product.name}</Card.Title>
                                                <Card.Text>
                                                    Price: Rs {product.askingPrice}<br />
                                                    Description: {product.description}<br />
                                                    End Time: {new Date(product.endTime).toLocaleString()}
                                                </Card.Text>
                                                <Button variant="primary" onClick={() => handleViewBids(product.productId)}>
                                                    View Bids
                                                </Button>
                                                <Button
                                                    variant={orderStatus[product.productId] === 'sold' ? "secondary" : "success"}
                                                    onClick={() => { 
                                                    setSelectedProductId(product.productId);
                                                    setSelectedProductBids([]); 
                                                    setShowOrderModal(true); 
                                                    }}
                                                    disabled={!highestBidders[product.productId] || !isAuctionEnded(product.endTime) || product.status !== 'active' || orderStatus[product.productId] === 'sold'}
                                                    >
                                                    {orderStatus[product.productId] === 'sold' || product.status === 'sold' ? "Sold" : "Create Order"}
                                                </Button>
                                            </Card.Body>
                                        </Card>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p>No products available for the selected criteria.</p>
                        )}
                        <Button
                            variant="primary"
                            style={{ backgroundColor: '#5c23a6', borderColor: '#5c23a6' }}
                            onClick={() => setShowProductModal(true)}
                        >
                            Add Product
                        </Button>
                    </div>
                </Tab>
            </Tabs>

            {/* Add Product Modal */}
            <Modal show={showProductModal} onHide={resetProductForm}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Product</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="productName">
                            <Form.Label>Product Name *</Form.Label>
                            <Form.Control
                                type="text"
                                value={newProduct.name}
                                onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
                                isInvalid={!!validationErrors.name}
                            />
                            <Form.Control.Feedback type="invalid">
                                {validationErrors.name}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="productCategory">
                            <Form.Label>Category *</Form.Label>
                            <Form.Control
                                as="select"
                                value={newProduct.categoryId}
                                onChange={(e) => setNewProduct({ ...newProduct, categoryId: e.target.value })}
                            >
                                <option value="">Select a category</option>
                                {categories.map(category => (
                                    <option key={category.categoryId} value={category.categoryId}>
                                        {category.categoryName}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group controlId="productPrice">
                            <Form.Label>Asking Price *</Form.Label>
                            <Form.Control
                                type="number"
                                value={newProduct.askingPrice}
                                onChange={(e) => setNewProduct({ ...newProduct, askingPrice: e.target.value })}
                                isInvalid={!!validationErrors.askingPrice}
                            />
                            <Form.Control.Feedback type="invalid">
                                {validationErrors.askingPrice}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="productEndTime">
                            <Form.Label>End Time * (YYYY-MM-DDTHH:mm:ss)</Form.Label>
                            <Form.Control
                                type="datetime-local"
                                value={newProduct.endTime}
                                onChange={(e) => setNewProduct({ ...newProduct, endTime: e.target.value })}
                                isInvalid={!!validationErrors.endTime}
                            />
                            <Form.Control.Feedback type="invalid">
                                {validationErrors.endTime}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="productDescription">
                            <Form.Label>Description *</Form.Label>
                            <Form.Control
                                as="textarea"
                                value={newProduct.description}
                                onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
                                isInvalid={!!validationErrors.description}
                            />
                            <Form.Control.Feedback type="invalid">
                                {validationErrors.description}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="productImage">
                            <Form.Label>Image *</Form.Label>
                            <Form.Control
                                type="file"
                                accept="image/*"
                                onChange={(e) => setNewProduct({ ...newProduct, image: e.target.files[0] })}
                            />
                        </Form.Group>
                        <Button variant="secondary" onClick={resetProductForm}>
                            Cancel
                        </Button>
                        <Button variant="primary" onClick={handleAddProduct}>
                            Add Product
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>

            {/* Bids Modal */}
            <Modal show={showBidsModal} onHide={() => setShowBidsModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Bid Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedProductBids.length > 0 ? (
                        <div>
                            {selectedProductBids.map(bid => (
                                <div key={bid.id}>
                                    <strong>Bidder ID:</strong> {bid.bidderId.userId} <br />
                                    <strong>Bid Amount:</strong> Rs {bid.bidPrice} <br />
                                    <strong>Bid Time:</strong> {new Date(bid.bidTime).toLocaleString()} <br />
                                    <hr />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No bids placed for this product yet.</p>
                    )}
                </Modal.Body>
            </Modal>

            {/* Order Creation Modal */}
            <Modal show={showOrderModal} onHide={() => setShowOrderModal(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Create Order</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {highestBidders[selectedProductId] ? (
                    <>
                        <h5>Highest Bidder: {highestBidders[selectedProductId].bidderId.userId}</h5>
                        <h6>Bid Amount: Rs {highestBidders[selectedProductId].bidPrice}</h6>
                    </>
                ) : (
                    <p>No bids available</p>
                )}
                <Form.Group controlId="deliveryAgencySelect">
                    <Form.Label>Select Delivery Agency</Form.Label>
                    <Form.Control as="select" onChange={handleDeliveryAgencyChange}>
                        <option value="">Select an agency</option>
                            {deliveryAgencies.map(agency => (
                        <option key={agency.id} value={agency.name}>{agency.name}</option>
                        ))}
                    </Form.Control>

                </Form.Group>
                {selectedDeliveryAgency && (
                    <Form.Group controlId="deliveryPersonSelect">
                        <Form.Label>Select Delivery Person</Form.Label>
                        <Form.Control as="select" onChange={(e) => setSelectedDeliveryPerson(e.target.value)}>
                            console.log(e.target.value);
                            <option value="">Select a person</option>
                            {deliveryPersons.map(person => (
                                <option key={person.user.userId} value={person.user.userId}>{person.firstName} {person.lastName}</option>
                            ))}
                        </Form.Control>
                    </Form.Group>
                )}
                <Button variant="secondary" onClick={() => setShowOrderModal(false)}>
                    Cancel
                </Button>
                <Button 
                    variant="success" 
                    onClick={createOrder}>
                    Create Order
                </Button>
            </Modal.Body>
        </Modal>

        </div>
    );
};

export default SellerProfile;
