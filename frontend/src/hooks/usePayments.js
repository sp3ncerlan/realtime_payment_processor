import { useCallback, useState, useEffect } from "react";
import paymentService from "../services/paymentService";
import websocketService from "../services/websocketService";

export const usePayments = (customerId, maxPayments = 50) => {
    const [payments, SetPayments] = useState([]);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    if (!customerId) {
        throw new Error('customerId is required for usePayments hook')
    }

    const handleNewPayment = useCallback((newPayment) => {
        console.log('adding new payment to list: ', newPayment);

        SetPayments((prevPayments) => {
            const updated = [newPayment, ...prevPayments]
            return updated.slice(0, maxPayments);
        })
        
        setIsConnected(true);
        setError(null);
    }, [maxPayments])

    const handleWebSocketError = useCallback((error) => {
        console.error('websocket error: ', error)
        setError('Connection lost, attempting to reconnect...');
        setIsConnected(false);
    }, []);

    useEffect(() => {
        const initializePayments = async () => {
            try {
                setIsLoading(true);
                console.log('Loading initial payments for customer:', customerId)

                // Fetch payments from REST API
                const initialPayments = await paymentService.getCustomerPayments(customerId);
                console.log('Initial payments loaded:', initialPayments);
                SetPayments(initialPayments);

                // Connect to STOMP WebSocket for real-time updates
                console.log('Connecting to STOMP WebSocket for customer:', customerId)
                websocketService.connect(
                    customerId,
                    handleNewPayment,
                    handleWebSocketError
                );

                setIsLoading(false);
            } catch (err) {
                console.error('Failed to initialize payments:', err)
                setError('Failed to load payments, please refresh the page.');
                setIsLoading(false);
            }
        }

        initializePayments();

        return () => {
            console.log('Cleaning up websocket connection');
            websocketService.disconnect();
        }
    }, [customerId, handleNewPayment, handleWebSocketError]);

    return {
        payments,
        isConnected,
        isLoading,
        error,
    }
}