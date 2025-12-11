import { Client } from '@stomp/stompjs';

class WebSocketService {
  constructor() {
    this.client = null;
    this.subscription = null;
  }

  connect(customerId, onMessage, onError) {
    // Create STOMP client with native WebSocket (no SockJS)
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      
      connectHeaders: {},
      
      debug: (str) => {
        console.log('STOMP Debug:', str);
      },
      
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      onConnect: (frame) => {
        console.log('STOMP Connected:', frame);
        
        // Subscribe to customer-specific payment updates
        this.subscription = this.client.subscribe(
          `/topic/payments/${customerId}`,
          (message) => {
            try {
              const payment = JSON.parse(message.body);
              console.log('Received payment:', payment);
              onMessage(payment);
            } catch (error) {
              console.error('Error parsing payment message:', error);
              onError(error);
            }
          }
        );
        
        console.log(`Subscribed to /topic/payments/${customerId}`);
      },

      onStompError: (frame) => {
        console.error('STOMP Error:', frame);
        onError(new Error(frame.headers.message || 'WebSocket error'));
      },

      onWebSocketError: (event) => {
        console.error('WebSocket Error:', event);
        onError(new Error('WebSocket connection error'));
      },

      onDisconnect: () => {
        console.log('STOMP Disconnected');
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
    
    console.log('WebSocket disconnected');
  }

  send(destination, message) {
    if (this.client && this.client.connected) {
      this.client.publish({
        destination: destination,
        body: JSON.stringify(message)
      });
    } else {
      console.error('Cannot send message: WebSocket not connected');
    }
  }
}

export default new WebSocketService();