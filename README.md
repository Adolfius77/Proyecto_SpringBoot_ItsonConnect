# 🎓 ItsonConnect

**ItsonConnect** es una plataforma de conexión social para estudiantes del ITSON (Instituto Tecnológico de Sonora), desarrollada con Spring Boot y WebSockets. Similar a aplicaciones de matching como Tinder, permite a los estudiantes descubrir, conectar y chatear en tiempo real con otros estudiantes que compartan intereses similares.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.8-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-purple)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
  - [Opción 1: Ejecución Local](#opción-1-ejecución-local)
  - [Opción 2: Ejecución con Docker](#opción-2-ejecución-con-docker)
  - [Opción 3: Cliente-Servidor (Red Local)](#opción-3-cliente-servidor-red-local)
- [API Endpoints](#-api-endpoints)
- [WebSocket - Chat en Tiempo Real](#-websocket---chat-en-tiempo-real)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Modelos de Datos](#-modelos-de-datos)
- [Contribuir](#-contribuir)

---

## ✨ Características

### 🔐 **Autenticación y Perfiles**
- ✅ Registro de estudiantes con validación de correo institucional (`@potros.itson.edu.mx`, `@itson.mx`)
- ✅ Login seguro con contraseñas encriptadas (BCrypt)
- ✅ Gestión de perfiles con foto (almacenamiento Base64)
- ✅ Selección de carrera y hobbies personalizados

### 🔍 **Sistema de Descubrimiento y Matching**
- ✅ Descubre nuevos estudiantes según intereses
- ✅ Interacciones tipo Tinder: **LIKE**, **SUPERLIKE**, **PASS**
- ✅ Match automático cuando hay interés mutuo
- ✅ Visualización de matches activos

### 💬 **Chat en Tiempo Real**
- ✅ Mensajería instantánea con WebSockets (STOMP + SockJS)
- ✅ Historial de conversaciones persistente
- ✅ Notificaciones en tiempo real
- ✅ Manejo de errores con colas individuales

### 🎯 **Gestión de Intereses**
- ✅ Sistema de hobbies precargados: Gaming, Música, Deportes, Viajes, Lectura, Cantar, Codificar
- ✅ Relación muchos-a-muchos entre estudiantes y hobbies
- ✅ Filtrado por intereses comunes

---

## 🛠️ Tecnologías Utilizadas

### **Backend**
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 21 | Lenguaje de programación principal |
| **Spring Boot** | 3.2.8 | Framework para aplicaciones Java |
| **Spring Data JPA** | 3.2.8 | Persistencia de datos |
| **Spring Security** | 3.2.8 | Seguridad y encriptación |
| **Spring WebSocket** | 3.2.8 | Comunicación en tiempo real |
| **Hibernate** | 6.x | ORM (Object-Relational Mapping) |
| **MapStruct** | 1.5.5 | Mapeo entre DTOs y entidades |
| **Maven** | 3.9+ | Gestión de dependencias |

### **Base de Datos**
| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **MySQL** | 8.0 | Base de datos relacional |
| **MySQL Connector/J** | Runtime | Driver JDBC para MySQL |

### **Otras Herramientas**
| Tecnología | Descripción |
|------------|-------------|
| **Gson** | Serialización/Deserialización JSON |
| **Docker** | Contenerización de la aplicación |
| **Docker Compose** | Orquestación de contenedores |

---

## 🏗️ Arquitectura del Proyecto

```
┌─────────────────────────────────────────────────────┐
│                  Capa de Presentación                │
│         (Controllers - REST API + WebSocket)         │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│              Capa de DTOs + Mappers                  │
│          (Data Transfer Objects - MapStruct)         │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│               Capa de Servicios                      │
│            (Lógica de Negocio - Services)            │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│            Capa de Repositorios                      │
│         (Acceso a Datos - JPA Repositories)          │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│                Capa de Persistencia                  │
│          (Entidades JPA + Base de Datos MySQL)       │
└─────────────────────────────────────────────────────┘
```

**Patrón de Diseño:** MVC (Model-View-Controller) + Service Layer

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

| Requisito | Versión Mínima | Verificación |
|-----------|----------------|--------------|
| **Java JDK** | 21 | `java -version` |
| **Maven** | 3.9+ | `mvn -version` |
| **MySQL** | 8.0+ | `mysql --version` |
| **Docker** (opcional) | 20.10+ | `docker --version` |
| **Docker Compose** (opcional) | 2.0+ | `docker-compose --version` |

---

## 🚀 Instalación y Configuración

### **Opción 1: Ejecución Local**

#### **1️⃣ Configurar la Base de Datos**

Abre MySQL Workbench o tu cliente MySQL preferido y ejecuta:

```sql
CREATE DATABASE ItsonConnectDB;
USE ItsonConnectDB;
```

#### **2️⃣ Configurar application.properties**

Navega a `src/main/resources/application.properties` y ajusta las credenciales:

```properties
spring.application.name=ItsonConnect

# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3307/ItsonConnectDB?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

> **Nota:** Cambia `spring.datasource.username` y `spring.datasource.password` según tu configuración local.

#### **3️⃣ Compilar y Ejecutar**

```bash
# Navegar al directorio del proyecto
cd C:\Users\USER\Documents\GitHub\Proyecto_SpringBoot_ItsonConnect

# Compilar el proyecto
mvnw clean install

# Ejecutar la aplicación
mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

### **Opción 2: Ejecución con Docker**

Esta opción configura automáticamente MySQL y la aplicación en contenedores.

#### **1️⃣ Construir y Levantar los Contenedores**

```bash
# Desde el directorio raíz del proyecto
docker-compose up --build
```

Esto creará dos contenedores:
- **itson_db**: MySQL 8.0 en puerto `3307`
- **itson_backend**: Spring Boot en puerto `8080`

#### **2️⃣ Verificar el Estado**

```bash
docker-compose ps
```

#### **3️⃣ Ver Logs**

```bash
# Logs de todos los servicios
docker-compose logs -f

# Logs solo del backend
docker-compose logs -f app
```

#### **4️⃣ Detener los Contenedores**

```bash
docker-compose down
```

**Acceso:**
- **Backend API:** http://localhost:8080
- **MySQL:** `localhost:3307` (usuario: `root`, contraseña: `1234`)

---

### **Opción 3: Cliente-Servidor (Red Local)**

Esta configuración permite ejecutar el backend en una computadora (servidor) y el cliente Java Swing en otra (cliente).

#### **📌 Computadora 1: Servidor (Backend + MySQL)**

##### **Paso 1: Configurar MySQL**

```sql
CREATE DATABASE ItsonConnectDB;
USE ItsonConnectDB;
```

##### **Paso 2: Ajustar application.properties**

```properties
spring.datasource.username=root
spring.datasource.password=Itson
```

##### **Paso 3: Obtener la IP del Servidor**

1. Presiona `Windows + R`, escribe `cmd` y presiona Enter
2. Ejecuta: `ipconfig`
3. Anota la **Dirección IPv4** (ejemplo: `192.168.0.109`)

##### **Paso 4: Configurar el Firewall**

1. Busca **"Firewall"** en Windows → Abre **"Windows Defender Firewall con seguridad avanzada"**
2. Clic en **"Reglas de entrada"** → **"Nueva regla..."**
3. **Tipo de Regla:** Puerto → Siguiente
4. **Protocolo:** TCP → **Puertos:** `8080, 3306` → Siguiente
5. **Acción:** Permitir la conexión → Siguiente
6. **Perfil:** Marcar todas (Dominio, Privado, Público) → Siguiente
7. **Nombre:** `ItsonConnect Puertos` → Finalizar

##### **Paso 5: Ejecutar el Backend**

```bash
mvnw clean install
mvnw spring-boot:run
```

---

#### **📌 Computadora 2: Cliente (Frontend Java Swing)**

##### **Paso 1: Configurar la URL del Servidor**

Edita `src/main/java/presentacion/ConfigCliente.java`:

```java
// Reemplaza con la IP de la Computadora 1
public static final String BASE_URL = "http://192.168.0.109:8080";
public static final String WS_URL = BASE_URL + "/itson-connect-ws";
```

##### **Paso 2: Compilar y Ejecutar**

1. Realiza un **"Clean and Build"** en el IDE
2. Busca la clase `presentacion.Main`
3. Clic derecho → **"Run File"**

---

## 📡 API Endpoints

### **🎓 Estudiantes**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/estudiantes` | Listar todos los estudiantes |
| `GET` | `/api/estudiantes/{id}` | Obtener estudiante por ID |
| `POST` | `/api/estudiantes` | Registrar nuevo estudiante |
| `PUT` | `/api/estudiantes/{id}` | Actualizar perfil de estudiante |
| `DELETE` | `/api/estudiantes/{id}` | Eliminar estudiante |
| `POST` | `/api/estudiantes/login` | Login de estudiante |
| `GET` | `/api/estudiantes/descubrir` | Descubrir nuevos estudiantes |
| `GET` | `/api/estudiantes/{id}/matches` | Obtener matches del estudiante |

### **💕 Matches**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/matches` | Listar todos los matches |
| `GET` | `/api/matches/{id}` | Obtener match por ID |
| `POST` | `/api/matches` | Crear nuevo match |
| `PUT` | `/api/matches/{id}` | Actualizar match |
| `DELETE` | `/api/matches/{id}` | Eliminar match |
| `GET` | `/api/matches/{matchId}/mensajes` | Historial de mensajes del match |

### **🎯 Interacciones**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/interacciones` | Listar todas las interacciones |
| `GET` | `/api/interacciones/{id}` | Obtener interacción por ID |
| `POST` | `/api/interacciones` | Registrar interacción (LIKE/SUPERLIKE/PASS) |
| `PUT` | `/api/interacciones/{id}` | Actualizar interacción |
| `DELETE` | `/api/interacciones/{id}` | Eliminar interacción |

### **🎨 Hobbies**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/hobbies` | Listar todos los hobbies |
| `GET` | `/api/hobbies/{id}` | Obtener hobby por ID |
| `POST` | `/api/hobbies` | Crear nuevo hobby |
| `PUT` | `/api/hobbies/{id}` | Actualizar hobby |
| `DELETE` | `/api/hobbies/{id}` | Eliminar hobby |

### **📚 Carreras**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/carreras` | Listar todas las carreras (paginado) |
| `GET` | `/api/carreras/nombres` | Obtener nombres de todas las carreras |

---

## 🔌 WebSocket - Chat en Tiempo Real

### **Configuración del Cliente**

```javascript
// Conectar al WebSocket
const socket = new SockJS('http://localhost:8080/itson-connect-ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Conectado: ' + frame);
    
    // Suscribirse al topic del match
    stompClient.subscribe('/topic/match/' + matchId, function(message) {
        const msg = JSON.parse(message.body);
        console.log('Mensaje recibido:', msg);
    });
});
```

### **Enviar Mensajes**

```javascript
// Enviar mensaje al match
stompClient.send("/app/chat/" + matchId, {}, JSON.stringify({
    'contenido': 'Hola, ¿cómo estás?',
    'emisor': { 'id': estudianteId },
    'match': { 'id': matchId }
}));
```

### **Endpoints WebSocket**

| Ruta | Descripción |
|------|-------------|
| `/itson-connect-ws` | Endpoint de conexión WebSocket (SockJS) |
| `/app/chat/{matchId}` | Enviar mensaje a un match específico |
| `/topic/match/{matchId}` | Suscripción para recibir mensajes del match |
| `/user/{username}/queue/errors` | Cola de errores individual |

---

## 📁 Estructura del Proyecto

```
Proyecto_SpringBoot_ItsonConnect/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/mycompany/itsonconnect/
│   │   │   │   └── ItsonConnectApplication.java    # Clase principal
│   │   │   ├── Config/
│   │   │   │   ├── SecurityConfig.java              # Configuración de seguridad
│   │   │   │   └── WebsocketConfig.java             # Configuración WebSocket
│   │   │   ├── controller/
│   │   │   │   ├── CarreraController.java
│   │   │   │   ├── ChatController.java              # WebSocket controller
│   │   │   │   ├── EstudianteController.java
│   │   │   │   ├── HobbyController.java
│   │   │   │   ├── InteraccionController.java
│   │   │   │   ├── MatchController.java
│   │   │   │   └── MatchEstudianteController.java
│   │   │   ├── dto/                                 # Data Transfer Objects
│   │   │   ├── Mappers/                             # MapStruct mappers
│   │   │   ├── model/
│   │   │   │   ├── Carrera.java
│   │   │   │   ├── Estudiante.java
│   │   │   │   ├── Hobby.java
│   │   │   │   ├── HobbyEstudiante.java
│   │   │   │   ├── Interaccion.java
│   │   │   │   ├── Match.java
│   │   │   │   ├── MatchEstudiante.java
│   │   │   │   └── Mensaje.java
│   │   │   ├── Repository/                          # JPA Repositories
│   │   │   ├── service/                             # Interfaces de servicios
│   │   │   └── presentacion/                        # Cliente Java Swing
│   │   └── resources/
│   │       ├── application.properties               # Configuración principal
│   │       └── *.png                                # Recursos gráficos
│   └── test/                                        # Tests unitarios
│
├── target/                                          # Archivos compilados
├── .mvn/                                            # Maven wrapper
├── config.properties                                # Configuración del servidor
├── docker-compose.yml                               # Orquestación Docker
├── Dockerfile                                       # Imagen Docker
├── pom.xml                                          # Dependencias Maven
├── mvnw / mvnw.cmd                                  # Maven wrapper scripts
└── README.md                                        # Este archivo
```

---

## 🗄️ Modelos de Datos

### **Relaciones entre Entidades**

```
┌──────────────┐
│  Estudiante  │────┬─────────────┐
└──────────────┘    │             │
        │           │             │
        │ 1:N       │ 1:N         │ 1:N
        │           │             │
┌───────▼──────┐ ┌──▼────────┐ ┌─▼──────────────┐
│ Interaccion  │ │   Mensaje │ │ HobbyEstudiante│
└──────────────┘ └───────────┘ └────────────────┘
                      │               │
                      │ N:1           │ N:1
                      │               │
                ┌─────▼──────┐  ┌─────▼──────┐
                │   Match    │  │   Hobby    │
                └────────────┘  └────────────┘
                      │
                      │ 1:N
                      │
                ┌─────▼──────────────┐
                │ MatchEstudiante    │
                └────────────────────┘
```

### **Entidades Principales**

#### **Estudiante**
```java
- id (Long, PK)
- nombre (String)
- apPaterno (String)
- apMaterno (String)
- correo (String, unique) // Validado: @potros.itson.edu.mx o @itson.mx
- password (String)       // Encriptado con BCrypt
- carrera (String)
- genero (String)
- fechaRegistro (LocalDateTime)
- foto (byte[])           // Almacenado como BLOB
```

#### **Match**
```java
- id (Long, PK)
- fecha (LocalDateTime)
- participantes (Set<MatchEstudiante>)
- mensajes (Set<Mensaje>)
```

#### **Interaccion**
```java
- id (Long, PK)
- emisor (Estudiante)
- receptor (Estudiante)
- tipo (TipoInteraccion) // LIKE, SUPERLIKE, PASS
- fechaHora (LocalDateTime)
```

#### **Mensaje**
```java
- id (Long, PK)
- contenido (String, max 1000 chars)
- fechaHora (LocalDateTime)
- emisor (Estudiante)
- match (Match)
```

---

## 🎯 Hobbies Precargados

Al iniciar la aplicación, se cargan automáticamente los siguientes hobbies:

1. 🎮 **Gaming** - Videojuegos y esports
2. 🎵 **Musica** - Escuchar o tocar instrumentos
3. ⚽ **Deportes** - Actividades físicas y competitivas
4. ✈️ **Viajes** - Exploración y turismo
5. 📖 **Lectura** - Libros y literatura
6. 🎤 **Cantar** - Vocal y música
7. 💻 **Codificar** - Programación y desarrollo

---

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Si deseas colaborar:

1. **Fork** el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Haz commit de tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un **Pull Request**

---

## 📄 Licencia

Este proyecto está licenciado bajo la **MIT License** - mira el archivo [LICENSE](LICENSE) para más detalles.

### ¿Qué significa esto?

✅ **Puedes:**
- Usar el código comercialmente
- Modificar el código
- Distribuir el código
- Usar el código de forma privada

📋 **Debes:**
- Incluir el aviso de copyright y la licencia en todas las copias o porciones sustanciales del código

❌ **No puedes:**
- Responsabilizar a los autores por daños o problemas

---

Este proyecto fue desarrollado como parte de un proyecto académico del ITSON.

---

## 👥 Autores

Desarrollado por estudiantes del **Instituto Tecnológico de Sonora (ITSON)**.

---

## 📞 Soporte

Para problemas o preguntas, por favor abre un **Issue** en este repositorio.

---

**¡Conecta con estudiantes del ITSON de manera innovadora! 🚀**