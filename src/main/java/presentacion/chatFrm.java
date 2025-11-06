package presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChatMensajeDTO;
import dto.EstudianteDTO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

/**
 *
 * @author USER
 */
public class chatFrm extends javax.swing.JFrame {

    private EstudianteDTO estudianteActual;
    private Long matchId;
    private String nombreReceptor;

    private StompSession stompSession;
    private WebSocketStompClient stompClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(chatFrm.class.getName());

    public chatFrm(EstudianteDTO estudianteActual, Long matchId, String nombreReceptor) {
        this.estudianteActual = estudianteActual;
        this.matchId = matchId;
        this.nombreReceptor = nombreReceptor;

        initComponents();

        this.setTitle("Chat con " + this.nombreReceptor);
        this.jLabel2.setText(this.nombreReceptor);

        panelDinamicoChat.setLayout(new BoxLayout(panelDinamicoChat, BoxLayout.Y_AXIS));

        conectarWebSocket();

        btnEnviarMensaje.addActionListener(e -> enviarMensaje());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                desconectarWebSocket();

                System.exit(0);
            }
        });

    }

    private void conectarWebSocket() {
        try {
            List<Transport> transports = new ArrayList<>(1);
            transports.add(new WebSocketTransport(new StandardWebSocketClient()));
            WebSocketClient transport = new SockJsClient(transports);

            this.stompClient = new WebSocketStompClient(transport);

            // URL del Endpoint definida en WebsocketConfig.java
            String url = "http://localhost:8080/itson-connect-ws";

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
        String textoMensaje;

        // Determinar si el mensaje es nuestro o del receptor
        if (dto.getEmisorId().equals(this.estudianteActual.getId())) {
            textoMensaje = "Tú: " + dto.getContenido();
            // Aquí podrías alinear el JLabel a la derecha o darle otro color
        } else {
            textoMensaje = this.nombreReceptor + ": " + dto.getContenido();
            // Aquí podrías alinear el JLabel a la izquierda
        }

        JLabel lblMensaje = new JLabel(textoMensaje);
        // Aquí puedes añadirle padding, bordes, etc.

        panelDinamicoChat.add(lblMensaje);

        // Refrescar la UI
        panelDinamicoChat.revalidate();
        panelDinamicoChat.repaint();
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
            logger.info("¡Conectado a WebSocket! Sesión: " + session.getSessionId());

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

                    // IMPORTANTE: Actualizar la UI de Swing debe hacerse en el Event Dispatch Thread (EDT)
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelDinamicoChat = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtMensaje = new presentacion.TextFieldRedondo();
        btnEnviarMensaje = new presentacion.botonCircular();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        inicio = new javax.swing.JMenu();
        matches = new javax.swing.JMenu();
        perfil = new javax.swing.JMenu();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        panelDinamicoChat.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelDinamicoChatLayout = new javax.swing.GroupLayout(panelDinamicoChat);
        panelDinamicoChat.setLayout(panelDinamicoChatLayout);
        panelDinamicoChatLayout.setHorizontalGroup(
            panelDinamicoChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 567, Short.MAX_VALUE)
        );
        panelDinamicoChatLayout.setVerticalGroup(
            panelDinamicoChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 343, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelDinamicoChat);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMensaje.setText("Escribe un Mensaje......");

        btnEnviarMensaje.setBackground(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviarMensaje.setText("Enviar");
        btnEnviarMensaje.setBorderColor(new java.awt.Color(255, 255, 255));
        btnEnviarMensaje.setColor(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setColorClick(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setColorOver(new java.awt.Color(102, 204, 255));
        btnEnviarMensaje.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnviarMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Nombre");

        jLabel4.setText("Hobbies");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Chat con:");

        jLabel2.setFont(new java.awt.Font("Schadow BT", 1, 25)); // NOI18N
        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("ITSON Connect");
        jMenuBar1.add(jMenu1);

        inicio.setText("Inicio");
        jMenuBar1.add(inicio);

        matches.setText("Matches");
        jMenuBar1.add(matches);

        perfil.setText("Perfil");
        jMenuBar1.add(perfil);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private presentacion.botonCircular btnEnviarMensaje;
    private javax.swing.JMenu inicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenu matches;
    private javax.swing.JPanel panelDinamicoChat;
    private javax.swing.JMenu perfil;
    private presentacion.TextFieldRedondo txtMensaje;
    // End of variables declaration//GEN-END:variables
}
