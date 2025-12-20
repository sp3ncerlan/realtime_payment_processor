import { Client } from '@stomp/stompjs';

class WebSocketService {
    constructor() {
        this.client = null;
        this.subscription = null;
    }

    connect(accountId, onMessage, onError, onConnectionChange) {
        this.client = new Client({
            brokerURL: 'ws://localhost:8080/ws',

            connectHeaders: {},

            debug: () => {},

            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,

            onConnect: (frame) => {
                if (!this.client || !this.client.connected) {
                    return;
                }
                
                if (onConnectionChange) {
                    onConnectionChange(true);
                }

                this.subscription = this.client.subscribe(
                    `/topic/payments/${accountId}`,
                    (message) => {
                        try {
                            const payment = JSON.parse(message.body);
                            console.log("WebSocket received new payment:", payment);
                            onMessage(payment);
                        } catch (error) {
                            onError(error);
                        }
                    }
                );
            },

            onStompError: (frame) => {
                onError(new Error(frame.headers.message || 'WebSocket error'));
            },

            onWebSocketError: (event) => {
                onError(new Error('WebSocket connection error'));
            },

            onDisconnect: () => {
                if (onConnectionChange) {
                    onConnectionChange(false);
                }
            }
        });

        this.client.activate();
    }

    disconnect() {
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }

        if (this.client) {
            this.client.deactivate();
            this.client = null;
        }
    }

    send(destination, message) {
        if (this.client && this.client.connected) {
            this.client.publish({
                destination: destination,
                body: JSON.stringify(message)
            });
        }
    }
}

export default new WebSocketService();