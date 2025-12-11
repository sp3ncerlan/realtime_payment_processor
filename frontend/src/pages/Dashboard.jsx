import SideBar from "../components/Sidebar";
import PaymentTable from "../components/PaymentTable";
import { useAuth } from '../context/AuthContext';


const Dashboard = () => {
  const { currentUser, switchUser, logout } = useAuth();

  return (
    <div className="flex min-h-screen bg-gray-950">
      <SideBar />

      <div className="flex-1 p-8">
        <div className="max-w-7xl mx-auto">
          <h1 className="text-3xl font-bold text-gray-100 mb-2">Payment Processor Dashboard</h1>
          <p className="text-gray-400 mb-8">Welcome to your real-time payment system.</p>

          {/* Account Overview Section */}
          <div className="mb-8">
            <h2 className="text-xl font-semibold text-gray-200 mb-4">Account Overview</h2>

            {/* Stats Grid */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
              {/* Balance Card */}
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Available Balance</p>
                <p className="text-3xl font-bold text-gray-100">$12,450.00</p>
                <p className="text-sm text-green-500 mt-2">+2.5% from last month</p>
              </div>

              {/* Account Number Card */}
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Account Number</p>
                <p className="text-2xl font-mono font-semibold text-gray-100">****-1234</p>
                <p className="text-sm text-gray-500 mt-2">Active since Jan 2024</p>
              </div>

              {/* Transaction Count Card */}
              <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
                <p className="text-sm font-medium text-gray-400 mb-1">Total Transactions</p>
                <p className="text-3xl font-bold text-gray-100">156</p>
                <p className="text-sm text-blue-400 mt-2">8 pending</p>
              </div>
            </div>
          </div>

          {/* Quick Actions Section */}
          <div className="mb-8">
            <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
              <h3 className="text-xl font-semibold text-gray-200 mb-4">Quick Actions</h3>
              <div className="flex gap-4">
                <button className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition-colors">
                  Send Money
                </button>
                <button className="bg-gray-800 hover:bg-gray-700 text-gray-100 font-medium py-2 px-4 border border-gray-700 rounded-md transition-colors">
                  View History
                </button>
              </div>
            </div>
          </div>

          {/* Live Feed Section */}
          <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800">
            <PaymentTable currentCustomer={currentUser}/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;