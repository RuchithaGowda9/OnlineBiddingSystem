import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const ViewCategories = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/categories/viewcategories');
                setCategories(response.data);
            } catch (error) {
                console.error("Error fetching categories:", error);
                setErrorMessage('Failed to load categories. Please try again.');
            } finally {
                setLoading(false);
            }
        };

        fetchCategories();
    }, []);

    const toggleStatus = async (categoryId, currentStatus) => {
        const newStatus = currentStatus === 'active' ? 'inactive' : 'active';
    
        try {
            await axios.put(`http://localhost:8080/api/categories/${categoryId}/status`, { status: newStatus });
            setCategories(categories.map(cat => 
                cat.categoryId === categoryId ? { ...cat, status: newStatus } : cat
            ));
        } catch (error) {
            console.error("Error updating category status:", error);
            setErrorMessage('Failed to update status. Please try again.');
        }
    };

    if (loading) {
        return <p>Loading categories...</p>;
    }

    return (
        <div className="container mt-5">
            <h2>All Categories</h2>
            {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
            <div className="card" style={{ backgroundColor: '#5c23a6', color: 'white' }}>
                <div className="card-body">
                    {categories.length > 0 ? (
                        <table className="table table-striped table-bordered" style={{ color: 'white' }}>
                            <thead>
                                <tr>
                                    <th style={{ color: '#5c23a6' }}>Category Name</th>
                                    <th style={{ color: '#5c23a6' }}>Category Description</th>
                                    <th style={{ color: '#5c23a6' }}>Status</th>
                                    <th style={{ color: '#5c23a6' }}>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {categories.map(category => (
                                    <tr key={category.categoryId} style={{ backgroundColor: '#6a3eaa' }}>
                                        <td>{category.categoryName}</td>
                                        <td>{category.categoryDescription}</td>
                                        <td>{category.status}</td>
                                        <td>
                                            <button 
                                                className="btn btn-light" 
                                                onClick={() => toggleStatus(category.categoryId, category.status)}
                                                style={{ borderRadius: '5px' }}
                                            >
                                                {category.status === 'active' ? 'Deactivate' : 'Activate'}
                                            </button>
                                            <Link to={`/admin/update-category/${category.categoryId}`} className="btn btn-info" style={{ marginLeft: '10px' }}>
                                                Update
                                            </Link>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>No categories available.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ViewCategories;
