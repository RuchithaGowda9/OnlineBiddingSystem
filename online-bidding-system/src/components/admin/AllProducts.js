import React, { useEffect, useState } from 'react';
import axios from 'axios';

const AllProducts = () => {
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/auth/products/');
                setProducts(response.data);
                setFilteredProducts(response.data);
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };
        fetchProducts();
    }, []);

    useEffect(() => {
        const filterProducts = () => {
            let filtered = products.filter(product => 
                product.name.toLowerCase().includes(searchTerm.toLowerCase())
            );

            if (statusFilter !== 'all') {
                filtered = filtered.filter(product => product.status === statusFilter);
            }

            setFilteredProducts(filtered);
        };

        filterProducts();
    }, [searchTerm, statusFilter, products]);

    return (
        <div className="container mt-5">
            <h2 className="text-center mb-4" style={{ color: '#5c23a6' }}>All Products</h2>
            <div className="d-flex mb-4">
                <input
                    type="text"
                    className="form-control me-2"
                    placeholder="Search by product name"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <select 
                    className="form-select" 
                    onChange={(e) => setStatusFilter(e.target.value)}
                    value={statusFilter}
                >
                    <option value="all">All Statuses</option>
                    <option value="available">Available</option>
                    <option value="sold">Sold</option>
                    <option value="pending">Pending</option>
                </select>
            </div>
            <table className="table table-bordered">
                <thead className="table-light">
                    <tr>
                        <th>Product Name</th>
                        <th>Asking Price</th>
                        <th>Status</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredProducts.length > 0 ? (
                        filteredProducts.map(product => (
                            <tr key={product.id}>
                                <td>{product.name}</td>
                                <td>{product.askingPrice}</td>
                                <td>{product.status}</td>
                                <td>{product.description}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="4" className="text-center">No products found</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default AllProducts;
