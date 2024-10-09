import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Alert, Table, Form, InputGroup } from 'react-bootstrap';
import '../../resources/css/AllOrders.css'; // Importing custom CSS

const AllOrders = () => {
    const [orders, setOrders] = useState([]);
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ text: '', type: '' });
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('All');

    const API_URL = 'http://localhost:8080/api/all-orders';

    const fetchOrders = async () => {
        try {
            const response = await axios.get(API_URL, { withCredentials: true });
            setOrders(response.data);
            setFilteredOrders(response.data);
        } catch (error) {
            console.error("Error fetching all orders:", error);
            setMessage({ text: 'Failed to fetch orders.', type: 'danger' });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    useEffect(() => {
        const filtered = orders.filter(order => {
            const matchesId = order.orderId.toString().includes(searchTerm);
            const matchesStatus = statusFilter === 'All' || order.orderStatus === statusFilter;
            return matchesId && matchesStatus;
        });
        setFilteredOrders(filtered);
    }, [searchTerm, statusFilter, orders]);

    if (loading) {
        return <p>Loading all orders...</p>;
    }

    return (
        <div className="container mt-5">
            <h2 style={{ color: '#5c23a6' }}>All Orders</h2>
            {message.text && (
                <Alert variant={message.type === 'success' ? 'success' : 'danger'}>
                    {message.text}
                </Alert>
            )}

            <InputGroup className="mb-3">
                <Form.Control 
                    type="text" 
                    placeholder="Enter Order ID" 
                    value={searchTerm} 
                    onChange={(e) => setSearchTerm(e.target.value)} 
                    style={{ width: '250px', borderColor: '#5c23a6' }} // Adjusted border color
                />
                <Form.Select 
                    value={statusFilter} 
                    onChange={(e) => setStatusFilter(e.target.value)} 
                    style={{ width: '150px', marginLeft: '10px', borderColor: '#5c23a6' }} // Adjusted border color
                >
                    <option value="All">All</option>
                    <option value="Dispatched">Dispatched</option>
                    <option value="Shipped">Shipped</option>
                    <option value="Out for Delivery">Out for Delivery</option>
                    <option value="Delivered">Delivered</option>
                    <option value="In Transit">In Transit</option>
                </Form.Select>
            </InputGroup>

            <Table striped bordered hover style={{ borderColor: '#5c23a6' }}>
                <thead style={{ backgroundColor: '#5c23a6', color: 'white' }}>
                    <tr>
                        <th>Order ID</th>
                        <th>Product Name</th>
                        <th>Buyer ID</th>
                        <th>Seller ID</th>
                        <th>Order Status</th>
                        <th>Bid Amount</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredOrders.length > 0 ? (
                        filteredOrders.map(order => (
                            <tr key={order.orderId}>
                                <td>{order.orderId}</td>
                                <td>{order.product.productName}</td>
                                <td>{order.buyerId.userId}</td>
                                <td>{order.sellerId.userId}</td>
                                <td>{order.orderStatus}</td>
                                <td>Rs {order.bidAmount}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6" className="text-center">No orders found</td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </div>
    );
};

export default AllOrders;
