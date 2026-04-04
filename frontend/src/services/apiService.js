/**
 * Servicio de API - Maneja todas las peticiones HTTP al backend
 */
import axios from 'axios';
import API_ENDPOINTS from '../config/api';

// Configurar axios con interceptores
const apiClient = axios.create({
  baseURL: API_ENDPOINTS.ESTUDIANTES.BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token JWT (si lo implementas)
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para manejar errores globalmente
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado o inválido
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

/**
 * Servicio de API con métodos para cada endpoint
 */
const apiService = {
  // ==================== ESTUDIANTES ====================
  
  /**
   * Login de estudiante
   */
  login: async (correo, password) => {
    const response = await apiClient.post(API_ENDPOINTS.ESTUDIANTES.LOGIN, {
      correo,
      password,
    });
    return response.data;
  },

  /**
   * Registrar nuevo estudiante
   */
  register: async (estudianteData) => {
    const response = await apiClient.post(API_ENDPOINTS.ESTUDIANTES.REGISTER, estudianteData);
    return response.data;
  },

  /**
   * Obtener estudiante por ID
   */
  getEstudianteById: async (id) => {
    const response = await apiClient.get(API_ENDPOINTS.ESTUDIANTES.GET_BY_ID(id));
    return response.data;
  },

  /**
   * Actualizar perfil de estudiante
   */
  updateEstudiante: async (id, estudianteData) => {
    const response = await apiClient.put(API_ENDPOINTS.ESTUDIANTES.UPDATE(id), estudianteData);
    return response.data;
  },

  /**
   * Descubrir nuevos estudiantes
   */
  descubrirEstudiantes: async (estudianteId) => {
    const response = await apiClient.get(`${API_ENDPOINTS.ESTUDIANTES.DESCUBRIR}?estudianteId=${estudianteId}`);
    return response.data;
  },

  /**
   * Obtener matches de un estudiante
   */
  getMatches: async (estudianteId) => {
    const response = await apiClient.get(API_ENDPOINTS.ESTUDIANTES.MATCHES(estudianteId));
    return response.data;
  },

  // ==================== INTERACCIONES ====================

  /**
   * Crear interacción (LIKE, SUPERLIKE, PASS)
   */
  crearInteraccion: async (emisorId, receptorId, tipo) => {
    const response = await apiClient.post(API_ENDPOINTS.INTERACCIONES.CREATE, {
      emisor: { id: emisorId },
      receptor: { id: receptorId },
      tipo,
    });
    return response.data;
  },

  // ==================== MATCHES ====================

  /**
   * Obtener mensajes de un match
   */
  getMensajesMatch: async (matchId) => {
    const response = await apiClient.get(API_ENDPOINTS.MATCHES.MENSAJES(matchId));
    return response.data;
  },

  // ==================== HOBBIES ====================

  /**
   * Obtener todos los hobbies
   */
  getHobbies: async () => {
    const response = await apiClient.get(API_ENDPOINTS.HOBBIES.BASE);
    return response.data;
  },

  // ==================== CARRERAS ====================

  /**
   * Obtener todas las carreras
   */
  getCarreras: async () => {
    const response = await apiClient.get(API_ENDPOINTS.CARRERAS.NOMBRES);
    return response.data;
  },
};

export default apiService;
