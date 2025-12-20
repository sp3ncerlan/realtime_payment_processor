import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const accountService = {
    getCustomerAccounts: async (customerId) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/customers/${customerId}/accounts`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },
    
    getAccountByAccountNumber: async (customerNumber) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/accounts/number/${customerNumber}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getAccountDetails: async (accountId) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/accounts/id/${accountId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getAllAccounts: async () => {
        try {
            const response = await axios.get(`${API_BASE_URL}/accounts`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }
}

export default accountService;