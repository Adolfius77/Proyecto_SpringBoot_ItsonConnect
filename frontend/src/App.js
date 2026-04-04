/**
 * Componente principal de la aplicación
 */
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider } from './context/AuthContext';

// Importa tus componentes aquí cuando los crees
// import Login from './components/Auth/Login';
// import Register from './components/Auth/Register';
// import Home from './components/Home/Home';
// import Discovery from './components/Discovery/Discovery';
// import Chat from './components/Chat/Chat';
// import Matches from './components/Matches/Matches';
// import Profile from './components/Profile/Profile';

// Componente temporal de bienvenida
function Welcome() {
  return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column',
      justifyContent: 'center', 
      alignItems: 'center', 
      height: '100vh',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <h1 style={{ color: 'white', fontSize: '48px', marginBottom: '20px' }}>
        ❤️ ItsonConnect
      </h1>
      <p style={{ color: 'white', fontSize: '20px', textAlign: 'center', maxWidth: '600px' }}>
        Frontend React configurado correctamente ✅
      </p>
      <p style={{ color: 'white', fontSize: '16px', marginTop: '20px' }}>
        Ahora puedes empezar a programar tus componentes
      </p>
      <div style={{ 
        marginTop: '40px', 
        background: 'rgba(255,255,255,0.2)', 
        padding: '20px', 
        borderRadius: '10px'
      }}>
        <p style={{ color: 'white', margin: '5px 0' }}>📁 Crea tus componentes en: src/components/</p>
        <p style={{ color: 'white', margin: '5px 0' }}>🔧 API configurada: src/services/apiService.js</p>
        <p style={{ color: 'white', margin: '5px 0' }}>🔌 WebSocket listo: src/services/websocketService.js</p>
        <p style={{ color: 'white', margin: '5px 0' }}>🔐 Auth Context: src/context/AuthContext.js</p>
      </div>
    </div>
  );
}

// Tema personalizado de Material-UI
const theme = createTheme({
  palette: {
    primary: {
      main: '#667eea',
      light: '#b3ccff',
      dark: '#764ba2',
    },
    secondary: {
      main: '#66ccff',
    },
    background: {
      default: '#f5f5f5',
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: '"Segoe UI", "Roboto", "Helvetica", "Arial", sans-serif',
  },
  shape: {
    borderRadius: 12,
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <Routes>
            {/* Ruta temporal de bienvenida */}
            <Route path="/" element={<Welcome />} />
            
            {/* Cuando crees tus componentes, descomenta estas rutas */}
            {/* <Route path="/login" element={<Login />} /> */}
            {/* <Route path="/register" element={<Register />} /> */}
            {/* <Route path="/home" element={<Home />} /> */}
            {/* <Route path="/discover" element={<Discovery />} /> */}
            {/* <Route path="/matches" element={<Matches />} /> */}
            {/* <Route path="/chat/:matchId" element={<Chat />} /> */}
            {/* <Route path="/profile" element={<Profile />} /> */}
            
            {/* Redireccionar rutas no encontradas */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
