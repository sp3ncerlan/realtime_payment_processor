import PaymentTable from "../components/PaymentTable";
import SendPaymentCard from "../components/SendPaymentCard";
import { useAuth } from '../context/AuthContext';
import { useAccountDetails, useCustomerAccount } from "../hooks/useCustomerAccount";
import { usePayments } from "../hooks/usePayments";
import { useEffect, useState, useRef } from "react";
import accountService from "../services/accountService";
import CountUp from "react-countup";
import CustomerDropdown from "../components/CustomerDropdown";


const Dashboard = () => {
  const { currentUser, selectedAccount, switchAccount } = useAuth();
  const { accounts, isLoading: accountsLoading } = useCustomerAccount(currentUser?.id);
  const [showAccountsDropdown, setShowAccountsDropdown] = useState(false);
  const infoBtnRef = useRef(null);

  // Close dropdown on outside click
  useEffect(() => {
    if (!showAccountsDropdown) return;
    function handleClick(e) {
      if (infoBtnRef.current && !infoBtnRef.current.contains(e.target)) {
        setShowAccountsDropdown(false);
      }
    }
    document.addEventListener('mousedown', handleClick);
    return () => document.removeEventListener('mousedown', handleClick);
  }, [showAccountsDropdown]);
  const [allAccounts, setAllAccounts] = useState([]);
  const [allAccountsLoading, setAllAccountsLoading] = useState(true);

  useEffect(() => {
    const fetchAllAccounts = async () => {
      try {
        setAllAccountsLoading(true);
        const data = await accountService.getAllAccounts();
        setAllAccounts(data);
      } catch (err) {
        setAllAccounts([]);
      } finally {
        setAllAccountsLoading(false);
      }
    };
    fetchAllAccounts();
  }, []);
  const { accountData, isLoading: detailsLoading, error } = useAccountDetails(selectedAccount?.id);
  const { payments, totalPayments, totalPendingPayments, isConnected, isLoading: paymentsLoading, error: paymentError, latestPaymentId } = usePayments(selectedAccount?.id);

  useEffect(() => {
    if (accounts.length > 0 && !selectedAccount) {
      switchAccount(accounts[0]);
    }
  }, [accounts, selectedAccount, switchAccount]);

  const formatAccountNumber = (accountNumber) => {
    if (!accountNumber) return '****-****';
    return `****-${accountNumber.slice(-4)}`;
  };

  const formatActiveDate = (isoString) => {
    if (!isoString) return 'N/A';

    const date = new Date(isoString);

    return date.toLocaleString('en-US', {
      month: 'short',
      year: 'numeric',
    });
  };

  const formatPercentage = (value) => {
    if (value == null) return '0.0%';
    const sign = value >= 0 ? '+' : '';
    return `${sign}${Number(value).toFixed(1)}%`;
  };

  console.log(accountData)

  return (
    <div className="flex p-8 min-h-screen bg-gray-950 overflow-hidden">
      <div className="flex-1 flex flex-col min-h-screen">
        <div className="flex-1 flex flex-col">
          <div className="flex items-center w-full mb-2">
            <div className="flex-1 min-w-0">
              <h1 className="text-3xl font-bold text-gray-100 whitespace-nowrap overflow-hidden text-ellipsis">
                Payment Processor Dashboard
              </h1>
              <p className="text-xs text-gray-400 mt-1 ml-1">Click the <span className="inline-block align-middle"><svg xmlns="http://www.w3.org/2000/svg" className="inline h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M12 20a8 8 0 100-16 8 8 0 000 16z" /></svg></span> icon to see test accounts you can send money to.</p>
            </div>
            <div className="relative ml-3" ref={infoBtnRef}>
              <button
                type="button"
                className={`align-middle text-gray-400 hover:text-blue-400 focus:outline-none ${showAccountsDropdown ? 'text-blue-400' : ''}`}
                onClick={() => setShowAccountsDropdown(v => !v)}
                aria-label="Show available accounts"
              >
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M12 20a8 8 0 100-16 8 8 0 000 16z" />
                </svg>
              </button>
              {showAccountsDropdown && (
                <div className="absolute right-0 z-50 mt-2 bg-gray-900 border border-gray-700 rounded-lg shadow-lg p-3 min-w-[220px] max-h-60 overflow-y-auto select-text">
                  <div className="font-semibold text-gray-200 mb-2 text-xs text-center">Available Accounts</div>
                  <table className="w-full text-xs text-gray-300">
                    <thead>
                      <tr>
                        <th className="text-left px-2 py-1">Account #</th>
                        <th className="text-left px-2 py-1">Name</th>
                      </tr>
                    </thead>
                    <tbody>
                      {allAccountsLoading ? (
                        <tr><td colSpan={2} className="px-2 py-1 text-gray-500 text-center">Loading...</td></tr>
                      ) : allAccounts && allAccounts.length > 0 ? allAccounts.map(acc => (
                        <tr key={acc.id}>
                          <td className="px-2 py-1 font-mono whitespace-nowrap">{acc.accountNumber}</td>
                          <td className="px-2 py-1 whitespace-nowrap">{acc.name || acc.accountType || '-'}</td>
                        </tr>
                      )) : (
                        <tr><td colSpan={2} className="px-2 py-1 text-gray-500 text-center">No accounts</td></tr>
                      )}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
            <div className="ml-4">
              <CustomerDropdown />
            </div>
          </div>
          <p className="text-gray-400 mb-8">Welcome to your real-time payment system.</p>
          <div className="mb-8">
            <h2 className="text-xl font-semibold text-gray-200 mb-4">Account Overview</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Available Balance</p>
                <p className="text-3xl font-bold text-gray-100">
                    <CountUp
                    end={accountData?.balance || 0}
                    duration={1}
                    decimals={2}
                    prefix="$"
                    separator="," />
                </p>
                <p className={`text-sm mt-2 ${
                  accountData?.balanceChangePercentage > 0 ? 'text-green-500' : 
                  accountData?.balanceChangePercentage < 0 ? 'text-red-500' :
                  'text-gray-400'
                }`}>{formatPercentage(accountData?.balanceChangePercentage)} since last month</p>
              </div>
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Account Number</p>
                <p className="text-2xl font-mono font-semibold text-gray-100">{formatAccountNumber(accountData?.accountNumber)}</p>
                <p className="text-sm text-gray-500 mt-2">Active since {formatActiveDate(accountData?.createdAt)}</p>
              </div>
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Total Transactions</p>
                <p className="text-3xl font-bold text-gray-100">{totalPayments}</p>
                <p className="text-sm text-blue-400 mt-2">{totalPendingPayments} pending</p>
              </div>
            </div>
          </div>
          <div className="-mt-8 mb-6">
            <SendPaymentCard onSuccess={null} />
          </div>
          <div>
            <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
              <PaymentTable
                currentCustomer={currentUser}
                allAccounts={allAccounts}
                accounts={accounts}
                currentAccountData={accountData}
                payments={payments}
                isConnected={isConnected}
                isLoading={paymentsLoading}
                error={paymentError}
                latestPaymentId={latestPaymentId}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;