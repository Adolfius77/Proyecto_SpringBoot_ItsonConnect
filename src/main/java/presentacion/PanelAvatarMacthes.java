/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author jorge
 */
public class PanelAvatarMacthes extends javax.swing.JPanel {
    private Long matchId;
    private Long estudianteId;
    private String nombreEstudiante;
    private ImageIcon fotoEstudiante;
    
    /**
     * Creates new form PanelAvatarMacthes
     */
    public PanelAvatarMacthes() {
        initComponents();
        configurarEstilos();
    }

    public PanelAvatarMacthes(Long matchId, Long estudianteId, String nombreEstudiante, ImageIcon fotoEstudiante) {
        this.matchId = matchId;
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.fotoEstudiante = fotoEstudiante;
        
        initComponents();
        configurarEstilos();
        cargarDatos();
    }
    
     private void configurarEstilos() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnMensaje.setBackground(new Color(254, 44, 85));
        btnMensaje.setForeground(Color.WHITE);
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
        lblNombre.setText(nombreEstudiante != null ? nombreEstudiante : "Usuario " + estudianteId);
        
        
        if (fotoEstudiante == null) {
            lblFoto.setIcon(crearAvatarCircular(80, obtenerIniciales(nombreEstudiante)));
        } else {
            lblFoto.setIcon(fotoEstudiante);
        }
    }
     
     private ImageIcon crearAvatarCircular(int size, String iniciales) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color bgColor = generarColorPorId(estudianteId);
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
        if (nombre == null || nombre.isEmpty()) return "??";
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length >= 2) {
            return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).toUpperCase();
        }
        return nombre.substring(0, Math.min(2, nombre.length())).toUpperCase();
    }
    
    private Color generarColorPorId(Long id) {
        if (id == null) id = 0L;
        Color[] colores = {
            new Color(255, 212, 186), new Color(255, 228, 212),
            new Color(255, 208, 200), new Color(255, 224, 216),
            new Color(232, 216, 200), new Color(255, 220, 220)
        };
        return colores[(int)(id % colores.length)];
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
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFoto = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        btnMensaje = new javax.swing.JButton();

        lblNombre.setText("Nombre Usuario");

        btnMensaje.setText("Mensaje");
        btnMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMensajeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblNombre)
                .addGap(18, 18, 18)
                .addComponent(btnMensaje)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnMensajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMensajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMensajeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMensaje;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNombre;
    // End of variables declaration//GEN-END:variables
}
