/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import dto.EstudianteDTO;
import dto.MatchDTO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author jorge
 */
public class PanelAvatarMacthes extends javax.swing.JPanel {

    private Long matchId;
    private String nombreEstudiante;
    private ImageIcon fotoEstudiante;
    private EstudianteDTO estudianteActual;
    private EstudianteDTO estudianteReceptor;

    /**
     * Creates new form PanelAvatarMacthes
     */
    public PanelAvatarMacthes() {
        initComponents();
        configurarEstilos();
    }

    public PanelAvatarMacthes(EstudianteDTO estudianteActual, MatchDTO match, EstudianteDTO estudianteReceptor) {
        this.estudianteActual = estudianteActual;
        this.matchId = match.getId();
        this.estudianteReceptor = estudianteReceptor;
        this.nombreEstudiante = estudianteReceptor.getNombre() + " " + estudianteReceptor.getApPaterno();

        initComponents();
        configurarEstilos();
        configurarDiseño();
        cargarDatos();
    }

    private void configurarDiseño() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)), 
                BorderFactory.createEmptyBorder(15, 20, 15, 20) 
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2; 
        gbc.insets = new Insets(0, 0, 0, 15); 
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblFoto, gbc);

        // 2. Nombre (Columna 1, Fila 0)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 3, 0); 
        add(lblNombre1, gbc);

        // 3. Intereses (Columna 1, Fila 1)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblCarrera, gbc);

        // 4. Botón Mensaje (Columna 2)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0; // No se estira
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 0, 0); 
        add(btnMensaje, gbc);
    }

    private void configurarEstilos() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        lblCarrera.setFont(new Font("Arial", Font.BOLD, 14));
        lblCarrera.setHorizontalAlignment(SwingConstants.CENTER);

        
        btnMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        btnMensaje.setFocusPainted(false);
        btnMensaje.setBorderPainted(false);
        btnMensaje.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(250, 250, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.WHITE);
            }
        });
    }

    private void cargarDatos() {
        lblNombre1.setText(this.nombreEstudiante);

        Set<String> hobies = estudianteReceptor.getHobbies();
        if (hobies != null && !hobies.isEmpty()) {
            lblCarrera.setText("Interes en comun: " + String.join(", ", hobies));

        } else {
            lblCarrera.setText("no hay interes en comun");
        }

        if (estudianteReceptor.getFotoBase64() != null && !estudianteReceptor.getFotoBase64().isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(estudianteReceptor.getFotoBase64());
                ImageIcon icon = new ImageIcon(fotoBytes);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblFoto.setIcon(crearAvatarCircular(72, obtenerIniciales(nombreEstudiante)));
            }
        } else {
            lblFoto.setIcon(crearAvatarCircular(72, obtenerIniciales(nombreEstudiante)));
        }
    }

    private ImageIcon crearAvatarCircular(int size, String iniciales) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor = generarColorPorId(estudianteReceptor.getId()); // Usa el ID del otro
        g2.setColor(bgColor);
        g2.fillOval(0, 0, size, size);

        g2.setColor(new Color(254, 44, 85));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(1, 1, size - 2, size - 2);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Arial", Font.BOLD, size / 3));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(iniciales)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(iniciales, x, y);

        g2.dispose();
        return new ImageIcon(image);
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "??";
        }
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length >= 2) {
            return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).toUpperCase();
        }
        return nombre.substring(0, Math.min(2, nombre.length())).toUpperCase();
    }

    private Color generarColorPorId(Long id) {
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

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public JLabel getLblFoto() {
        return lblFoto;
    }

    public void setLblFoto(JLabel lblFoto) {
        this.lblFoto = lblFoto;
    }

    public JLabel getLblNombre() {
        return lblCarrera;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblCarrera = lblNombre;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        btnMensaje = new presentacion.botonCircular();
        lblNombre1 = new javax.swing.JLabel();
        lblIntereses1 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setBackground(new java.awt.Color(255, 255, 255));

        lblCarrera.setBackground(new java.awt.Color(0, 0, 0));
        lblCarrera.setFont(new java.awt.Font("SansSerif", 1, 25)); // NOI18N
        lblCarrera.setForeground(new java.awt.Color(0, 0, 0));
        lblCarrera.setText("Carrera");

        btnMensaje.setBackground(new java.awt.Color(204, 204, 204));
        btnMensaje.setForeground(new java.awt.Color(0, 0, 0));
        btnMensaje.setText("Mensaje");
        btnMensaje.setBorderColor(new java.awt.Color(255, 255, 255));
        btnMensaje.setColor(new java.awt.Color(204, 204, 204));
        btnMensaje.setColorClick(new java.awt.Color(204, 204, 204));
        btnMensaje.setColorOver(new java.awt.Color(204, 204, 204));
        btnMensaje.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMensajeActionPerformed(evt);
            }
        });

        lblNombre1.setFont(new java.awt.Font("SansSerif", 1, 25)); // NOI18N
        lblNombre1.setForeground(new java.awt.Color(0, 0, 0));
        lblNombre1.setText("Nombre Usuario");

        lblIntereses1.setBackground(new java.awt.Color(0, 0, 0));
        lblIntereses1.setFont(new java.awt.Font("SansSerif", 1, 25)); // NOI18N
        lblIntereses1.setForeground(new java.awt.Color(0, 0, 0));
        lblIntereses1.setText("Intereses en comun");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIntereses1, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(165, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblIntereses1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCarrera)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMensajeActionPerformed
        if (this.estudianteActual == null) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo identificar al usuario actual.");
            return;
        }

        chatFrm chat = new chatFrm(this.estudianteActual, this.matchId, this.nombreEstudiante, this.estudianteReceptor);
        chat.setVisible(true);

        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_btnMensajeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private presentacion.botonCircular btnMensaje;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblIntereses1;
    private javax.swing.JLabel lblNombre1;
    // End of variables declaration//GEN-END:variables
}
