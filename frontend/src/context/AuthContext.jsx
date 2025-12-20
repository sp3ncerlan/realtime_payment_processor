import { createContext, useContext, useState, useEffect } from "react";
import accountService from "../services/accountService";
import customerService from "../services/customerService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [selectedAccount, setSelectedAccount] = useState(null);

    useEffect(() => {
        const init = async () => {
            const storedUser = localStorage.getItem('currentUser');
            if (storedUser) {
                const parsedUser = JSON.parse(storedUser);
                setCurrentUser(parsedUser);
                // Also auto-select their first account if not already set
                try {
                    const accounts = await accountService.getCustomerAccounts(parsedUser.id);
                    if (accounts && accounts.length > 0) {
                        setSelectedAccount(accounts[0]);
                        localStorage.setItem('selectedAccount', JSON.stringify(accounts[0]));
                    }
                } catch (e) {
                    // ignore
                }
            } else {
                // No user in storage, auto-select first customer
                try {
                    const customers = await customerService.getAllCustomers();
                    if (customers && customers.length > 0) {
                        setCurrentUser(customers[0]);
                        localStorage.setItem('currentUser', JSON.stringify(customers[0]));
                        // Also auto-select their first account
                        const accounts = await accountService.getCustomerAccounts(customers[0].id);
                        if (accounts && accounts.length > 0) {
                            setSelectedAccount(accounts[0]);
                            localStorage.setItem('selectedAccount', JSON.stringify(accounts[0]));
                        }
                    }
                } catch (e) {
                    // ignore
                }
            }
            const storedAccount = localStorage.getItem('selectedAccount');
            if (storedAccount) {
                setSelectedAccount(JSON.parse(storedAccount));
            }
        };
        init();
    }, []);

    const switchUser = async (user) => {
        setCurrentUser(user);
        localStorage.setItem('currentUser', JSON.stringify(user));

        setSelectedAccount(null);
        localStorage.removeItem('selectedAccount');

        // Auto-select first account for the new user
        if (user) {
            try {
                const accounts = await accountService.getCustomerAccounts(user.id);
                console.log(accounts);
                if (accounts && accounts.length > 0) {
                    setSelectedAccount(accounts[0]);
                    localStorage.setItem('selectedAccount', JSON.stringify(accounts[0]));
                }
            } catch (e) {
                // ignore
            }
        }
    };

    const switchAccount = (account) => {
        setSelectedAccount(account);
        localStorage.setItem('selectedAccount', JSON.stringify(account));
    }

    const logout = () => {
        setCurrentUser(null);
        localStorage.removeItem('currentUser');
    };

    return (
        <AuthContext.Provider value={{ currentUser, selectedAccount, switchUser, switchAccount, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error('useAuth must be used within AuthProvider')
    }

    return context;
}