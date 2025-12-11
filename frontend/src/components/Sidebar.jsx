import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import customerService from '../services/customerService';

const SideBar = () => {
    const { currentUser, switchUser, logout } = useAuth();

    const [customers, setCustomers] = useState([]);
    const [showCustomerMenu, setShowCustomerMenu] = useState(false);
    const [isLoadingCustomers, setIsLoadingCustomers] = useState(false);

    useEffect(() => {
        loadCustomers();
    }, [])

    const loadCustomers = async () => {
        try {
            setIsLoadingCustomers(true);
            console.log('fetching customers from service')

            const data = await customerService.getAllCustomers();
            console.log('customers are loaded', data);
            setCustomers(data);

            if (!currentUser && data.length > 0) {
                console.log('automatically selecting first customer', data[0]);
                switchUser(data[0]);
            }
        } catch (err) {
            console.error('failed to load customers: ', err);
        } finally {
            setIsLoadingCustomers(false);
        }
    }

    const handleCustomerSwitch = (customer) => {
        console.log('user has clicked on the customer: ', customer);
        switchUser(customer);
        setShowCustomerMenu(false);
    }

    return (
        <div className="flex h-screen w-64 flex-col justify-between border-e border-gray-800 bg-gray-900">
            <div className="px-4 py-6">
                <span className="grid h-10 w-32 place-content-center rounded-lg bg-gray-800 text-xs text-gray-300">
                Logo
                </span>

                <ul className="mt-6 space-y-1">
                <li>
                    <a href="#" className="block rounded-lg bg-gray-800 px-4 py-2 text-sm font-medium text-gray-100">
                    General
                    </a>
                </li>

                <li>
                    <a href="#" className="block rounded-lg px-4 py-2 text-sm font-medium text-gray-400 hover:bg-gray-800 hover:text-gray-100">
                    Billing
                    </a>
                </li>

                <li>
                    <a href="#" className="block rounded-lg px-4 py-2 text-sm font-medium text-gray-400 hover:bg-gray-800 hover:text-gray-100">
                    Invoices
                    </a>
                </li>
                </ul>
            </div>

            <div className="sticky inset-x-0 bottom-0 border-t border-gray-800">
                <div className="relative">
                    <button 
                        onClick={() => setShowCustomerMenu(!showCustomerMenu)}
                        className="flex w-full items-center gap-2 bg-gray-900 p-4 hover:bg-gray-800 transition-colors"
                    >
                        <div className="flex-1 text-left">
                            {currentUser ? (
                                <p className="text-xs">
                                    <strong className="block font-medium text-gray-100">
                                        {currentUser.name}
                                    </strong>
                                    <span className="text-gray-400">{currentUser.email}</span>
                                </p>
                            ) : (
                                <p className="text-xs">
                                    <strong className="block font-medium text-gray-400">
                                        {isLoadingCustomers ? 'Loading...' : 'Select a customer'}
                                    </strong>
                                </p>
                            )}
                        </div>
                        <svg 
                            xmlns="http://www.w3.org/2000/svg" 
                            className={`size-5 text-gray-400 transition-transform ${showCustomerMenu ? 'rotate-180' : ''}`}
                            viewBox="0 0 20 20" 
                            fill="currentColor"
                        >
                            <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
                        </svg>
                    </button>

                    {showCustomerMenu && (
                        <div className="absolute bottom-full left-0 w-full bg-gray-800 border border-gray-700 rounded-t-lg shadow-lg max-h-64 overflow-y-auto">
                            {customers.map((customer) => (
                                <button
                                    key={customer.customer_id}
                                    onClick={() => handleCustomerSwitch(customer)}
                                    className={`w-full text-left p-3 hover:bg-gray-700 transition-colors border-b border-gray-700 last:border-b-0 ${
                                        currentUser?.customer_id === customer.customer_id ? 'bg-gray-700' : ''
                                    }`}
                                >
                                    <p className="text-xs">
                                        <strong className="block font-medium text-gray-100">
                                            {customer.name}
                                        </strong>
                                        <span className="text-gray-400">{customer.email}</span>
                                    </p>
                                </button>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default SideBar;