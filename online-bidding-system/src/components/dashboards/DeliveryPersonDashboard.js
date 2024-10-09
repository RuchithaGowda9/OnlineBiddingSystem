import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import bannerImage from '../../resources/images/banner2.jpeg'; // Adjust path as necessary

const DeliveryPersonDashboard = () => {
    const [profile, setProfile] = useState(null);
    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                const user = response.data.user;
                const userInfo = response.data.userInfo;

                setProfile({
                    id: user.id,
                    email: user.email,
                    firstName: userInfo.firstName,
                    lastName: userInfo.lastName,
                    phone: userInfo.phoneNumber,
                });
            } catch (error) {
                console.error("Error fetching user info:", error);
                if (error.response && error.response.status === 401) {
                    navigate('/session-expired');
                }
            }
        };

        fetchUserInfo();
    }, [API_URL, navigate]);

    // If profile is not loaded yet, show a loading message
    if (!profile) {
        return <p>Loading profile information...</p>;
    }

    return (
        <div className="text-center mt-5">
            <h1>Welcome, {profile.firstName}!</h1> {/* Display the user's first name */}
            <img src={bannerImage} alt="Delivery Person Dashboard Banner" style={{ width: '100%', maxHeight: '400px', objectFit: 'cover' }} />
            <div className="container mt-4">
                {/* Additional delivery person dashboard content can go here */}
            </div>
        </div>
    );
};

export default DeliveryPersonDashboard;
