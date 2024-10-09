import React, { useEffect, useState } from 'react';  
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom'; // Import useNavigate
import { Card, Button, Row, Col, Form } from 'react-bootstrap';

const ProductDetail = () => {
    const { productId } = useParams();
    const navigate = useNavigate(); 
    const [product, setProduct] = useState(null);
    const [walletBalance, setWalletBalance] = useState(0);
    const [bidAmount, setBidAmount] = useState('');
    const [message, setMessage] = useState('');
    const [showMessage, setShowMessage] = useState(false);
    const [bids, setBids] = useState([]);
    const [bidderId, setBidderId] = useState(null);
    const [addresses, setAddresses] = useState([]); 
    const [selectedAddress, setSelectedAddress] = useState(''); 
    
    const API_URL = `http://localhost:8080/api/auth/products/${productId}`;
    const WALLET_URL = 'http://localhost:8080/api/auth/wallet/balance';
    const USER_INFO_URL = 'http://localhost:8080/api/auth/user-info';
    const ADDRESS_URL = `http://localhost:8080/api/auth/addresses/user`; // Fetch user addresses

    const fetchBids = async (productId) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/auth/bids/product/${productId}`);
            setBids(response.data);
        } catch (error) {
            console.error("Error fetching bids:", error);
        }
    };

    useEffect(() => {
        const fetchProductDetail = async () => {
            try {
                const response = await axios.get(API_URL);
                setProduct(response.data);
                console.log(response.data);
                await fetchBids(response.data.productId); 
            } catch (error) {
                console.error("Error fetching product detail:", error);
            }
        };

        const fetchWalletBalance = async () => {
            try {
                const response = await axios.get(WALLET_URL, { withCredentials: true });
                setWalletBalance(response.data);
            } catch (error) {
                console.error("Error fetching wallet balance:", error);
            }
        };

        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(USER_INFO_URL, { withCredentials: true });
                if (response.data.user && response.data.user.id) {
                    setBidderId(response.data.user.id);
                    await fetchUserAddresses(response.data.user.id);
                }
            } catch (error) {
                console.error("Error fetching user info:", error);
            }
        };

        const fetchUserAddresses = async (userId) => {
            try {
                const response = await axios.get(`${ADDRESS_URL}/${userId}`, { withCredentials: true });
                setAddresses(response.data);
                if (response.data.length > 0) {
                    setSelectedAddress(response.data[0]); // Set the first address as default
                }
            } catch (error) {
                console.error("Error fetching addresses:", error);
            }
        };

        fetchProductDetail();
        fetchWalletBalance();
        fetchUserInfo();
    }, [API_URL, WALLET_URL, USER_INFO_URL, ADDRESS_URL]);

    const isSeller = bidderId === product?.sellerId;

    const handleBid = async () => {
        const bidValue = parseFloat(bidAmount);
        console.log("product.sellerid = "+product.sellerId)
        console.log("bidderid = "+bidderId)
        if (bidderId === product.sellerId) {
            setMessage("You cannot bid on your own product.");
            setShowMessage(true);
            setTimeout(() => {
                setShowMessage(false);
                setMessage('');
            }, 3000);
            return; 
        }

        if (bidValue > product.askingPrice && bidValue < 100000) {
            if (walletBalance >= bidValue) {
                if (selectedAddress) { // Check if an address is selected
                    try {
                        await axios.post(`http://localhost:8080/api/auth/bids/${productId}`, { 
                            bidPrice: bidValue,
                            bidderId: { userId: bidderId },
                            address: selectedAddress // Send the selected address with the bid
                        }, { withCredentials: true });
                        setMessage("Bid placed successfully!");
                        setBidAmount('');
                        await fetchBids(productId);
                    } catch (error) {
                        setMessage("Error placing bid.");
                    }
                } else {
                    setMessage("Please add your address before placing a bid.");
                }
            } else {
                setMessage("Insufficient wallet balance to place this bid.");
            }
        } else {
            setMessage("Bid amount must be greater than the product price and less than 100,000.");
        }

        setShowMessage(true);
        setTimeout(() => {
            setShowMessage(false);
            setMessage('');
        }, 3000);
    };

    const userBid = bids.find(bid => bid.bidderId.userId === bidderId);
    const getHighestBidDetails = () => {
        if (bids.length === 0) return { highestBid: 0 };

        const highestBid = bids.reduce((max, bid) => (bid.bidPrice > max.bidPrice ? bid : max), bids[0]);
        return { highestBid: highestBid.bidPrice };
    };

    const { highestBid } = getHighestBidDetails();

    return (
        <div className="mt-5">
            {product ? (
                <Row className="g-0">
                    <Col md={6} className="d-flex justify-content-center align-items-center">
                        <img
                            src={`http://localhost:8080/api/auth/products/image/${product.productId}`}
                            alt={product.name}
                            className="img-fluid"
                            style={{ width: '400px', height: '300px', objectFit: 'cover' }}
                        />
                    </Col>
                    <Col md={6}>
                        <Card.Body>
                            <Card.Title>{product.name}</Card.Title>
                            <Card.Text>
                                <strong>Price:</strong> <span style={{ fontSize: '1.5em', fontWeight: 'bold', color: 'green' }}>Rs {product.askingPrice}</span>
                                <br />
                                <strong>Description:</strong> <span>{product.description}</span>
                                <br />
                                <strong>Auction Ends:</strong> {new Date(product.endTime).toLocaleString()}
                            </Card.Text>

                            {userBid ? (
                                <div>
                                    <p>Your Bid: <strong>Rs {userBid.bidPrice}</strong></p>
                                    <p>Bid Time: {new Date(userBid.bidTime).toLocaleString()}</p>
                                </div>
                            ) : walletBalance >= product.askingPrice ? (
                                <>
                                    {isSeller ? (
                                        <div className="alert alert-warning">
                                            You cannot bid on your own product.
                                        </div>
                                    ) : addresses.length > 0 ? (
                                        <>
                                            <Form.Group className="mb-3">
                                                <Form.Label>Enter Your Bid Amount</Form.Label>
                                                <Form.Control 
                                                    type="number" 
                                                    value={bidAmount} 
                                                    onChange={(e) => setBidAmount(e.target.value)} 
                                                    placeholder="Enter bid amount" 
                                                />
                                            </Form.Group>
                                            <Button 
                                                variant="primary" 
                                                onClick={handleBid}
                                            >
                                                Place Your Bid
                                            </Button>
                                        </>
                                    ) : (
                                        <div className="alert alert-warning">
                                            You need to add your address before placing a bid.
                                            <Button variant="link" onClick={() => navigate('/customer/buyers-profile')}>
                                                Go to Profile
                                            </Button>
                                        </div>
                                    )}
                                </>
                            ) : (
                                <div className="alert alert-warning">
                                    Insufficient funds in your wallet.
                                    <Button variant="link" onClick={() => navigate('/customer/buyers-profile')}>
                                        Go to Profile
                                    </Button>
                                </div>
                            )}
                            {showMessage && <div className="alert alert-warning mt-3">{message}</div>}
                        </Card.Body>
                    </Col>
                    <Col md={3}>
                        <Card>
                            <Card.Body>
                                <Card.Title>Bids</Card.Title>
                                {bids.length > 0 ? (
                                    bids.map((bid) => (
                                        <div key={bid.id}>
                                            <strong>{bid.bidderId.userId === bidderId ? "You" : "Anonymous User"}</strong>: Rs {bid.bidPrice}
                                        </div>
                                    ))
                                ) : (
                                    <div>No bids placed yet.</div>
                                )}
                                <div style={{ color: 'red', fontWeight: 'bold' }}>
                                    {highestBid > 0 && `Highest Bid: Rs ${highestBid}`}
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            ) : (
                <p>Loading product details...</p>
            )}
        </div>
    );
};

export default ProductDetail;
