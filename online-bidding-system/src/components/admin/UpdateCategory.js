import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const UpdateCategory = () => {
    const { categoryId } = useParams();
    const navigate = useNavigate();
    const [category, setCategory] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        const fetchCategory = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/categories/${categoryId}`);
                setCategory(response.data);
            } catch (error) {
                console.error("Error fetching category:", error);
                setErrorMessage('Failed to load category. Please try again.');
            }
        };

        fetchCategory();
    }, [categoryId]);

    const validationSchema = Yup.object().shape({
        categoryDescription: Yup.string()
            .required('Category description cannot be empty.')
            .max(255, 'Category description cannot exceed 255 characters.'),
    });

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            await axios.put(`http://localhost:8080/api/categories/${categoryId}`, {
                categoryDescription: values.categoryDescription,
            });
            setSuccessMessage('Category updated successfully!');
            setErrorMessage(''); // Clear any previous error messages

            
            setTimeout(() => {
                navigate('/admin/view-categories'); 
            }, 2000);
        } catch (error) {
            console.error("Error updating category:", error);
            setErrorMessage('Failed to update category. Please try again.');
            setSuccessMessage(''); // Clear any previous success messages
        } finally {
            setSubmitting(false);
        }
    };

    if (!category) {
        return <p>Loading category information...</p>;
    }

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
                <h2 style={{ color: '#5c23a6' }}>Update Category</h2>
                {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
                {successMessage && <div className="alert alert-success">{successMessage}</div>}
                <Formik
                    initialValues={{
                        categoryDescription: category.categoryDescription,
                    }}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting, errors, touched }) => (
                        <Form>
                            <div className="mb-3">
                                <label className="form-label">Category Name</label>
                                <input type="text" className="form-control" value={category.categoryName} readOnly />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Category Description</label>
                                <Field 
                                    as="textarea"
                                    className={`form-control ${errors.categoryDescription && touched.categoryDescription ? 'is-invalid' : ''}`}
                                    name="categoryDescription"
                                    required
                                />
                                <div style={{ color: 'red', marginTop: '5px' }}>
                                    <ErrorMessage name="categoryDescription" />
                                </div>
                            </div>
                            <button type="submit" className="btn" style={{ backgroundColor: '#5c23a6', color: 'white' }} disabled={isSubmitting}>
                                Update Category
                            </button>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
};

export default UpdateCategory;
