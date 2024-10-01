import React from 'react';

const SessionExpired = () => {
    return (
        <div className="text-center mt-5">
            <h1>Session Expired</h1>
            <p>Your session has expired. Please login again to continue.</p>
            <a 
                href="/login" 
                className="btn" 
                style={{ 
                    backgroundColor: '#5c23a6', 
                    color: 'white', 
                    borderRadius: '5px', 
                    padding: '10px 20px',
                    textDecoration: 'none'
                }}
            >
                Login
            </a>
        </div>
    );
};

export default SessionExpired;
