import SideBar from "../components/Sidebar";
import PaymentTable from "../components/PaymentTable";
import { useAuth } from '../context/AuthContext';
import { useAccountDetails, useCustomerAccount } from "../hooks/useCustomerAccount";
import { usePayments } from "../hooks/usePayments";
import { useEffect } from "react";
import CountUp from "react-countup";


const Dashboard = () => {
  const { currentUser, selectedAccount, switchAccount } = useAuth();
  const { accounts, allAccounts, isLoading: accountsLoading } = useCustomerAccount(currentUser?.id);
  const { accountData, isLoading: detailsLoading, error } = useAccountDetails(selectedAccount?.id);
  const { payments, totalPayments, totalPendingPayments, isConnected, isLoading: paymentsLoading, error: paymentError } = usePayments(selectedAccount?.id);

  console.log(allAccounts);

  useEffect(() => {
    if (accounts.length > 0 && !selectedAccount) {
      switchAccount(accounts[0]);
    }
  }, [accounts, selectedAccount, switchAccount]);

  useEffect(() => {
    if (selectedAccount) {
      console.log(`Selected account updated: ${selectedAccount.id}`);
    }
  }, [selectedAccount]);

  const formatCurrency = (amount) => {
    if (amount == null) return '$0.00';
    return `$${Number(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

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

  return (
    <div className="flex min-h-screen bg-gray-950 overflow-x-hidden">
      
      <div className="flex-shrink-0 h-screen">
        <SideBar />
      </div>

      
      <div className="flex-1 p-8 flex flex-col min-h-screen">
        <div className="max-w-7xl mx-auto flex-1 flex flex-col">
          <h1 className="text-3xl font-bold text-gray-100 mb-2">Payment Processor Dashboard</h1>
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
                    separator=","
                  />
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

          
          <div className="flex-1 flex flex-col justify-between">
            <div
              className={`bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800${payments.length === 0 ? '' : ' flex-1'}`}
              style={
                payments.length === 0
                  ? { minHeight: 'unset', maxHeight: 'unset', height: 'auto', alignSelf: 'flex-start', width: '100%' }
                  : { minHeight: 0, maxHeight: '100%', overflowY: 'auto', height: '100%' }
              }
            >
              <PaymentTable
                currentCustomer={currentUser}
                allAccounts={allAccounts}
                accounts={accounts}
                currentAccountData={accountData}
                payments={payments}
                isConnected={isConnected}
                isLoading={paymentsLoading}
                error={paymentError}
              />
            </div>

            
            
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;