import React, { useState } from 'react';  
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { register } from '../../services/AuthService';
import { useNavigate } from 'react-router-dom'; 
import biddingImage from '../../resources/images/login-image.jpg';

const Register = ({ role }) => {
    const [showPassword, setShowPassword] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState(''); 
    const navigate = useNavigate(); 

    const formik = useFormik({
        initialValues: {
            firstName: '',
            lastName: '',
            phoneNumber: '',
            email: '',
            password: ''
        },
        validationSchema: Yup.object({
            firstName: Yup.string()
                .required('First Name is required')
                .min(3, 'First Name must be at least 3 characters')
                .max(50, 'First Name cannot exceed 50 characters'),
            lastName: Yup.string()
                .max(50, 'Last Name cannot exceed 50 characters'),
            phoneNumber: Yup.string()
                .required('Phone Number is required')
                .matches(/^[6-9]\d{9}$/, 'Please enter a valid Indian number')
                .max(10, 'Phone number can only contain 10 numbers'),
            email: Yup.string()
                .email('Invalid email format')
                .required('Email is required')
                .max(100, 'Email cannot exceed 100 characters')
                .test('domain', 'Email must be from bidmaster.com', value => {
                    if (role === 'ADMIN') {
                        return value && value.endsWith('@bidmaster.com');
                    }
                    return true; 
                }),
            password: Yup.string()
                .required('Password is required')
                .min(8, 'Password must be at least 8 characters')
                .max(100, 'Password cannot exceed 100 characters')
                .matches(/[a-z]/, 'Password must contain at least one lowercase letter')
                .matches(/[A-Z]/, 'Password must contain at least one uppercase letter')
                .matches(/[0-9]/, 'Password must contain at least one number')
                .matches(/[^a-zA-Z0-9]/, 'Password must contain at least one special character')
        }),
        onSubmit: async (values) => {
            const registrationData = {
                user: {
                    email: values.email,
                    password: values.password,
                },
                userInfo: {
                    firstName: values.firstName,
                    lastName: values.lastName,
                    phoneNumber: values.phoneNumber,
                    roleName: role || 'CUSTOMER'  
                }
            };
        
            try {
                await register(registrationData);
                if (role === 'ADMIN') {
                    setSuccessMessage('Admin added successfully!');
                    formik.resetForm();
                } else {
                    navigate('/login', { state: { successMessage: 'Registration successful! Please log in.' } });
                }
                setErrorMessage(''); 
            } catch (error) {
                console.error('Registration error', error);
                if (error.response && error.response.status === 409) {
                    setErrorMessage(error.response.data.message); 
                } else {
                    setErrorMessage('Registration failed. Please try again.'); 
                }
            }
        }
    });

    return (
        <div className="container-fluid mt-0" style={{ height: '100vh', padding: 0 }}>
            <div className="row no-gutters" style={{ height: '100%' }}>
                <div className="col-md-6" style={{ 
                    backgroundImage: `url(${biddingImage})`, 
                    backgroundSize: 'cover', 
                    backgroundPosition: 'center',
                    height: '100vh' 
                }} />
                <div className="col-md-6 d-flex align-items-center justify-content-center flex-column">
                    {successMessage && (
                        <div className="alert alert-success mb-3" style={{ width: '400px' }}>
                            {successMessage}
                        </div>
                    )}
                    {errorMessage && (
                        <div className="alert alert-danger mb-3" style={{ width: '400px' }}>
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
                            <h2 className="card-title">Register</h2>
                            <form onSubmit={formik.handleSubmit}>
                                <div className="row">
                                    <div className="col-md-6 mb-3">
                                        <label htmlFor="firstName" className="form-label">First Name *</label>
                                        <input 
                                            id="firstName" 
                                            type="text" 
                                            className="form-control" 
                                            {...formik.getFieldProps('firstName')} 
                                        />
                                        {formik.touched.firstName && formik.errors.firstName ? (
                                            <div className="text-danger">{formik.errors.firstName}</div>
                                        ) : null}
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label htmlFor="lastName" className="form-label">Last Name (Optional)</label>
                                        <input 
                                            id="lastName" 
                                            type="text" 
                                            className="form-control" 
                                            {...formik.getFieldProps('lastName')} 
                                        />
                                        {formik.touched.lastName && formik.errors.lastName ? (
                                            <div className="text-danger">{formik.errors.lastName}</div>
                                        ) : null}
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="phoneNumber" className="form-label">Phone Number *</label>
                                    <div className="input-group">
                                        <span className="input-group-text" id="phonePrefix">+91</span>
                                        <input 
                                            id="phoneNumber" 
                                            type="text" 
                                            className="form-control" 
                                            {...formik.getFieldProps('phoneNumber')} 
                                        />
                                    </div>
                                    {formik.touched.phoneNumber && formik.errors.phoneNumber ? (
                                        <div className="text-danger">{formik.errors.phoneNumber}</div>
                                    ) : null}
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="email" className="form-label">Email *</label>
                                    <input 
                                        id="email" 
                                        type="email" 
                                        className="form-control" 
                                        {...formik.getFieldProps('email')} 
                                    />
                                    {formik.touched.email && formik.errors.email ? (
                                        <div className="text-danger">{formik.errors.email}</div>
                                    ) : null}
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="password" className="form-label">Password *</label>
                                    <input 
                                        id="password" 
                                        type={showPassword ? 'text' : 'password'} 
                                        className="form-control" 
                                        {...formik.getFieldProps('password')} 
                                    />
                                    {formik.touched.password && formik.errors.password ? (
                                        <div className="text-danger">{formik.errors.password}</div>
                                    ) : null}
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
                                <button type="submit" className="btn btn-light">Register</button>
                            </form>
                            <p className="mt-2" style={{ color: 'white' }}>* All fields are mandatory</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;
