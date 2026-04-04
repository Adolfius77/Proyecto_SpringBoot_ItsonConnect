/**
 * Servicio de WebSocket para chat en tiempo real
 */
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { WS_URL } from '../config/api';

class WebSocketService {
  constructor() {
    this.stompClient = null;
    this.connected = false;
    this.subscriptions = new Map();
  }

  /**
   * Conectar al servidor WebSocket
   */
  connect(onConnected, onError) {
    const socket = new SockJS(WS_URL);
    
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      
      onConnect: () => {
        console.log('✅ WebSocket conectado');
        this.connected = true;
        if (onConnected) onConnected();
      },
      
      onStompError: (frame) => {
        console.error('❌ Error STOMP:', frame);
        this.connected = false;
        if (onError) onError(frame);
      },
      
      onWebSocketClose: () => {
        console.warn('⚠️ WebSocket cerrado');
        this.connected = false;
      },
    });

    this.stompClient.activate();
  }

  /**
   * Desconectar del servidor WebSocket
   */
  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.connected = false;
      this.subscriptions.clear();
      console.log('🔌 WebSocket desconectado');
    }
  }

  /**
   * Suscribirse a un topic de match
   */
  subscribeToMatch(matchId, callback) {
    if (!this.connected) {
      console.error('❌ No hay conexión WebSocket');
      return null;
    }

    const topic = `/topic/match/${matchId}`;
    
    const subscription = this.stompClient.subscribe(topic, (message) => {
      const payload = JSON.parse(message.body);
      callback(payload);
    });

    this.subscriptions.set(matchId, subscription);
    console.log(`📡 Suscrito al match: ${matchId}`);
    
    return subscription;
  }

  /**
   * Cancelar suscripción a un match
   */
  unsubscribeFromMatch(matchId) {
    const subscription = this.subscriptions.get(matchId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(matchId);
      console.log(`🔕 Desuscrito del match: ${matchId}`);
    }
  }

  /**
   * Enviar mensaje a un match
   */
  sendMessage(matchId, mensaje) {
    if (!this.connected) {
      console.error('❌ No hay conexión WebSocket');
      return false;
    }

    this.stompClient.publish({
      destination: `/app/chat/${matchId}`,
      body: JSON.stringify(mensaje),
    });

    console.log(`📤 Mensaje enviado al match ${matchId}`);
    return true;
  }

  /**
   * Verificar si está conectado
   */
  isConnected() {
    return this.connected;
  }
}

// Exportar una única instancia (Singleton)
const websocketService = new WebSocketService();
export default websocketService;
