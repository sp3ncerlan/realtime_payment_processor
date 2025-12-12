import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [selectedAccount, setSelectedAccount] = useState(null);

    useEffect(() => {
        const storedUser = localStorage.getItem('currentUser');
        if (storedUser) {
            setCurrentUser(JSON.parse(storedUser));
        }
    }, []);

    const switchUser = (user) => {
        setCurrentUser(user);
        localStorage.setItem('currentUser', JSON.stringify(user));

        setSelectedAccount(null);
        localStorage.removeItem('selectedAccount');
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