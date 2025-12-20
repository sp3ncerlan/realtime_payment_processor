import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

const paymentService = {
  getCustomerPayments: async (customerId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getRecentCustomerPayments: async (customerId, limit = 8) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}/recent`, {
        params: { limit }
      });   
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getPaymentById: async (customerId, paymentId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/payments/customer/${customerId}/payment/${paymentId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  sendMoney: async (customerId, paymentData) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/payments/customer/${customerId}`, paymentData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
};

export default paymentService;