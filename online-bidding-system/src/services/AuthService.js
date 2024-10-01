import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/'; 

export const register = (userData) => {
    return axios.post(`${API_URL}register`, userData);
};

export const login = async (userData) => {
    try {
        const response = await axios.post(`${API_URL}login`, userData);
        return response.data; 
    } catch (error) {
        throw new Error("Login failed"); 
    }
};
