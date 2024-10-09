import React, { useEffect, useState } from 'react'; 
import { Alert } from 'react-bootstrap';
import axios from 'axios';

const DeliveryProfile = () => {
    const [profile, setProfile] = useState({});
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ text: '', type: '' });

    const API_URL = 'http://localhost:8080/api/auth/user-info';

    const fetchProfile = async () => {
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
            console.error("Error fetching delivery person profile:", error);
            setMessage({ text: 'Failed to fetch profile.', type: 'danger' });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
    }, []);

    if (loading) {
        return <p>Loading delivery person information...</p>;
    }

    if (!profile.firstName) {
        return <p>No profile data found. Please check the console for more information.</p>;
    }

    return (
        <div className="container mt-5">
            <h2 style={{ color: '#5c23a6' }}>Delivery Person Profile</h2>
            {message.text && (
                <Alert variant={message.type === 'success' ? 'success' : 'danger'}>
                    {message.text}
                </Alert>
            )}
            <div>
                <h5>Your Profile:</h5>
                <form style={{ maxWidth: '400px' }}>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: '#5c23a6' }}>
                            <i className="fas fa-user me-2"></i>
                            First Name
                        </label>
                        <input type="text" className="form-control" value={profile.firstName} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: '#5c23a6' }}>
                            <i className="fas fa-user me-2"></i>
                            Last Name
                        </label>
                        <input type="text" className="form-control" value={profile.lastName} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: '#5c23a6' }}>
                            <i className="fas fa-envelope me-2"></i>
                            Email
                        </label>
                        <input type="email" className="form-control" value={profile.email} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label" style={{ color: '#5c23a6' }}>
                            <i className="fas fa-phone me-2"></i>
                            Phone Number
                        </label>
                        <input type="text" className="form-control" value={profile.phone} readOnly />
                    </div>
                </form>
            </div>
        </div>
    );
};

export default DeliveryProfile;
