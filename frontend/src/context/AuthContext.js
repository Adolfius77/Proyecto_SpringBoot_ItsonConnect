/**
 * Contexto de Autenticación
 * Maneja el estado global del usuario autenticado
 */
import React, { createContext, useState, useContext, useEffect } from 'react';
import apiService from '../services/apiService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Verificar si hay usuario guardado al iniciar
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  /**
   * Login de usuario
   */
  const login = async (correo, password) => {
    try {
      const userData = await apiService.login(correo, password);
      setUser(userData);
      localStorage.setItem('user', JSON.stringify(userData));
      return { success: true, user: userData };
    } catch (error) {
      console.error('Error en login:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || 'Error al iniciar sesión' 
      };
    }
  };

  /**
   * Registro de nuevo usuario
   */
  const register = async (estudianteData) => {
    try {
      const userData = await apiService.register(estudianteData);
      setUser(userData);
      localStorage.setItem('user', JSON.stringify(userData));
      return { success: true, user: userData };
    } catch (error) {
      console.error('Error en registro:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || 'Error al registrarse' 
      };
    }
  };

  /**
   * Logout de usuario
   */
  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  /**
   * Actualizar datos del usuario
   */
  const updateUser = (newUserData) => {
    setUser(newUserData);
    localStorage.setItem('user', JSON.stringify(newUserData));
  };

  const value = {
    user,
    loading,
    login,
    register,
    logout,
    updateUser,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Hook para usar el contexto de autenticación
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider');
  }
  return context;
};

export default AuthContext;
