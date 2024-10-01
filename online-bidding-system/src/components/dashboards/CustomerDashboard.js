import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CustomerDashboard = () => {
    const [userInfo, setUserInfo] = useState(null);
    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setUserInfo(response.data);
            } catch (error) {
                console.error("Error fetching user info:", error);
                // Redirect to the session expired page if user is not found
                if (error.response && error.response.status === 401) {
                    navigate('/session-expired'); // Adjust this path as needed
                }
            }
        };

        fetchUserInfo();
    }, [API_URL, navigate]);

    return (
        <div className="text-center mt-5">
            <h1>Customer Dashboard</h1>
            {userInfo ? (
                <p>Welcome, {userInfo.userInfo.firstName}!</p>
            ) : (
                <p>Loading user information...</p>
            )}
            <p>View your bids and account details here.</p>
        </div>
    );
};

export default CustomerDashboard;
