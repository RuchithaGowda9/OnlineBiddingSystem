import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const AddCategory = () => {
    const navigate = useNavigate();
    const [successMessage, setSuccessMessage] = React.useState('');
    const [errorMessage, setErrorMessage] = React.useState('');

    const validationSchema = Yup.object().shape({
        categoryName: Yup.string()
            .required('Category name is required.')
            .max(50, 'Category name cannot exceed 50 characters.'),
        categoryDescription: Yup.string()
            .required('Category description is required.')
            .max(255, 'Category description cannot exceed 255 characters.'),
    });

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            await axios.post('http://localhost:8080/api/categories/add', values, { withCredentials: true });
            setSuccessMessage('Category added successfully!');
            setErrorMessage('');
            setTimeout(() => {
                navigate('/admin/view-categories');
            }, 2000);
        } catch (error) {
            if (error.response && error.response.status === 409) {
                setErrorMessage(error.response.data.message);
            } else {
                setErrorMessage('Failed to add category. Please try again.');
            }
        } finally {
            setSubmitting(false);
        }
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
                <h2 style={{ color: '#5c23a6' }}>Add Category</h2>
                {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
                {successMessage && <div className="alert alert-success">{successMessage}</div>}
                <Formik
                    initialValues={{ categoryName: '', categoryDescription: '' }}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting }) => (
                        <Form>
                            <div className="mb-3">
                                <label className="form-label">Category Name <span style={{ color: 'red' }}>*</span></label>
                                <Field 
                                    type="text" 
                                    className="form-control" 
                                    name="categoryName" 
                                />
                                <ErrorMessage name="categoryName" component="div" style={{ color: 'red', marginTop: '5px' }} />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Category Description <span style={{ color: 'red' }}>*</span></label>
                                <Field 
                                    as="textarea" 
                                    className="form-control" 
                                    name="categoryDescription" 
                                />
                                <ErrorMessage name="categoryDescription" component="div" style={{ color: 'red', marginTop: '5px' }} />
                            </div>
                            <button type="submit" className="btn" style={{ backgroundColor: '#5c23a6', color: 'white' }} disabled={isSubmitting}>
                                Add Category
                            </button>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
};

export default AddCategory;
