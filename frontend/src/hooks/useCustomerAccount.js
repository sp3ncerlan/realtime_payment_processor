import { useEffect, useState } from "react";
import accountService from "../services/accountService";

export const useCustomerAccount = (customerId) => {
    const [allAccounts, setAllAccounts] = useState([]);
    const [accounts, setAccounts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!customerId) {
            setIsLoading(false);
            setAccounts([]);
            return;
        }

        const fetchAccountData = async () => {
            try {
                setIsLoading(true);
                const allAccountsList = await accountService.getAllCustomerAccounts();
                const accountsList = await accountService.getCustomerAccounts(customerId);

                setAllAccounts(allAccountsList);
                setAccounts(accountsList);
                setError(null);
            } catch (error) {
                setError('Failed to load accounts...');
            } finally {
                setIsLoading(false);
            }
        }

        fetchAccountData();
    }, [customerId])

    return { accounts, allAccounts, isLoading, error };
}

export const useAccountDetails = (accountId) => {
    const [accountData, setAccountData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!accountId) {
            setIsLoading(false);
            setAccountData(null);
            return;
        }

        const fetchAccountDetails = async () => {
            try {
                setIsLoading(true);
                const details = await accountService.getAccountDetails(accountId);
                
                setAccountData(details);
                setError(null);
            } catch (err) {
                setError('Failed to load account details');
                setAccountData(null);
            } finally {
                setIsLoading(false);
            }
        };

        fetchAccountDetails();
    }, [accountId]);

    return { accountData, isLoading, error };
}