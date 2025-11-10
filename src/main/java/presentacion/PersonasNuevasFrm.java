/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import dto.EstudianteDTO;
import dto.MatchDTO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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

/**
 *
 * @author USER
 */
public class PersonasNuevasFrm extends javax.swing.JPanel {

    private String nombreEstudiante;
    private EstudianteDTO estudianteActual;
    private EstudianteDTO estudianteReceptor;

    

    public PersonasNuevasFrm() {
        initComponents();
    }

    public PersonasNuevasFrm(EstudianteDTO estudianteActual, EstudianteDTO estudianteReceptor) {
        this.estudianteActual = estudianteActual;

        this.estudianteReceptor = estudianteReceptor;
        this.nombreEstudiante = estudianteReceptor.getNombre() + " " + estudianteReceptor.getApPaterno();

        initComponents();
        configurarEstiloTarjeta();
        cargarDatos();
    }
    private void configurarEstiloTarjeta(){
        this.setOpaque(false);
        
        jPanel1.setOpaque(true);
        jPanel1.setBackground(Color.white);
        
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(230, 230, 230), 1, true), 
            javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15) 
    ));
    }
    

    private void cargarDatos() {
        lblCarrera.setText(this.nombreEstudiante);
        Set<String> hobbies = estudianteReceptor.getHobbies();

        if (hobbies != null && !hobbies.isEmpty()) {
            lblIntereseYhobies.setText("Intereses: " + String.join(", ", hobbies));
        } else {
            lblIntereseYhobies.setText("Sin intereses comunes visibles");
        }

       
        if (estudianteReceptor.getFotoBase64() != null && !estudianteReceptor.getFotoBase64().isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(estudianteReceptor.getFotoBase64());
                ImageIcon icon = new ImageIcon(fotoBytes);
                Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblFoto.setIcon(crearAvatarCircular(60, obtenerIniciales(nombreEstudiante)));
            }
        } else {
            lblFoto.setIcon(crearAvatarCircular(60, obtenerIniciales(nombreEstudiante)));
        }
    }

    private ImageIcon crearAvatarCircular(int size, String iniciales) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor = generarColorPorId(estudianteReceptor.getId());
        g2.setColor(bgColor);
        g2.fillOval(0, 0, size, size);

        
        g2.setColor(new Color(254, 44, 85));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Arial", Font.BOLD, (int) (size / 2.5)));
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

        jPanel1 = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        lblIntereseYhobies = new javax.swing.JLabel();
        lblNombreEstudiante1 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblCarrera.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        lblCarrera.setForeground(new java.awt.Color(0, 0, 0));
        lblCarrera.setText("Carrera");

        lblIntereseYhobies.setFont(new java.awt.Font("SansSerif", 3, 13)); // NOI18N
        lblIntereseYhobies.setForeground(new java.awt.Color(0, 0, 0));
        lblIntereseYhobies.setText("Intereses y hobbies");

        lblNombreEstudiante1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        lblNombreEstudiante1.setForeground(new java.awt.Color(0, 0, 0));
        lblNombreEstudiante1.setText("Nombre estudiante");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(lblCarrera))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNombreEstudiante1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblIntereseYhobies)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombreEstudiante1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIntereseYhobies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCarrera)
                .addContainerGap(19, Short.MAX_VALUE))
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblIntereseYhobies;
    private javax.swing.JLabel lblNombreEstudiante1;
    // End of variables declaration//GEN-END:variables
}
