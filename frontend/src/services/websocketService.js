class WebSocketService {
    constructor() {
        this.socket = null;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 3000;
    }

    // websocket connection
    connect(url, onMessageCallback, onErrorCallback) {
        try {
            this.socket = new WebSocket(url);

            // successful connection
            this.socket.onopen = () => {
                console.log('websocket connected to: ', url);
                this.reconnectAttempts = 0;
            }

            // new payment arrives
            this.socket.onmessage = (event) => {
                try {
                    const dataReceived = JSON.parse(event.data);
                    console.log('payment received: ', data);
                    onMessageCallback(data);
                } catch (error) {
                    console.error('Error parsing websocket message: ', error);
                }
            }

            // error
            this.socket.onerror = (error) => {
                console.error('websocket error: ', error);
                if (onErrorCallback) {
                    onErrorCallback(error);
                }
            }

            // connection closes, reconnect attempt
            this.socket.onclose = () => {
                console.log('websocket has disconnected');
                this.attemptReconnect(url, onMessageCallback, onErrorCallback);
            };
        } catch (error) {
            console.error('failed to create websocket connection', error);
        }
    }

    // helper method to attempt reconnecting
    attemptReconnect(url, onMessageCallback, onErrorCallback) {
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(`attempting to reconnect, attempt (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)

            setTimeout(() => {
                this.connect(url, onMessageCallback, onErrorCallback);
            }, this.reconnectDelay);
        } else {
            console.error('max reconnect attempts reached. please refresh the page.')
        }
    }

    // disconnect
    disconnect() {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
            console.log('websocket has been manually disconnected')
        }
    }

    send(data) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(data));
        } else {
            console.error('websocket is not connected, cannot send any data.');
        }
    }
}

export default new WebSocketService();