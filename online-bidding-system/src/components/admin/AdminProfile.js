import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';

const AdminProfile = () => {
    const [profile, setProfile] = useState(null);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const API_URL = 'http://localhost:8080/api/auth/user-info';
    const CHANGE_PASSWORD_URL = 'http://localhost:8080/api/auth/change-password';
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

    if (!profile) {
        return <p>Loading user information...</p>;
    }

    const { userInfo, user } = profile;

    const openModal = () => {
        setModalIsOpen(true);
        setMessage('');
        setError('');
        setCurrentPassword('');
        setNewPassword('');
        setConfirmPassword('');
    };

    const closeModal = () => setModalIsOpen(false);

    const handleChangePassword = async (e) => {
        e.preventDefault();
        if (newPassword !== confirmPassword) {
            setError("New passwords do not match!");
            return;
        }

        try {
            await axios.post(CHANGE_PASSWORD_URL, {
                currentPassword,
                newPassword
            }, { withCredentials: true });
            setMessage("Password changed successfully!");
            setError('');
            closeModal();
        } catch (error) {
            console.error("Error changing password:", error);
            setError("Failed to change password. Please check your current password.");
            setMessage('');
        }
    };

    const handlePaste = (e) => {
        e.preventDefault();
    };

    return (
        <div className="container mt-5 d-flex justify-content-center">
            <div className="card" style={{ 
                backgroundColor: 'white', 
                color: 'black', 
                width: '400px', 
                padding: '20px',
                borderRadius: '10px', 
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.5)' 
            }}>
                <h2 style={{ color: '#5c23a6' }}>Profile Information</h2>
                <form>
                    <div className="mb-3">
                        <label className="form-label">First Name</label>
                        <input type="text" className="form-control" value={userInfo.firstName} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Last Name</label>
                        <input type="text" className="form-control" value={userInfo.lastName} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Email</label>
                        <input type="email" className="form-control" value={user.email} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Phone Number</label>
                        <input type="text" className="form-control" value={userInfo.phoneNumber} readOnly />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Password</label>
                        <input type="password" className="form-control" value={user.password} readOnly />
                    </div>
                    <button type="button" className="btn" style={{ backgroundColor: '#5c23a6', color: 'white' }} onClick={openModal}>Change Password</button>
                </form>

               
                {message && <div className="alert alert-success mt-3">{message}</div>}
            </div>

            
            <Modal isOpen={modalIsOpen} onRequestClose={closeModal} style={{
                content: {
                    maxWidth: '400px',
                    margin: 'auto',
                    backgroundColor: 'white',
                    color: 'black',
                    borderRadius: '10px',
                    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.5)'
                }
            }}>
                <h2 style={{ color: '#5c23a6' }}>Change Password</h2>
                <form onSubmit={handleChangePassword}>
                    <div className="mb-3">
                        <label className="form-label">Current Password</label>
                        <input
                            type="password"
                            className="form-control"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            onPaste={handlePaste}  
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">New Password</label>
                        <input
                            type="password"
                            className="form-control"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            onPaste={handlePaste}  // Prevent paste action
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Confirm New Password</label>
                        <input
                            type="password"
                            className="form-control"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            onPaste={handlePaste}  // Prevent paste action
                            required
                        />
                    </div>
                    {error && <div className="alert alert-danger mt-3">{error}</div>}
                    <button type="submit" className="btn" style={{ backgroundColor: '#5c23a6', color: 'white' }}>Change Password</button>
                    <button type="button" className="btn btn-secondary" onClick={closeModal}>Cancel</button>
                </form>
            </Modal>
        </div>
    );
};

export default AdminProfile;
