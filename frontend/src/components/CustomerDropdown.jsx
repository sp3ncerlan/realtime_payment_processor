import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import customerService from "../services/customerService";

const CustomerDropdown = () => {
  const { currentUser, switchUser } = useAuth();
  const [customers, setCustomers] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    customerService.getAllCustomers()
      .then(setCustomers)
      .finally(() => setLoading(false));
  }, []);

  const handleSelect = async (customer) => {
    await switchUser(customer);
    setShowDropdown(false);
  };

  return (
    <div className="relative inline-block text-left">
      <button
        onClick={() => setShowDropdown((v) => !v)}
        className="flex items-center gap-2 px-4 py-2 bg-gray-800 text-gray-100 rounded-lg shadow hover:bg-gray-700 focus:outline-none"
      >
        <span>{currentUser ? currentUser.name : loading ? "Loading..." : "Select Customer"}</span>
        <svg className={`w-4 h-4 transition-transform ${showDropdown ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>
      {showDropdown && (
        <div className="absolute right-0 mt-2 w-56 rounded-md shadow-lg bg-gray-900 ring-1 ring-black ring-opacity-5 z-50">
          <div className="py-1 max-h-60 overflow-y-auto">
            {customers.map((customer) => (
              <button
                key={customer.id}
                onClick={() => handleSelect(customer)}
                className={`w-full text-left px-4 py-2 text-sm hover:bg-gray-800 transition-colors ${currentUser?.id === customer.id ? 'bg-gray-800 text-blue-400' : 'text-gray-100'}`}
              >
                <div className="font-medium">{customer.name}</div>
                <div className="text-xs text-gray-400">{customer.email}</div>
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default CustomerDropdown;
