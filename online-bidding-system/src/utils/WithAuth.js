import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const WithAuth = (WrappedComponent) => {
    const AuthHOC = (props) => {
        const navigate = useNavigate();

        useEffect(() => {
            const userSession = sessionStorage.getItem('user');
            if (!userSession) {
                navigate('/login', { state: { message: 'User not found / Session has expired. Please log in.' } });
            }
        }, [navigate]);

        return <WrappedComponent {...props} />;
    };

    return AuthHOC;
};

export default WithAuth;
