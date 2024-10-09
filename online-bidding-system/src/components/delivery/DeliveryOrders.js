import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Alert, Card, Button, Dropdown, Form } from 'react-bootstrap';
import '../../resources/css/DeliveryOrders.css';

const DeliveryOrders = () => {
    const [orders, setOrders] = useState([]);
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ text: '', type: '' });
    const [statusOptions] = useState(['All', 'Dispatched', 'Shipped', 'Out for Delivery', 'Delivered', 'In Transit']);
    const [selectedStatus, setSelectedStatus] = useState('All');
    const API_URL = 'http://localhost:8080/api/delivery-orders';

    const fetchOrders = async () => {
        try {
            const response = await axios.get(API_URL, { withCredentials: true });
            setOrders(response.data);
            setFilteredOrders(response.data); // Initialize filtered orders
        } catch (error) {
            console.error("Error fetching delivery orders:", error);
            setMessage({ text: 'Failed to fetch orders.', type: 'danger' });
        } finally {
            setLoading(false);
        }
    };

    const updateOrderStatus = async (orderId, newStatus) => {
        try {
            await axios.put(`${API_URL}/${orderId}/status`, { orderStatus: newStatus }, { withCredentials: true });
            setMessage({ text: 'Order status updated successfully.', type: 'success' });
            fetchOrders(); // Refresh the order list
        } catch (error) {
            console.error("Error updating order status:", error);
            setMessage({ text: 'Failed to update order status.', type: 'danger' });
        }
    };

    const handleStatusFilterChange = (event) => {
        const status = event.target.value;
        setSelectedStatus(status);
        if (status === 'All') {
            setFilteredOrders(orders);
        } else {
            setFilteredOrders(orders.filter(order => order.orderStatus === status));
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    if (loading) {
        return <p>Loading delivery orders...</p>;
    }

    return (
        <div className="container mt-5">
            <h2 style={{ color: '#5c23a6' }}>Delivery Orders</h2>
            {message.text && (
                <Alert variant={message.type === 'success' ? 'success' : 'danger'}>
                    {message.text}
                </Alert>
            )}
            <Form.Group controlId="statusFilter" className="mb-3">
                <Form.Label>Filter by Order Status:</Form.Label>
                <Form.Control 
                    as="select" 
                    value={selectedStatus} 
                    onChange={handleStatusFilterChange} 
                    className="filter-dropdown" // Apply custom class
                >
                    {statusOptions.map(status => (
                        <option key={status} value={status}>{status}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <div className="row">
                {filteredOrders.length > 0 ? (
                    filteredOrders.map(order => (
                        <div className="col-md-4 mb-3" key={order.orderId}>
                            <Card>
                                <Card.Body>
                                    <Card.Title>Order ID: {order.orderId}</Card.Title>
                                    <Card.Text>
                                        <strong>Product Name:</strong> {order.product.productName}<br />
                                        <strong>Buyer ID:</strong> {order.buyerId.userId}<br />
                                        <strong>Seller ID:</strong> {order.sellerId.userId}<br />
                                        <strong>Order Status:</strong> {order.orderStatus}<br />
                                        <strong>Prepaid. Do not collect cash</strong> 
                                    </Card.Text>
                                    {order.orderStatus !== 'Delivered' ? (
                                        <Dropdown>
                                            <Dropdown.Toggle style={{ backgroundColor: '#5c23a6', borderColor: '#5c23a6' }} id="dropdown-basic">
                                                Update Status
                                            </Dropdown.Toggle>
                                            <Dropdown.Menu>
                                                {statusOptions.slice(1).map(status => (
                                                    <Dropdown.Item
                                                        key={status}
                                                        onClick={() => updateOrderStatus(order.orderId, status)}
                                                    >
                                                        {status}
                                                    </Dropdown.Item>
                                                ))}
                                            </Dropdown.Menu>
                                        </Dropdown>
                                    ) : (
                                        <Button variant="primary" disabled>
                                            Order Delivered
                                        </Button>
                                    )}
                                </Card.Body>
                            </Card>
                        </div>
                    ))
                ) : (
                    <p>No orders found</p>
                )}
            </div>
        </div>
    );
};

export default DeliveryOrders;
