import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const AccountService = {
    getCustomerAccounts: async (customerId) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/customers/${customerId}/accounts`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getAccountDetails: async (accountId) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/accounts/${accountId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }
}

export default AccountService;