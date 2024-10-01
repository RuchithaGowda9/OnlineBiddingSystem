import React, { useEffect, useState } from 'react'; 
import { Tabs, Tab, Button, Form, Alert } from 'react-bootstrap';
import axios from 'axios';
import '../../resources/css/BuyerProfile.css';

const BuyerProfile = () => {
    const [key, setKey] = useState('personalDetails');
    const [profile, setProfile] = useState(null);
    const [walletBalance, setWalletBalance] = useState(null);
    const [loading, setLoading] = useState(true);
    const [rechargeAmount, setRechargeAmount] = useState('');
    const [message, setMessage] = useState({ text: '', type: '' });
    const API_URL = 'http://localhost:8080/api/auth/user-info'; 
    const WALLET_URL = 'http://localhost:8080/api/auth/wallet/balance';
    const RECHARGE_URL = 'http://localhost:8080/api/auth/wallet/recharge';

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setProfile(response.data);
                const balanceResponse = await axios.get(WALLET_URL, { withCredentials: true });
                setWalletBalance(balanceResponse.data);
            } catch (error) {
                console.error("Error fetching user info:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchUserInfo();
    }, [API_URL]);

    const handleRechargeWallet = async () => {
        try {
            const amount = parseFloat(rechargeAmount);
            if (isNaN(amount) || amount <= 0) {
                setMessage({ text: 'Please enter a valid amount to recharge.', type: 'error' });
                return;
            }
    
            await axios.post(RECHARGE_URL, { amount: amount }, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            setMessage({ text: 'Wallet recharged successfully!', type: 'success' });
            const balanceResponse = await axios.get(WALLET_URL, { withCredentials: true });
            setWalletBalance(balanceResponse.data);
            setRechargeAmount('');
        } catch (error) {
            console.error("Error recharging wallet:", error);
            setMessage({ text: 'Failed to recharge wallet: ' + (error.response?.data || error.message), type: 'error' });
        }
    };

    if (loading) {
        return <p>Loading user information...</p>;
    }

    if (!profile) {
        return <p>No profile data found.</p>;
    }

    const { userInfo, user } = profile;

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
                        <p>Your order history will be displayed here.</p>
                    </div>
                </Tab>
                <Tab eventKey="wallet" title="Wallet">
                    <div>
                        <p style={{ color: '#5c23a6' }}>
                            <i className="fas fa-wallet me-2"></i>
                            Your wallet balance: Rs {walletBalance !== null ? walletBalance : "Loading..."}
                        </p>
                        <Form style={{ maxWidth: '400px' }}>
                            <Form.Group controlId="rechargeAmount">
                                <Form.Label style={{ color: '#5c23a6' }}>
                                    <i className="fas fa-wallet me-2"></i>
                                    Recharge Amount
                                </Form.Label>
                                <Form.Control 
                                    type="number" 
                                    placeholder="Enter amount" 
                                    value={rechargeAmount} 
                                    onChange={(e) => setRechargeAmount(e.target.value)} 
                                />
                            </Form.Group>
                            <Button 
                                variant="primary" 
                                style={{ backgroundColor: '#5c23a6', borderColor: '#5c23a6' }} 
                                onClick={handleRechargeWallet}
                            >
                                Recharge Wallet
                            </Button>
                        </Form>
                    </div>
                </Tab>
                <Tab eventKey="myAddresses" title="My Addresses">
                    <div>
                        <p>Your saved addresses will be displayed here.</p>
                    </div>
                </Tab>
            </Tabs>
        </div>
    );
};

export default BuyerProfile;
