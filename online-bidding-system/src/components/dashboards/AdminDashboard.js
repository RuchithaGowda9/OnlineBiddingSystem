import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import bannerImage from '../../resources/images/banner2.jpeg'; // Adjust path as necessary

const AdminDashboard = () => {
    const [profile, setProfile] = useState(null);
    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const navigate = useNavigate(); // Initialize navigate for redirection

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axios.get(API_URL, { withCredentials: true });
                setProfile(response.data);
            } catch (error) {
                console.error("Error fetching user info:", error);
                // Redirect to the session expired page if user is not found
                if (error.response && error.response.status === 401) {
                    navigate('/session-expired'); // Redirect to session expired page
                }
            }
        };

        fetchUserInfo();
    }, [API_URL, navigate]); // Include navigate in dependencies

    // If profile is not loaded yet, you can show a loading message
    if (!profile) {
        return <p>Loading profile information...</p>;
    }
console.log(profile);
    return (
        <div className="text-center mt-5">
            <h1>Welcome, {profile.userInfo.firstName}!</h1> {/* Display the user's first name */}
            <img src={bannerImage} alt="Admin Dashboard Banner" style={{ width: '100%', maxHeight: '400px', objectFit: 'cover' }} />
            {/* Additional admin dashboard content can go here */}
        </div>
    );
};

export default AdminDashboard;
