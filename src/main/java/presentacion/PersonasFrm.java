/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EstudianteDTO;
import dto.InteraccionDTO;
import java.awt.Font;
import java.awt.Image;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.util.function.Consumer;
import static org.springframework.boot.util.LambdaSafe.callback;

/**
 *
 * @author USER
 */
public class PersonasFrm extends javax.swing.JPanel {

    private Long emisorId;
    private Long receptorId;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PersonasFrm.class.getName());
    private Consumer<PersonasFrm> alFinalizarAccion;//consumer

    /**
     * Creates new form PersonasFrm
     */
    public PersonasFrm() {
        initComponents();
    }

    public PersonasFrm(Long emisorId, EstudianteDTO receptorDto, Consumer<PersonasFrm> callback) {

        initComponents();

        this.emisorId = emisorId;
        this.receptorId = receptorDto.getId();

        setFoto(receptorDto.getFotoBase64());

        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        String nombreCompleto = receptorDto.getNombre() + " " + receptorDto.getApPaterno();
        lblNombre.setText(nombreCompleto);
        String carreraTexto = receptorDto.getCarrera() != null ? receptorDto.getCarrera() : "Carrera no especificada";
        lblCarrera.setText(carreraTexto);
        lblGenero.setText(receptorDto.getGenero());

        lblHobbys.setFont(new Font("SansSerif", Font.ITALIC, 12));
        setHobbies(receptorDto.getHobbies());

        btnMegusta.addActionListener(e -> enviarInteraccion("LIKE"));
        btnNoMeInteresa.addActionListener(e -> enviarInteraccion("PASS"));
        this.alFinalizarAccion = callback;

    }

    private void setFoto(String fotoBase64) {
        ImageIcon icon;
        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(fotoBase64);
                icon = new ImageIcon(fotoBytes);
            } catch (Exception e) {
                logger.log(java.util.logging.Level.WARNING, "Error al decodificar foto", e);
                icon = getPlaceholderIcon();
            }
        } else {
            icon = getPlaceholderIcon();
        }

        Image img = icon.getImage().getScaledInstance(244, 182, Image.SCALE_SMOOTH);

        lblFotoPerfil.setIcon(new ImageIcon(img));
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setText("");
    }

    private ImageIcon getPlaceholderIcon() {
        return new ImageIcon(getClass().getResource("/fotoPerfil.jpg"));
    }

    private void setHobbies(Set<String> hobbies) {
        if (hobbies != null && !hobbies.isEmpty()) {
            String hobbiesTexto = "Hobbies: " + String.join(", ", hobbies);
            lblHobbys.setText(hobbiesTexto);
        } else {
            lblHobbys.setText("Sin hobbies definidos");
        }

    }

    private void enviarInteraccion(String tipo) {
        // Deshabilita los botones para evitar doble clic
        btnMegusta.setEnabled(false);
        btnNoMeInteresa.setEnabled(false);

        try {
            // 1. Crear el DTO de la Interacción
            InteraccionDTO interaccionDto = new InteraccionDTO();
            interaccionDto.setEmisorId(this.emisorId);
            interaccionDto.setReceptorId(this.receptorId);
            interaccionDto.setTipo(tipo);

            // 2. Convertir a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(interaccionDto);

            // 3. Crear cliente y petición HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigCliente.BASE_URL + "/api/interacciones")) // Endpoint de InteraccionController
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();

            // 4. Enviar petición (asíncrona para no bloquear la UI)
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        // Esto se ejecuta cuando el servidor responde
                        if (response.statusCode() == 201) { // 201 Creado
                            System.out.println(tipo + " enviado a " + this.receptorId);

                            // Oculta esta tarjeta después de interactuar
                           if (alFinalizarAccion != null) {
                                alFinalizarAccion.accept(this);
                            }

                        } else {
                            // Error (ej. 400 Bad Request, "Ya interactuaste")
                            JOptionPane.showMessageDialog(this, "Error: " + response.body(), "Error de Interacción", JOptionPane.ERROR_MESSAGE);
                            // Vuelve a habilitar los botones si falló
                            btnMegusta.setEnabled(true);
                            btnNoMeInteresa.setEnabled(true);
                        }
                    })
                    .exceptionally(e -> {
                        // Error de conexión
                        logger.log(java.util.logging.Level.SEVERE, "Error al enviar interacción", e);
                        JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        btnMegusta.setEnabled(true);
                        btnNoMeInteresa.setEnabled(true);
                        return null;
                    });

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al crear JSON de interacción", e);
            btnMegusta.setEnabled(true);
            btnNoMeInteresa.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblFotoPerfil = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblHobbys = new javax.swing.JLabel();
        btnNoMeInteresa = new presentacion.botonCircular();
        btnMegusta = new presentacion.botonCircular();
        lblCarrera = new javax.swing.JLabel();
        lblGenero = new javax.swing.JLabel();

        jLabel3.setText("jLabel3");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblNombre.setText("Nombre");

        lblHobbys.setText("Hobbies");

        btnNoMeInteresa.setForeground(new java.awt.Color(153, 0, 0));
        btnNoMeInteresa.setText("no me interesa");
        btnNoMeInteresa.setBorderColor(new java.awt.Color(255, 255, 255));
        btnNoMeInteresa.setColor(new java.awt.Color(255, 204, 204));
        btnNoMeInteresa.setColorClick(new java.awt.Color(255, 51, 51));
        btnNoMeInteresa.setColorOver(new java.awt.Color(255, 204, 204));
        btnNoMeInteresa.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N

        btnMegusta.setBackground(new java.awt.Color(102, 204, 255));
        btnMegusta.setForeground(new java.awt.Color(255, 255, 255));
        btnMegusta.setText("Me gusta");
        btnMegusta.setBorderColor(new java.awt.Color(255, 255, 255));
        btnMegusta.setColor(new java.awt.Color(102, 204, 255));
        btnMegusta.setColorClick(new java.awt.Color(102, 204, 255));
        btnMegusta.setColorOver(new java.awt.Color(51, 153, 255));
        btnMegusta.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N

        lblCarrera.setText("Carrera");

        lblGenero.setText("Genero");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombre)
                    .addComponent(lblHobbys)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNoMeInteresa, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnMegusta, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCarrera)
                    .addComponent(lblGenero))
                .addGap(0, 69, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFotoPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHobbys)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCarrera)
                .addGap(12, 12, 12)
                .addComponent(lblGenero)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNoMeInteresa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMegusta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private presentacion.botonCircular btnMegusta;
    private presentacion.botonCircular btnNoMeInteresa;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblHobbys;
    private javax.swing.JLabel lblNombre;
    // End of variables declaration//GEN-END:variables
}
