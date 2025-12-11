import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const paymentService = {
  // Get all payments
  getCustomerPayments: async (customerId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching customer payments:', error);
      throw error;
    }
  },

  // Get recent payments (last N)
  getRecentCustomerPayments: async (customerId, limit = 8) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}/recent`, {
        params: { limit }
      });   
      return response.data;
    } catch (error) {
      console.error('Error fetching recent customer payments:', error);
      throw error;
    }
  },

  // Get payment by ID belonging to customer
  getPaymentById: async (customerId, paymentId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}/payment/${paymentId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching payment:', error);
      throw error;
    }
  },

  // Send money (create new payment)
  sendMoney: async (customerId, paymentData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/payments/customer/${customerId}`, paymentData);
      return response.data;
    } catch (error) {
      console.error('Error sending money:', error);
      throw error;
    }
  },
};

export default paymentService;