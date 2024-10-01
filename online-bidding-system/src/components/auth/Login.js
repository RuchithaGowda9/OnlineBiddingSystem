import React, { useState } from 'react';  
import { login } from '../../services/AuthService'; 
import biddingImage from '../../resources/images/login-image.jpg'; 
import { useLocation, useNavigate } from 'react-router-dom'; 
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import '../../resources/css/Login.css';

const Login = () => {
    const [showPassword, setShowPassword] = useState(false); 
    const [errorMessage, setErrorMessage] = useState('');
    const location = useLocation(); 
    const navigate = useNavigate(); 
    const message = location.state?.message;
    const successMessage = location.state?.successMessage; 

    const initialValues = {
        email: '',
        password: ''
    };

    const validationSchema = Yup.object().shape({
        email: Yup.string()
            .email('Invalid email format')
            .required('Email is required'),
        password: Yup.string()
            .required('Password is required')
    });

    const handleSubmit = async (values, { setSubmitting }) => {
        setErrorMessage(''); 
        try {
            const response = await login(values);
            navigate(response); 
        } catch (error) {
            setErrorMessage('Invalid username or password.'); 
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="container-fluid mt-0" style={{ height: '100vh', padding: 0 }}>
            <div className="row no-gutters" style={{ height: '100%' }}>
                <div className="col-md-6" style={{ 
                    backgroundImage: `url(${biddingImage})`, 
                    backgroundSize: 'cover', 
                    backgroundPosition: 'center',
                    height: '100vh' 
                }}></div>
                <div className="col-md-6 d-flex align-items-center justify-content-center flex-column">
                    {message && (
                        <div className="alert alert-danger w-100 text-center" style={{ width: '400px' }} role="alert">
                            {message}
                        </div>
                    )}
                    {successMessage && (
                        <div className="alert alert-success w-100 text-center" style={{ width: '400px' }} role="alert">
                            {successMessage}
                        </div>
                    )}
                    {errorMessage && (
                        <div className="alert alert-danger w-100 text-center" style={{ width: '400px' }} role="alert">
                            {errorMessage}
                        </div>
                    )}
                    <div className="card" style={{ 
                        backgroundColor: 'rgba(92, 35, 166, 0.9)', 
                        color: 'white', 
                        width: '400px', 
                        padding: '20px',
                        borderRadius: '10px', 
                        boxShadow: '0 4px 20px rgba(0, 0, 0, 0.5)' 
                    }}>
                        <div className="card-body">
                            <h2 className="card-title">Login</h2>
                            <Formik
                                initialValues={initialValues}
                                validationSchema={validationSchema}
                                onSubmit={handleSubmit}
                            >
                                {({ isSubmitting }) => (
                                    <Form>
                                        <div className="mb-3">
                                            <label htmlFor="email" className="form-label">Email *</label>
                                            <Field 
                                                type="email" 
                                                className="form-control custom-input" 
                                                name="email" 
                                                id="email" 
                                            />
                                            <ErrorMessage name="email" component="div" className="text-danger" />
                                        </div>
                                        <div className="mb-3">
                                            <label htmlFor="password" className="form-label">Password *</label>
                                            <Field 
                                                type={showPassword ? 'text' : 'password'} 
                                                className="form-control custom-input" 
                                                name="password" 
                                                id="password" 
                                            />
                                            <ErrorMessage name="password" component="div" className="text-danger" />
                                            <div className="form-check">
                                                <input 
                                                    type="checkbox" 
                                                    className="form-check-input" 
                                                    id="showPassword" 
                                                    checked={showPassword} 
                                                    onChange={() => setShowPassword(!showPassword)} 
                                                />
                                                <label className="form-check-label" htmlFor="showPassword">Show Password</label>
                                            </div>
                                        </div>
                                        <button type="submit" className="btn btn-light" disabled={isSubmitting}>Login</button>
                                    </Form>
                                )}
                            </Formik>
                            <p className="mt-2" style={{ color: 'white' }}>* All fields are mandatory</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
