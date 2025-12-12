import { usePayments } from '../hooks/usePayments';
import StripedTable from './reusables/StripedTable';

const PaymentTable = ({ currentCustomer, currentAccountData, payments, allAccounts, isConnected, isLoading, error }) => {
  if (!allAccounts || allAccounts.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-400">Loading accounts...</p>
      </div>
    );
  }

  if (!currentCustomer || !currentCustomer.id) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-400">Please select a customer to view payments</p>
      </div>
    );
  }

  console.log(allAccounts);

  const columns = [
    { key: 'id', label: 'ID', render: (payment) => payment.id?.substring(0, 8) || 'N/A' },
    {
      key: 'sender',
      label: 'From',
      render: (payment) => {
        const account = allAccounts.find((acc) => acc.id === payment.sourceAccountId);
        return (
          <div>
            <p>{payment.sourceName || 'Unknown'}</p>
            <p className="text-xs text-gray-400">{account?.accountType || 'N/A'}</p>
          </div>
        );
      },
    },
    {
      key: 'recipient',
      label: 'To',
      render: (payment) => {
        const account = allAccounts.find((acc) => acc.id === payment.destAccountId);
        return (
          <div>
            <p>{payment.destName || 'Unknown'}</p>
            <p className="text-xs text-gray-400">{account?.accountType || 'N/A'}</p>
          </div>
        );
      },
    },
    {
      key: 'amount',
      label: 'Amount',
      render: (payment) => {
        const isIncoming = payment?.destAccountId === currentAccountData?.id;
        const colorClass = isIncoming ? 'text-green-400' : 'text-red-400';
        const prefix = isIncoming ? '+' : '-';
        return (
          <span className={colorClass}>
            {prefix}${payment?.amount?.toFixed(2) || '0.00'}
          </span>
        );
      },
    },
    {
      key: 'status',
      label: 'Status',
      render: (payment) => (
        <span
          className={`inline-flex items-center justify-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
            payment.status === 'COMPLETED'
              ? 'bg-green-900 text-green-300'
              : payment.status === 'PENDING'
              ? 'bg-yellow-900 text-yellow-300'
              : payment.status === 'FAILED'
              ? 'bg-red-900 text-red-300'
              : 'bg-gray-800 text-gray-300'
          }`}
        >
          {payment.status || 'UNKNOWN'}
        </span>
      ),
    },
    {
      key: 'timestamp',
      label: 'Time',
      render: (payment) =>
        payment.createdAt
          ? new Date(payment.createdAt).toLocaleString('en-US', {
              month: '2-digit',
              day: '2-digit',
              year: 'numeric',
              hour: '2-digit',
              minute: '2-digit',
              hour12: true,
            })
          : 'N/A',
    },
  ];

  return (
    <div>
      {/* Connection Status */}
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-xl font-semibold text-gray-200">Live Payment Feed</h3>
        <div className="flex items-center gap-2">
          <div className={`w-2 h-2 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'} animate-pulse`}></div>
          <span className="text-sm text-gray-400">{isConnected ? 'Live' : 'Disconnected'}</span>
        </div>
      </div>

      {/* Generic Table */}
      <StripedTable
        columns={columns}
        data={payments}
        isLoading={isLoading}
        error={error}
        emptyMessage="No payments yet. Waiting for transactions..."
      />

      {/* Payment Count */}
      <div className="mt-4 text-sm text-gray-400 text-center">
        Showing {payments.length} most recent payments
      </div>
    </div>
  );
};

export default PaymentTable;