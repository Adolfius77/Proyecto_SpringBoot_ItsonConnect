/**
 * Configuración de la API
 * Centraliza las URLs del backend para facilitar cambios
 */

// URL base del backend Spring Boot
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Endpoints de la API
export const API_ENDPOINTS = {
  // Autenticación y Estudiantes
  ESTUDIANTES: {
    BASE: `${API_BASE_URL}/api/estudiantes`,
    LOGIN: `${API_BASE_URL}/api/estudiantes/login`,
    REGISTER: `${API_BASE_URL}/api/estudiantes`,
    GET_BY_ID: (id) => `${API_BASE_URL}/api/estudiantes/${id}`,
    UPDATE: (id) => `${API_BASE_URL}/api/estudiantes/${id}`,
    DELETE: (id) => `${API_BASE_URL}/api/estudiantes/${id}`,
    DESCUBRIR: `${API_BASE_URL}/api/estudiantes/descubrir`,
    MATCHES: (id) => `${API_BASE_URL}/api/estudiantes/${id}/matches`,
  },
  
  // Matches
  MATCHES: {
    BASE: `${API_BASE_URL}/api/matches`,
    GET_BY_ID: (id) => `${API_BASE_URL}/api/matches/${id}`,
    CREATE: `${API_BASE_URL}/api/matches`,
    UPDATE: (id) => `${API_BASE_URL}/api/matches/${id}`,
    DELETE: (id) => `${API_BASE_URL}/api/matches/${id}`,
    MENSAJES: (matchId) => `${API_BASE_URL}/api/matches/${matchId}/mensajes`,
  },
  
  // Interacciones
  INTERACCIONES: {
    BASE: `${API_BASE_URL}/api/interacciones`,
    CREATE: `${API_BASE_URL}/api/interacciones`,
  },
  
  // Hobbies
  HOBBIES: {
    BASE: `${API_BASE_URL}/api/hobbies`,
    GET_BY_ID: (id) => `${API_BASE_URL}/api/hobbies/${id}`,
  },
  
  // Carreras
  CARRERAS: {
    BASE: `${API_BASE_URL}/api/carreras`,
    NOMBRES: `${API_BASE_URL}/api/carreras/nombres`,
  },
};

// WebSocket URL
export const WS_URL = `${API_BASE_URL}/itson-connect-ws`;

export default API_ENDPOINTS;
