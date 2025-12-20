import React, { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import paymentService from "../services/paymentService";
import accountService from "../services/accountService";

const SendPaymentCard = ({ onSuccess }) => {
    const [amount, setAmount] = useState("");
    const [recipientAccountNumber, setRecipientAccountNumber] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const { currentUser, selectedAccount, switchAccount } = useAuth();

    // Reset success when user edits inputs
    useEffect(() => {
        if (success) setSuccess(false);
        // eslint-disable-next-line
    }, [recipientAccountNumber, amount]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setSuccess(false);

        try {
            const account = await accountService.getAccountByAccountNumber(recipientAccountNumber);

            if (!account || !account.id) {
                setError("Recipient account not found...");
                return;
            }

            const paymentData = {
                sourceAccountId: selectedAccount.id,
                destinationAccountId: account.id,
                amount: amount,
                currency: 'USD'
            }

            await paymentService.sendMoney(currentUser?.id, paymentData);
            setAmount("");
            setRecipientAccountNumber("");
            setSuccess(true);
            if (onSuccess) onSuccess();
        } catch (err) {
            setError("Failed to send payment.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-gray-900 p-6 rounded-lg shadow-lg border border-gray-800 w-full">
            <form onSubmit={handleSubmit} className="flex items-center gap-3 w-full flex-1">
                <span className="text-lg font-semibold text-gray-100 whitespace-nowrap">Send Payment</span>
                <input
                    className="rounded border border-gray-700 bg-gray-800 px-3 py-2 text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500 flex-1 min-w-0"
                    value={recipientAccountNumber}
                    onChange={e => setRecipientAccountNumber(e.target.value)}
                    placeholder="Recipient Account Number"
                    required
                />
                <div className="relative basis-2/5 min-w-0 max-w-[120px]">
                    <span className="absolute left-2 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none text-base">$</span>
                    <input
                        type="number"
                        className="pl-7 rounded border border-gray-700 bg-gray-800 px-3 py-2 text-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
                        value={amount}
                        onChange={e => setAmount(e.target.value)}
                        placeholder="Amount"
                        min="0.01"
                        step="0.01"
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded transition-colors disabled:opacity-50 whitespace-nowrap"
                    disabled={loading}
                >
                    {loading ? "Sending..." : "Send Payment"}
                </button>
            </form>
            {error && <div className="text-red-400 text-sm mt-2">{error}</div>}
            {success && <div className="text-green-400 text-sm mt-2">Payment sent!</div>}
        </div>
    );
};

export default SendPaymentCard;
