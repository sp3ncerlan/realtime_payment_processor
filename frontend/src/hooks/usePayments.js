import { useCallback, useState, useEffect } from "react";
import paymentService from "../services/paymentService";
import websocketService from "../services/websocketService";

export const usePayments = (accountId, maxPayments = 50) => {
    const [payments, SetPayments] = useState([]);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    const handleNewPayment = useCallback((newPayment) => {
        SetPayments((prevPayments) => {
            const updated = [newPayment, ...prevPayments]
            return updated.slice(0, maxPayments);
        })
    
        setError(null);
    }, [maxPayments])

    const handleWebSocketError = useCallback((error) => {
        setError('Connection lost, attempting to reconnect...');
        setIsConnected(false);
    }, []);

    const handleConnectionStatus = useCallback((connected) => {
        setIsConnected(connected);
    }, [])

    useEffect(() => {
        if (!accountId) {
            setIsLoading(false);
            return;
        }

        const initializePayments = async () => {
            try {
                setIsLoading(true);

                const initialPayments = await paymentService.getCustomerPayments(accountId);
                console.log(initialPayments);
                SetPayments(initialPayments);

                websocketService.connect(
                    accountId,
                    handleNewPayment,
                    handleWebSocketError,
                    handleConnectionStatus
                );

                setIsLoading(false);
            } catch (err) {
                setError('Failed to load payments, please refresh the page.');
                setIsLoading(false);
            }
        }

        initializePayments();

        return () => {
            websocketService.disconnect();
            setIsConnected(false);
        }
    }, [accountId, handleNewPayment, handleWebSocketError, handleConnectionStatus]);

    return {
        payments,
        totalPayments: payments.length,
        totalPendingPayments: payments.filter(payment => payment.status === 'PENDING').length,
        isConnected,
        isLoading,
        error,
    }
}