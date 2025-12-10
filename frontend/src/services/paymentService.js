import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const paymentService = {
  // Get all payments
  getAllPayments: async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments`);
      return response.data;
    } catch (error) {
      console.error('Error fetching payments:', error);
      throw error;
    }
  },

  // Get recent payments (last N)
  getRecentPayments: async (limit = 8) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/recent`, {
        params: { limit }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching recent payments:', error);
      throw error;
    }
  },

  // Get payment by ID
  getPaymentById: async (id) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching payment:', error);
      throw error;
    }
  },

  // Send money (create new payment)
  sendMoney: async (paymentData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/payments`, paymentData);
      return response.data;
    } catch (error) {
      console.error('Error sending money:', error);
      throw error;
    }
  },
};

export default paymentService;