import React from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import Register from './components/auth/Register';
import Login from './components/auth/Login';
import Navbar from './resources/navbar/Navbar';
import AdminNavbar from './resources/navbar/AdminNavbar'; 
import Footer from './resources/footer/Footer';
import Home from './components/Home';
import AdminDashboard from './components/dashboards/AdminDashboard'; 
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import CustomerDashboard from './components/dashboards/CustomerDashboard';
import CustomerNavbar from './resources/navbar/CustomerNavbar';
import AdminProfile from './components/admin/AdminProfile';
import AddCategory from './components/admin/AddCategory';
import ViewCategories from './components/admin/ViewCategories';
import SessionExpired from './utils/SessionExpired';
import UpdateCategory from './components/admin/UpdateCategory';
import BuyerProfile from './components/customer/BuyerProfile';
import SellerProfile from './components/customer/SellerProfile';
import ProductDetail from './components/customer/ProductDetail';
import DeliveryPersonDashboard from './components/dashboards/DeliveryPersonDashboard';
import DeliveryPersonNavbar from './resources/navbar/DeliveryPersonNavbar';
import DeliveryProfile from './components/delivery/DeliveryProfile';
import DeliveryOrders from './components/delivery/DeliveryOrders';
import AllOrders from './components/admin/AllOrders';
import AllProducts from './components/admin/AllProducts';

axios.defaults.withCredentials = true;

const App = () => {
    const location = useLocation(); 

    return (
        <div className="d-flex flex-column min-vh-100"> 
            {location.pathname.startsWith('/admin') ? <AdminNavbar /> : 
             location.pathname.startsWith('/customer') ? <CustomerNavbar/> :
             location.pathname.startsWith('/delivery') ? <DeliveryPersonNavbar/> : <Navbar />}
            <div className="container flex-grow-1"> {/* Main content area */}
                <Routes>
                    <Route path="/register" element={<Register role = "CUSTOMER"/>} />
                    <Route path="/admin/register" element={<Register role="ADMIN" />}/>
                    <Route path="/admin/add-category" element={<AddCategory />} />
                    <Route path="/delivery/dashboard" element={< DeliveryPersonDashboard/>} />
                    <Route path="/delivery/profile" element={< DeliveryProfile/>} />
                    <Route path="/delivery/my-orders" element={< DeliveryOrders/>} />
                    <Route path="/admin/view-categories" element={<ViewCategories />} />
                    <Route path="/admin/all-orders" element={<AllOrders/>} />
                    <Route path="/admin/all-products" element={<AllProducts/>} />
                    <Route path="/admin/update-category/:categoryId" element={<UpdateCategory/>} />
                    <Route path="/session-expired" element={<SessionExpired />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/" element={<Home />} />
                    <Route path="/admin/dashboard" element={<AdminDashboard />} />
                    <Route path="/customer/dashboard" element={<CustomerDashboard />} />
                    <Route path="/customer/buyers-profile" element={<BuyerProfile/>} />
                    <Route path="/customer/sellers-profile" element={<SellerProfile/>} />
                    <Route path="/customer/productdetail/:productId" element={<ProductDetail />} />
                    <Route path="/admin/profile" element={<AdminProfile />} />
                </Routes>
            </div>
            <Footer /> {/* Footer */}
        </div>
    );
};

const MainApp = () => (
    <Router>
        <App />
    </Router>
);

export default MainApp;
