package presentacion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChatMensajeDTO;
import dto.EstudianteDTO;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

/**
 *
 * @author USER
 */
public class chatFrm extends javax.swing.JFrame {

    private EstudianteDTO estudianteActual;
    private Long matchId;
    private String nombreReceptor;
    private EstudianteDTO estudianteReceptor;

    private StompSession stompSession;
    private WebSocketStompClient stompClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

  
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(chatFrm.class.getName());

    public chatFrm(EstudianteDTO estudianteActual, Long matchId, String nombreReceptor, EstudianteDTO estudianteReceptor) {
        this.estudianteActual = estudianteActual;
        this.matchId = matchId;
        this.nombreReceptor = nombreReceptor;
        this.estudianteReceptor = estudianteReceptor;

        this.nombreReceptor = estudianteReceptor.getNombre() + " " + estudianteReceptor.getApPaterno();

        initComponents();
        setLocationRelativeTo(null);
        this.setTitle("Chat con " + this.nombreReceptor);
        this.jLabel2.setText(this.nombreReceptor);

        this.lblNombreInfo.setText(this.nombreReceptor);
        lblNombreInfo.setAlignmentX(CENTER_ALIGNMENT);
        lblHobbies.setAlignmentY(CENTER_ALIGNMENT);
        lblNombre.setText(this.estudianteActual.getNombre() + " " + this.estudianteActual.getApPaterno());
        setFoto(estudianteReceptor.getFotoBase64());

        Set<String> hobbies = estudianteReceptor.getHobbies();
        if (hobbies != null && !hobbies.isEmpty()) {
            String hobbiesTexto = "<html>" + String.join(", ", hobbies) + "</html>";
            this.lblHobbies.setText(hobbiesTexto);
        } else {
            this.lblHobbies.setText("esta persona no tiene hobbies");
        }
        panelDinamicoChat.setLayout(new BoxLayout(panelDinamicoChat, BoxLayout.Y_AXIS));

        cargarHistorialDemensajes();
        conectarWebSocket();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                desconectarWebSocket();

                System.exit(0);
            }
        });

    }

    private void irAPerfil() {
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.desconectarWebSocket(); // Desconecta el chat antes de cerrar
            this.dispose();
            return;
        }

        EditarPerfilFrm editFrame = new EditarPerfilFrm(this.estudianteActual);
        editFrame.setVisible(true);
        this.desconectarWebSocket();
        this.dispose();
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "??";
        }
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length >= 2) {
            //sepramos
            return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).toUpperCase();
        }
        return nombre.substring(0, Math.min(2, nombre.length())).toUpperCase();
    }

    private void setFoto(String fotoBase64) {
        ImageIcon icon;
        int fotoSize = 100; // Define el tamaño de la foto/avatar aquí

        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            try {
                // Intenta decodificar la foto
                byte[] fotoBytes = Base64.getDecoder().decode(fotoBase64);
                icon = new ImageIcon(fotoBytes);
                // Escala la foto
                Image img = icon.getImage().getScaledInstance(fotoSize, fotoSize, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));

            } catch (Exception e) {
                // Si la foto está corrupta o falla, usa las iniciales
                logger.log(java.util.logging.Level.WARNING, "Error al decodificar foto", e);
                String iniciales = obtenerIniciales(this.nombreReceptor);
                icon = crearAvatarCircular(fotoSize, iniciales);
                lblFoto.setIcon(icon);
            }
        } else {
            String iniciales = obtenerIniciales(this.nombreReceptor);
            icon = crearAvatarCircular(fotoSize, iniciales);
            lblFoto.setIcon(icon);
        }

        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setText("");
    }

    public Color generarColorPorId(Long id) {
        if (id == null) {
            id = 0L;
        }
        Color[] colores = {
            new Color(255, 212, 186), new Color(255, 228, 212),
            new Color(255, 208, 200), new Color(255, 224, 216),
            new Color(232, 216, 200), new Color(255, 220, 220)
        };
        return colores[(int) (id % colores.length)];
    }

    private ImageIcon crearAvatarCircular(int size, String iniciales) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Long idReceptor = 0L;
        if (this.estudianteReceptor != null) {
            idReceptor = this.estudianteReceptor.getId();
        }
        Color bgColor = generarColorPorId(idReceptor);

        g2.setColor(bgColor);
        g2.fillOval(0, 0, size, size);

        // Dibuja las iniciales
        g2.setColor(Color.DARK_GRAY); // Color del texto
        g2.setFont(new Font("SansSerif", Font.BOLD, size / 3)); // Tamaño de fuente
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(iniciales)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(iniciales, x, y);

        g2.dispose();
        return new ImageIcon(image);
    }

    private ImageIcon getPlaceholderIcon() {
        return new ImageIcon(getClass().getResource("/fotoPerfil.jpg"));
    }

    private void conectarWebSocket() {
        try {
            List<Transport> transports = new ArrayList<>(1);
            transports.add(new WebSocketTransport(new StandardWebSocketClient()));
            WebSocketClient transport = new SockJsClient(transports);

            this.stompClient = new WebSocketStompClient(transport);
            //esto tranforma los objetos chatmesaje dto a json
            this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            // URL del Endpoint definida en WebsocketConfig.java
            String url = ConfigCliente.WS_URL;

            this.stompSession = stompClient.connectAsync(url, new MyStompSessionHandler()).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al conectar con WebSocket", e);
            JOptionPane.showMessageDialog(this, "Error de conexión al chat: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarMensaje() {
        String contenido = txtMensaje.getText();
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }

        if (stompSession == null || !stompSession.isConnected()) {
            JOptionPane.showMessageDialog(this, "No estás conectado al chat.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Crear el DTO que espera el ChatController
        ChatMensajeDTO mensajeDto = new ChatMensajeDTO();
        mensajeDto.setContenido(contenido);
        mensajeDto.setEmisorId(estudianteActual.getId());
        mensajeDto.setEmisorNombre(estudianteActual.getNombre());
        mensajeDto.setMatchId(this.matchId);

        // 2. Definir el destino (mapeado en @MessageMapping en ChatController)
        // El prefijo /app se añade automáticamente
        String destino = "/app/chat/" + this.matchId;

        // 3. Enviar el DTO
        stompSession.send(destino, mensajeDto);

        // 4. Limpiar el campo de texto
        txtMensaje.setText("");
    }

    private void mostrarMensaje(ChatMensajeDTO dto) {
        boolean esMio = dto.getEmisorId().equals(this.estudianteActual.getId());
        BurbujaMensaje burbuja = new BurbujaMensaje(dto.getContenido(), esMio);

        JPanel fila = new JPanel();
        fila.setOpaque(false);
        fila.setLayout(new FlowLayout(esMio ? FlowLayout.TRAILING : FlowLayout.LEADING, 10, 5));

        fila.add(burbuja);

        panelDinamicoChat.add(fila);
        panelDinamicoChat.add(Box.createVerticalStrut(5));

        panelDinamicoChat.revalidate();
        panelDinamicoChat.repaint();

        SwingUtilities.invokeLater(() -> {
            try {
                jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
            } catch (Exception e) {
            }
        });
    }

    private void cargarHistorialDemensajes() {
        java.util.concurrent.Executors.newSingleThreadExecutor().submit(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                String url = ConfigCliente.BASE_URL + "/api/matches/" + this.matchId + "/mensajes?limit=50";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Contend-Type", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    List<ChatMensajeDTO> historial = objectMapper.readValue(
                            response.body(),
                            new TypeReference<List<ChatMensajeDTO>>() {
                    }
                    );
                    SwingUtilities.invokeLater(() -> {
                        for (ChatMensajeDTO dto : historial) {
                            mostrarMensaje(dto);
                        }
                        hacerScrollAbajo();
                    });
                } else {
                    System.out.println("error al cargar el historial: " + response.statusCode());
                }
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Error cargando historial de chat", e);
            }
        });
    }

    private void hacerScrollAbajo() {
        try {
            javax.swing.JScrollBar vertical = jScrollPane1.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void desconectarWebSocket() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            logger.info("Desconectado del WebSocket.");
        }
        if (stompClient != null) {
            stompClient.stop();
        }
    }

    private class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            logger.info("¡Conectado a WebSocket! Sesion: " + session.getSessionId());

            // 1. Suscribirse al topic del Match
            // Este es el destino que ChatController usa para re-enviar mensajes
            String destinoTopic = "/topic/match/" + matchId;

            session.subscribe(destinoTopic, new StompFrameHandler() {

                // Define qué tipo de objeto esperamos recibir (el DTO)
                @Override
                public java.lang.reflect.Type getPayloadType(StompHeaders headers) {
                    return ChatMensajeDTO.class;
                }

                // Método que se llama cuando llega un mensaje
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    ChatMensajeDTO mensajeRecibido = (ChatMensajeDTO) payload;

                    SwingUtilities.invokeLater(() -> {
                        mostrarMensaje(mensajeRecibido);
                    });
                }
            });

            logger.info("Suscrito a: " + destinoTopic);
        }

        public void handleException(StompSession session, StompHeaders headers, Throwable exception) {
            logger.log(java.util.logging.Level.SEVERE, "Excepción en STOMP", exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            logger.log(java.util.logging.Level.SEVERE, "Error de transporte en STOMP", exception);
        }
    }

    public chatFrm() {
        initComponents();
        JOptionPane.showMessageDialog(this, "Error: Este chat se inició sin un usuario o match. No funcionará.", "Error de Contexto", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btnBuscarEstudiantes = new presentacion.botonCircular();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelDinamicoChat = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtMensaje = new presentacion.TextFieldRedondo();
        btnEnviarMensaje = new presentacion.botonCircular();
        jPanel3 = new javax.swing.JPanel();
        panelRedondo1 = new presentacion.PanelRedondo();
        lblFoto = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblNombreInfo = new javax.swing.JLabel();
        lblHobbies = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnInicio = new presentacion.botonCircular();
        btnMatches = new presentacion.botonCircular();
        btnPerfil = new presentacion.botonCircular();
        jLabel3 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        btnCerrarSesion = new presentacion.botonCircular();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnBuscarEstudiantes.setBackground(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarEstudiantes.setText("Buscar estudiantes");
        btnBuscarEstudiantes.setBorderColor(new java.awt.Color(255, 255, 255));
        btnBuscarEstudiantes.setColor(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setColorClick(new java.awt.Color(102, 204, 255));
        btnBuscarEstudiantes.setColorOver(new java.awt.Color(51, 204, 255));
        btnBuscarEstudiantes.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnBuscarEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEstudiantesActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        panelDinamicoChat.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelDinamicoChatLayout = new javax.swing.GroupLayout(panelDinamicoChat);
        panelDinamicoChat.setLayout(panelDinamicoChatLayout);
        panelDinamicoChatLayout.setHorizontalGroup(
            panelDinamicoChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 723, Short.MAX_VALUE)
        );
        panelDinamicoChatLayout.setVerticalGroup(
            panelDinamicoChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelDinamicoChat);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMensajeActionPerformed(evt);
            }
        });

        btnEnviarMensaje.setBackground(new java.awt.Color(30, 115, 179));
        btnEnviarMensaje.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviarMensaje.setText("Enviar");
        btnEnviarMensaje.setBorderColor(new java.awt.Color(255, 255, 255));
        btnEnviarMensaje.setColor(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setColorClick(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setColorOver(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnEnviarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarMensajeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelRedondo1.setBackground(new java.awt.Color(255, 255, 255));

        lblNombreInfo.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N

        javax.swing.GroupLayout panelRedondo1Layout = new javax.swing.GroupLayout(panelRedondo1);
        panelRedondo1.setLayout(panelRedondo1Layout);
        panelRedondo1Layout.setHorizontalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addGroup(panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRedondo1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRedondo1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblNombreInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRedondo1Layout.setVerticalGroup(
            panelRedondo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRedondo1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombreInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lblHobbies.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        lblHobbies.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblHobbies, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(panelRedondo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHobbies, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Chat con:");

        jLabel2.setFont(new java.awt.Font("Schadow BT", 1, 25)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("jLabel2");

        jPanel5.setBackground(new java.awt.Color(247, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnInicio.setForeground(new java.awt.Color(0, 0, 0));
        btnInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hogar.png"))); // NOI18N
        btnInicio.setText("Inicio");
        btnInicio.setBorderColor(new java.awt.Color(204, 255, 255));
        btnInicio.setColorClick(new java.awt.Color(102, 204, 255));
        btnInicio.setColorOver(new java.awt.Color(102, 204, 255));
        btnInicio.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        btnMatches.setForeground(new java.awt.Color(0, 0, 0));
        btnMatches.setIcon(new javax.swing.ImageIcon(getClass().getResource("/amigos-felices.png"))); // NOI18N
        btnMatches.setText("Matches");
        btnMatches.setBorderColor(new java.awt.Color(204, 255, 255));
        btnMatches.setColorClick(new java.awt.Color(102, 204, 255));
        btnMatches.setColorOver(new java.awt.Color(102, 204, 255));
        btnMatches.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnMatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchesActionPerformed(evt);
            }
        });

        btnPerfil.setForeground(new java.awt.Color(0, 0, 0));
        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editar (2).png"))); // NOI18N
        btnPerfil.setText("Perfil");
        btnPerfil.setBorderColor(new java.awt.Color(204, 255, 255));
        btnPerfil.setColorClick(new java.awt.Color(102, 204, 255));
        btnPerfil.setColorOver(new java.awt.Color(102, 204, 255));
        btnPerfil.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/usuario (2).png"))); // NOI18N

        lblNombre.setBackground(new java.awt.Color(0, 0, 0));
        lblNombre.setFont(new java.awt.Font("SansSerif", 3, 16)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(0, 0, 0));
        lblNombre.setText("Nombre estudiante");

        btnCerrarSesion.setForeground(new java.awt.Color(0, 0, 0));
        btnCerrarSesion.setText("Cerrar sesion");
        btnCerrarSesion.setBorderColor(new java.awt.Color(204, 255, 255));
        btnCerrarSesion.setColorClick(new java.awt.Color(102, 204, 255));
        btnCerrarSesion.setColorOver(new java.awt.Color(102, 204, 255));
        btnCerrarSesion.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNombre))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMatches, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblNombre)))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMatches, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarMensajeActionPerformed
        enviarMensaje();
    }//GEN-LAST:event_btnEnviarMensajeActionPerformed

    private void txtMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMensajeActionPerformed
       enviarMensaje();
    }//GEN-LAST:event_txtMensajeActionPerformed

    private void btnBuscarEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEstudiantesActionPerformed
        if (this.estudianteActual == null) {

            JOptionPane.showMessageDialog(this, "Error, no se ha iniciado sesion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DescubrirFrm descu = new DescubrirFrm(this.estudianteActual, null);
        descu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBuscarEstudiantesActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }
        inicioConnectFrm inicio = new inicioConnectFrm(this.estudianteActual);
        inicio.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnMatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchesActionPerformed
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error de sesion. Intente iniciar sesion de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            new IniciarSesionFrm().setVisible(true);
            this.dispose();
            return;
        }
        matchesFrm matchesVentana = new matchesFrm(this.estudianteActual);
        matchesVentana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMatchesActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        irAPerfil();

    }//GEN-LAST:event_btnPerfilActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cerrar sesion?",
                "Cerrar Sesión",
                javax.swing.JOptionPane.YES_NO_OPTION
        );
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            this.estudianteActual = null;

            Main mainFrame = new Main();
            mainFrame.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(chatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(chatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(chatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(chatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new chatFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private presentacion.botonCircular btnBuscarEstudiantes;
    private presentacion.botonCircular btnCerrarSesion;
    private presentacion.botonCircular btnEnviarMensaje;
    private presentacion.botonCircular btnInicio;
    private presentacion.botonCircular btnMatches;
    private presentacion.botonCircular btnPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblHobbies;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreInfo;
    private javax.swing.JPanel panelDinamicoChat;
    private presentacion.PanelRedondo panelRedondo1;
    private presentacion.TextFieldRedondo txtMensaje;
    // End of variables declaration//GEN-END:variables
}
