import React, { useEffect, useState } from 'react';
import { Tabs, Tab, Button, Form, Alert, Modal,Card, Table } from 'react-bootstrap';
import axios from 'axios';
import '../../resources/css/BuyerProfile.css';


const BuyerProfile = () => {
    const [key, setKey] = useState('personalDetails');
    const [profile, setProfile] = useState(null);
    const [walletBalance, setWalletBalance] = useState(null);
    const [loading, setLoading] = useState(true);
    const [rechargeAmount, setRechargeAmount] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('');
    const [upiId, setUpiId] = useState('');
    const [cardNumber, setCardNumber] = useState('');
    const [expiry, setExpiry] = useState('');
    const [cvv, setCvv] = useState('');
    const [message, setMessage] = useState({ text: '', type: '' });
    const [showRechargeModal, setShowRechargeModal] = useState(false);
    const [errors, setErrors] = useState({});  
    const [addresses, setAddresses] = useState([]);
    const [newAddress, setNewAddress] = useState({
        houseNo: '',
        street: '',
        apartment: '',
        city: '',
        state: '',
        pincode: ''
    });
    const [showAddressModal, setShowAddressModal] = useState(false);
    const [orders, setOrders] = useState([]);     

    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const WALLET_URL = 'http://localhost:8080/api/auth/wallet/balance';
    const RECHARGE_URL = 'http://localhost:8080/api/auth/wallet/recharge';
    const ADD_ADDRESS_URL = 'http://localhost:8080/api/auth/addresses/addaddress';
    const ADDRESSES_URL = 'http://localhost:8080/api/auth/addresses/user';
    const ORDER_HISTORY_URL = 'http://localhost:8080/api/orders';

    const fetchUserAddresses = async (userId) => {
        try {
            const response = await axios.get(`${ADDRESSES_URL}/${userId}`, { withCredentials: true });
            setAddresses(response.data);
        } catch (error) {
            console.error("Error fetching addresses:", error);
        }
    };

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setProfile(response.data);
                console.log("Fetched Profile:", response.data);
                const balanceResponse = await axios.get(WALLET_URL, { withCredentials: true });
                setWalletBalance(balanceResponse.data);
                await fetchOrderHistory(response.data.user.id);
                const userId = response.data.user.id;
                if (userId) {
                    await fetchUserAddresses(userId);
                }
            } catch (error) {
                console.error("Error fetching user info:", error);
            } finally {
                setLoading(false);
            }
        };     
        fetchUserInfo();
    }, [API_URL]);
    
    const fetchOrderHistory = async (userId) => {
        try {
            const response = await axios.get(`${ORDER_HISTORY_URL}`, { withCredentials: true });
            setOrders(response.data);
        } catch (error) {
            console.error("Error fetching order history:", error);
            setMessage({ text: 'Failed to fetch order history: ' + (error.response?.data || error.message), type: 'error' });
        }
    };

    
    const handleRechargeWallet = async () => {
        if (!validateInputs()) return;

        try {
            await axios.post(RECHARGE_URL, { amount: rechargeAmount }, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            setMessage({ text: 'Wallet recharged successfully!', type: 'success' });
            const balanceResponse = await axios.get(WALLET_URL, { withCredentials: true });
            setWalletBalance(balanceResponse.data);
            resetForm();
        } catch (error) {
            console.error("Error recharging wallet:", error);
            setMessage({ text: 'Failed to recharge wallet: ' + (error.response?.data || error.message), type: 'error' });
        }
    };

    const validateInputs = (field) => {
        const newErrors = { ...errors };
        let valid = true;

        if (field === 'rechargeAmount') {
            if (!rechargeAmount || isNaN(rechargeAmount) || rechargeAmount < 1 || rechargeAmount > 100000) {
                newErrors.rechargeAmount = 'Recharge amount must be between 1 and 100000.';
                valid = false;
            } else {
                delete newErrors.rechargeAmount; // Clear error if valid
            }
        }

        if (field === 'upiId' && paymentMethod === 'upi') {
            if (!/^[\w\.-]+@[\w\.-]+$/.test(upiId)) {
                newErrors.upiId = 'Invalid UPI ID format.';
                valid = false;
            } else {
                delete newErrors.upiId; // Clear error if valid
            }
        }

        if (field === 'cardNumber' && paymentMethod === 'card') {
            if (!/^\d{15}$/.test(cardNumber)) {
                newErrors.cardNumber = 'Invalid card number. It should be 16 digits.';
                valid = false;
            } else {
                delete newErrors.cardNumber; // Clear error if valid
            }
        }

        if (field === 'expiry' && paymentMethod === 'card') {
            // Regex to match MM/YY format correctly
            const expiryPattern = /^(0[1-9]|1[0-2])\/([0-9]{1})$/;
            
            if (!expiryPattern.test(expiry)) {
                newErrors.expiry = 'Invalid expiry date format. Use MM/YY.';
                valid = false;
            } else {
                const [month, year] = expiry.split('/').map(Number);
                const currentDate = new Date();
                const currentMonth = currentDate.getMonth() + 1; // Months are 0-based, so +1
                const currentYear = currentDate.getFullYear() % 100; // Get last two digits of the current year
                
                // Check if the expiry date is in the future
                // Note: year is a two-digit representation, so need to adjust for comparison
                const expiryYear = year + 2000; // Convert YY to YYYY for comparison
                
                if (expiryYear < currentYear || 
                    (expiryYear === currentYear && month < currentMonth)) {
                    newErrors.expiry = 'Expiry date must be in the future.';
                    valid = false;
                } else {
                    delete newErrors.expiry; // Clear error if valid
                }
            }
        }
        
        
        if (field === 'cvv' && paymentMethod === 'card') {
            if (!/^\d{2}$/.test(cvv)) {
                newErrors.cvv = 'Invalid CVV. It should be 3 digits.';
                valid = false;
            } else {
                delete newErrors.cvv; // Clear error if valid
            }
        }

        setErrors(newErrors);  // Update the error state
        return valid;
    };

    const resetForm = () => {
        setRechargeAmount('');
        setUpiId('');
        setCardNumber('');
        setExpiry('');
        setCvv('');
        setPaymentMethod('');
        setShowRechargeModal(false);
        setErrors({});  // Reset errors
    };

    const handlePaymentMethodChange = (e) => {
        setPaymentMethod(e.target.value);
        if (e.target.value === 'upi') {
            resetCardFields();
        } else {
            resetUpiField();
        }
    };

    const resetCardFields = () => {
        setCardNumber('');
        setExpiry('');
        setCvv('');
        setErrors(prev => ({ ...prev, cardNumber: '', expiry: '', cvv: '' }));  // Clear card errors
    };

    const resetUpiField = () => {
        setUpiId('');
        setErrors(prev => ({ ...prev, upiId: '' }));  // Clear UPI error
    };

    if (loading) {
        return <p>Loading user information...</p>;
    }

    if (!profile) {
        return <p>No profile data found.</p>;
    }

    const { userInfo, user } = profile;

    

    const validateFields = (field, value) => {
        const newErrors = { ...errors };
        if (field === 'houseNo') {
            if (!/^\d{1,4}$/.test(value)) {
                newErrors.houseNo = 'House No must be 1 to 4 digits long and only numbers.';
            } else {
                delete newErrors.houseNo;
            }
        }

        if (['street', 'apartment', 'city', 'state'].includes(field)) {
            if (value.length > 50) {
                newErrors[field] = `${field.charAt(0).toUpperCase() + field.slice(1)} must not exceed 50 characters.`;
            } else {
                delete newErrors[field];
            }
        }
        if (field === 'pincode') {
            if (!/^\d{6}$/.test(value)) {
                newErrors.pincode = 'Pincode must contain exactly 6 digits.';
            } else {
                delete newErrors.pincode;
            }
        }

        setErrors(newErrors);
    };

    const handleChange = (field) => (e) => {
        const value = e.target.value;
        setNewAddress({ ...newAddress, [field]: value });
        validateFields(field, value); // Call validation when input changes
    };

    



    const handleAddAddress = async () => {
    
        if (!profile || !profile.user.id) {
            console.log(profile.user.id);
            setMessage({ text: 'User ID is not available', type: 'error' });
            return;
        }
    
        try {
            const userId = profile.user.id; // Access the user ID directly
            console.log("Adding address for user ID:", userId);
            await axios.post(ADD_ADDRESS_URL, { ...newAddress, user:  profile.user.id  }, { withCredentials: true });
            setMessage({ text: 'Address added successfully!', type: 'success' });
            resetAddressForm();
            await fetchUserAddresses(profile.user.id);
        } catch (error) {
            console.error("Error adding address:", error);
            setMessage({ text: 'Failed to add address: ' + (error.response?.data || error.message), type: 'error' });
        }
    };
    
    
    
    const resetAddressForm = () => {
        setNewAddress({
            houseNo: '',
            street: '',
            apartment: '',
            city: '',
            state: '',
            pincode: ''
        });
        setShowAddressModal(false);
    };

    return (
        <div className="container mt-5">
            <h2 style={{ color: '#5c23a6' }}>Buyer's Profile</h2>
            {message.text && (
                <Alert variant={message.type === 'success' ? 'success' : 'danger'}>
                    {message.text}
                </Alert>
            )}
            <Tabs
                activeKey={key}
                onSelect={(k) => setKey(k)}
                id="buyer-profile-tabs"
                className="mb-3 custom-tabs"
            >
                <Tab eventKey="personalDetails" title="Personal Details">
                    <div>
                        <form style={{ maxWidth: '400px' }}>
                            <div className="mb-3">
                                <label className="form-label" style={{ color: '#5c23a6' }}>
                                    <i className="fas fa-user me-2"></i>
                                    First Name
                                </label>
                                <input type="text" className="form-control" value={userInfo.firstName} readOnly />
                            </div>
                            <div className="mb-3">
                                <label className="form-label" style={{ color: '#5c23a6' }}>
                                    <i className="fas fa-user me-2"></i>
                                    Last Name
                                </label>
                                <input type="text" className="form-control" value={userInfo.lastName} readOnly />
                            </div>
                            <div className="mb-3">
                                <label className="form-label" style={{ color: '#5c23a6' }}>
                                    <i className="fas fa-envelope me-2"></i>
                                    Email
                                </label>
                                <input type="email" className="form-control" value={user.email} readOnly />
                            </div>
                            <div className="mb-3">
                                <label className="form-label" style={{ color: '#5c23a6' }}>
                                    <i className="fas fa-phone me-2"></i>
                                    Phone Number
                                </label>
                                <input type="text" className="form-control" value={userInfo.phoneNumber} readOnly />
                            </div>
                        </form>
                    </div>
                </Tab>
                <Tab eventKey="orderHistory" title="View Order History">
                    <div>
                        <h5>Your Order History</h5>
                        {orders.length > 0 ? (
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Product Name</th>
                                        <th>Bid Amount</th>
                                        <th>Order Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {orders.map(order => (
                                        <tr key={order.orderId}>
                                            <td>{order.orderId}</td>
                                            <td>{order.product.productName}</td> {/* Adjust according to your product structure */}
                                            <td>{order.bidAmount}</td>
                                            <td>{order.orderStatus}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        ) : (
                            <p>You have no orders in your history.</p>
                        )}
                    </div>
                </Tab>
                <Tab eventKey="wallet" title="Wallet">
                    <div>
                        <p style={{ color: '#5c23a6' }}>
                            <i className="fas fa-wallet me-2"></i>
                            Your wallet balance: Rs {walletBalance !== null ? walletBalance : "Loading..."}
                        </p>
                        <Button 
                            variant="primary" 
                            style={{ backgroundColor: '#5c23a6', borderColor: '#5c23a6' }} 
                            onClick={() => setShowRechargeModal(true)}
                        >
                            Recharge Wallet
                        </Button>
                    </div>
                </Tab>
                <Tab eventKey="myAddresses" title="My Addresses">
                    <div>
                        <h5>Your saved addresses:</h5>
                        {addresses.length > 0 ? (
                            <div className="row">
                                {addresses.map(address => (
                                    <div className="col-md-4 mb-3" key={address.addressId}>
                                        <Card>
                                            <Card.Body>
                                                <Card.Title>
                                                    <i className="fas fa-map-marker-alt me-2"></i>
                                                    Address
                                                </Card.Title>
                                                <Card.Text>
                                                    {address.houseNo}, {address.street}, {address.apartment}, {address.city}, {address.state} - {address.pincode}
                                                </Card.Text>
                                            </Card.Body>
                                        </Card>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p>You currently have no saved addresses. Please add an address to get started.</p>
                        )}
                        <Button 
                            variant="primary" 
                            style={{ backgroundColor: '#5c23a6', borderColor: '#5c23a6' }} 
                            onClick={() => setShowAddressModal(true)}
                        >
                            Add Address
                        </Button>
                    </div>
                </Tab>


            </Tabs>

            {/* Recharge Modal */}
            <Modal show={showRechargeModal} onHide={() => setShowRechargeModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Recharge Wallet</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="rechargeAmount">
                            <Form.Label style={{ color: '#5c23a6' }}>
                                <i className="fas fa-wallet me-2"></i>
                                Recharge Amount (1-100000)
                            </Form.Label>
                            <Form.Control 
                                type="number" 
                                placeholder="Enter amount" 
                                value={rechargeAmount} 
                                onChange={(e) => {
                                    setRechargeAmount(e.target.value);
                                    validateInputs('rechargeAmount');
                                }} 
                            />
                            {errors.rechargeAmount && <div className="text-danger">{errors.rechargeAmount}</div>}
                        </Form.Group>
                        <Form.Group controlId="paymentMethod">
                            <Form.Label style={{ color: '#5c23a6' }}>Payment Method</Form.Label>
                            <Form.Control as="select" value={paymentMethod} onChange={(e) => {
                                handlePaymentMethodChange(e);
                                validateInputs();
                            }}>
                                <option value="">Select...</option>
                                <option value="upi">UPI</option>
                                <option value="card">Credit/Debit Card</option>
                            </Form.Control>
                        </Form.Group>

                        {paymentMethod === 'upi' && (
                            <Form.Group controlId="upiId">
                                <Form.Label>UPI ID</Form.Label>
                                <Form.Control 
                                    type="text" 
                                    value={upiId} 
                                    onChange={(e) => {
                                        setUpiId(e.target.value);
                                        validateInputs('upiId');
                                    }} 
                                />
                                {errors.upiId && <div className="text-danger">{errors.upiId}</div>}
                            </Form.Group>
                        )}

                        {paymentMethod === 'card' && (
                            <>
                                <Form.Group controlId="cardNumber">
                                    <Form.Label>Card Number</Form.Label>
                                    <Form.Control 
                                        type="text" 
                                        value={cardNumber} 
                                        onChange={(e) => {
                                            setCardNumber(e.target.value);
                                            validateInputs('cardNumber');
                                        }} 
                                    />
                                    {errors.cardNumber && <div className="text-danger">{errors.cardNumber}</div>}
                                </Form.Group>
                                <Form.Group controlId="expiry">
                                    <Form.Label>Expiry Date (MM/YY)</Form.Label>
                                    <Form.Control 
                                        type="text" 
                                        value={expiry} 
                                        onChange={(e) => {
                                            setExpiry(e.target.value);
                                            validateInputs('expiry');
                                        }} 
                                    />
                                    {errors.expiry && <div className="text-danger">{errors.expiry}</div>}
                                </Form.Group>
                                <Form.Group controlId="cvv">
                                    <Form.Label>CVV</Form.Label>
                                    <Form.Control 
                                        type="password" 
                                        value={cvv} 
                                        onChange={(e) => {
                                            setCvv(e.target.value);
                                            validateInputs('cvv');
                                        }} 
                                    />
                                    {errors.cvv && <div className="text-danger">{errors.cvv}</div>}
                                </Form.Group>
                            </>
                        )}

                        <Button variant="secondary" onClick={() => setShowRechargeModal(false)}>
                            Cancel
                        </Button>
                        <Button variant="primary" onClick={handleRechargeWallet}>
                            Recharge
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
            {/* Add Address Modal */}
            <Modal show={showAddressModal} onHide={() => setShowAddressModal(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Add New Address</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="houseNo">
                        <Form.Label>House No *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.houseNo}
                            onChange={handleChange('houseNo')} // Use the new handler
                            isInvalid={!!errors.houseNo}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.houseNo}
                        </Form.Control.Feedback>
                    </Form.Group>
                    
                    <Form.Group controlId="street">
                        <Form.Label>Street *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.street}
                            onChange={handleChange('street')} // Use the new handler
                            isInvalid={!!errors.street}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.street}
                        </Form.Control.Feedback>
                    </Form.Group>
                    
                    <Form.Group controlId="apartment">
                        <Form.Label>Apartment *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.apartment}
                            onChange={handleChange('apartment')} // Use the new handler
                            isInvalid={!!errors.apartment}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.apartment}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group controlId="city">
                        <Form.Label>City *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.city}
                            onChange={handleChange('city')} // Use the new handler
                            isInvalid={!!errors.city}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.city}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group controlId="state">
                        <Form.Label>State *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.state}
                            onChange={handleChange('state')} // Use the new handler
                            isInvalid={!!errors.state}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.state}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group controlId="pincode">
                        <Form.Label>Pincode *</Form.Label>
                        <Form.Control
                            type="text"
                            value={newAddress.pincode}
                            onChange={handleChange('pincode')} // Use the new handler
                            isInvalid={!!errors.pincode}
                        />
                        <Form.Control.Feedback type="invalid">
                            {errors.pincode}
                        </Form.Control.Feedback>
                    </Form.Group>

                    <Button variant="secondary" onClick={() => setShowAddressModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleAddAddress}>
                        Add Address
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>

        </div>
    );
};

export default BuyerProfile;
