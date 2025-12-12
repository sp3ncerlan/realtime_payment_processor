import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const customerService = {
  getAllCustomers: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/customers`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getCustomerById: async (customerId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/customers/${customerId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
};

export default customerService;