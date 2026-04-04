# 🚀 GUÍA DE INICIO - Frontend React

## 📋 Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/        # 👉 AQUÍ CREAS TUS COMPONENTES
│   │   ├── Auth/          # Login, Register
│   │   ├── Home/          # Pantalla principal
│   │   ├── Discovery/     # Descubrir estudiantes (swipe)
│   │   ├── Chat/          # Chat en tiempo real
│   │   ├── Matches/       # Lista de matches
│   │   └── Profile/       # Perfil de usuario
│   │
│   ├── services/          # ✅ YA CONFIGURADO
│   │   ├── apiService.js       # Llamadas a la API REST
│   │   └── websocketService.js # WebSocket para chat
│   │
│   ├── context/           # ✅ YA CONFIGURADO
│   │   └── AuthContext.js      # Estado global de autenticación
│   │
│   ├── config/            # ✅ YA CONFIGURADO
│   │   └── api.js              # URLs del backend
│   │
│   ├── App.js             # ✅ YA CONFIGURADO (rutas principales)
│   └── index.js           # Punto de entrada
│
├── .env                   # ✅ YA CONFIGURADO (variables de entorno)
└── package.json           # Dependencias instaladas
```

---

## 🎯 Dependencias Instaladas

| Librería | Uso |
|----------|-----|
| **react-router-dom** | Navegación entre páginas |
| **@mui/material** | Componentes UI modernos (Material Design) |
| **@mui/icons-material** | Iconos Material Design |
| **axios** | Peticiones HTTP al backend |
| **sockjs-client** | WebSocket para chat en tiempo real |
| **@stomp/stompjs** | Protocolo STOMP para WebSocket |

---

## ⚡ Comandos Disponibles

```bash
# Iniciar servidor de desarrollo (puerto 3000)
npm start

# Crear build de producción
npm run build

# Ejecutar tests
npm test

# Ver todas las dependencias
npm list
```

---

## 🔧 Archivos Ya Configurados

### 1️⃣ **API Service** (`src/services/apiService.js`)
```javascript
import apiService from './services/apiService';

// Login
const result = await apiService.login(correo, password);

// Registro
const result = await apiService.register(estudianteData);

// Descubrir estudiantes
const estudiantes = await apiService.descubrirEstudiantes(miId);

// Crear interacción (LIKE, SUPERLIKE, PASS)
await apiService.crearInteraccion(miId, otroId, 'LIKE');

// Obtener matches
const matches = await apiService.getMatches(miId);

// Obtener mensajes de un match
const mensajes = await apiService.getMensajesMatch(matchId);

// Obtener hobbies
const hobbies = await apiService.getHobbies();

// Obtener carreras
const carreras = await apiService.getCarreras();
```

### 2️⃣ **WebSocket Service** (`src/services/websocketService.js`)
```javascript
import websocketService from './services/websocketService';

// Conectar al WebSocket
websocketService.connect(
  () => console.log('Conectado'),
  (error) => console.error('Error', error)
);

// Suscribirse a mensajes de un match
websocketService.subscribeToMatch(matchId, (mensaje) => {
  console.log('Nuevo mensaje:', mensaje);
});

// Enviar mensaje
websocketService.sendMessage(matchId, {
  contenido: 'Hola!',
  emisor: { id: miId },
  match: { id: matchId }
});

// Desconectar
websocketService.disconnect();
```

### 3️⃣ **Auth Context** (`src/context/AuthContext.js`)
```javascript
import { useAuth } from './context/AuthContext';

function MiComponente() {
  const { user, login, logout, isAuthenticated } = useAuth();

  const handleLogin = async () => {
    const result = await login(email, password);
    if (result.success) {
      // Login exitoso
    }
  };

  return (
    <div>
      {isAuthenticated ? (
        <p>Hola {user.nombre}</p>
      ) : (
        <button onClick={handleLogin}>Login</button>
      )}
    </div>
  );
}
```

---

## 📁 Cómo Crear tus Componentes

### Ejemplo 1: Login Component

```bash
# Crear carpeta Auth
mkdir src/components/Auth
```

**`src/components/Auth/Login.jsx`:**
```javascript
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { TextField, Button, Card, CardContent } from '@mui/material';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await login(email, password);
    if (result.success) {
      navigate('/home');
    } else {
      alert(result.error);
    }
  };

  return (
    <Card sx={{ maxWidth: 400, margin: 'auto', mt: 5 }}>
      <CardContent>
        <h2>❤️ ItsonConnect</h2>
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Correo ITSON"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            margin="normal"
          />
          <TextField
            fullWidth
            type="password"
            label="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            margin="normal"
          />
          <Button fullWidth variant="contained" type="submit" sx={{ mt: 2 }}>
            Iniciar Sesión
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}

export default Login;
```

### Ejemplo 2: Discovery Component (Swipe Cards)

**`src/components/Discovery/Discovery.jsx`:**
```javascript
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import apiService from '../../services/apiService';
import { Card, CardContent, CardMedia, Button } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import CloseIcon from '@mui/icons-material/Close';

function Discovery() {
  const { user } = useAuth();
  const [estudiantes, setEstudiantes] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    cargarEstudiantes();
  }, []);

  const cargarEstudiantes = async () => {
    const data = await apiService.descubrirEstudiantes(user.id);
    setEstudiantes(data);
  };

  const handleLike = async () => {
    const estudiante = estudiantes[currentIndex];
    await apiService.crearInteraccion(user.id, estudiante.id, 'LIKE');
    setCurrentIndex(currentIndex + 1);
  };

  const handlePass = async () => {
    const estudiante = estudiantes[currentIndex];
    await apiService.crearInteraccion(user.id, estudiante.id, 'PASS');
    setCurrentIndex(currentIndex + 1);
  };

  const estudianteActual = estudiantes[currentIndex];

  if (!estudianteActual) {
    return <p>No hay más estudiantes por descubrir</p>;
  }

  return (
    <div style={{ textAlign: 'center', padding: '20px' }}>
      <Card sx={{ maxWidth: 400, margin: 'auto' }}>
        <CardMedia
          component="img"
          height="400"
          image={`data:image/jpeg;base64,${estudianteActual.foto}`}
          alt={estudianteActual.nombre}
        />
        <CardContent>
          <h2>{estudianteActual.nombre} {estudianteActual.apPaterno}</h2>
          <p>{estudianteActual.carrera}</p>
        </CardContent>
      </Card>
      
      <div style={{ marginTop: '20px' }}>
        <Button
          variant="contained"
          color="error"
          onClick={handlePass}
          sx={{ mr: 2 }}
        >
          <CloseIcon /> PASS
        </Button>
        <Button
          variant="contained"
          color="success"
          onClick={handleLike}
        >
          <FavoriteIcon /> LIKE
        </Button>
      </div>
    </div>
  );
}

export default Discovery;
```

---

## 🎨 Material-UI (MUI) - Componentes Disponibles

Ya tienes Material-UI instalado. Usa estos componentes:

```javascript
import {
  Button,
  TextField,
  Card,
  CardContent,
  Avatar,
  AppBar,
  Toolbar,
  IconButton,
  Drawer,
  List,
  ListItem,
  Dialog,
  Snackbar,
  Grid,
  Box,
  Typography
} from '@mui/material';

import {
  Favorite,
  Close,
  Chat,
  Person,
  Logout,
  Send
} from '@mui/icons-material';
```

📚 **Documentación:** https://mui.com/material-ui/getting-started/

---

## 🔄 Flujo de la Aplicación

```
1. Usuario abre http://localhost:3000
2. Ve pantalla de Login/Register
3. Al hacer login, se guarda en AuthContext
4. Redirige a /home
5. Desde home puede ir a:
   - /discover (descubrir estudiantes)
   - /matches (ver matches)
   - /chat/:matchId (chatear)
   - /profile (ver perfil)
```

---

## 🚀 Próximos Pasos

### 1. Crear componentes básicos:
```bash
src/components/Auth/Login.jsx
src/components/Auth/Register.jsx
src/components/Home/Home.jsx
src/components/Discovery/Discovery.jsx
src/components/Matches/Matches.jsx
src/components/Chat/Chat.jsx
src/components/Profile/Profile.jsx
```

### 2. Descomentar rutas en `App.js`

### 3. Iniciar servidor de desarrollo:
```bash
cd frontend
npm start
```

### 4. Verificar que el backend esté corriendo:
```bash
# En otra terminal
cd ..
mvnw spring-boot:run
```

---

## 🐛 Troubleshooting

### Error de CORS
✅ **Ya configurado** en `backend/src/main/java/Config/CorsConfig.java`

### Backend no responde
- Verifica que Spring Boot esté corriendo en puerto 8080
- Verifica MySQL esté corriendo

### WebSocket no conecta
- Verifica la URL en `.env`: `REACT_APP_API_URL=http://localhost:8080`

---

## 📞 Ayuda Rápida

- **API Service:** Todas las peticiones HTTP ya están configuradas
- **WebSocket:** Chat en tiempo real ya está configurado
- **Auth:** Login/Register/Logout ya está configurado
- **Rutas:** React Router ya está configurado
- **Material-UI:** Componentes UI ya están instalados

**¡Solo necesitas crear tus componentes y usar lo que ya está configurado!** 🎉
