import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

class AccountService {
    async getAllCustomerAccounts() {
        try {
            const response = await axios.get(`${API_BASE_URL}/customers`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }

    async getCustomerAccounts(customerId) {
        try {
            const response = await axios.get(`${API_BASE_URL}/customers/${customerId}/accounts`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }

    async getAccountDetails(accountId) {
        try {
            const response = await axios.get(`${API_BASE_URL}/accounts/${accountId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    }
}

export default new AccountService();