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
import javax.swing.SwingConstants;

/**
 *
 * @author USER
 */
public class PersonasNuevasFrm extends javax.swing.JPanel {

    private String nombreEstudiante;
    private EstudianteDTO estudianteActual;
    private EstudianteDTO estudianteReceptor;
    
    private PanelFotoCircular panelFoto;
    private JLabel lblNombre;
    private JLabel lblCarrera;
    private JLabel lblHobbies;

    

    public PersonasNuevasFrm() {
        initComponents();
    }

    public PersonasNuevasFrm(EstudianteDTO estudianteActual, EstudianteDTO estudianteReceptor) {
        this.estudianteActual = estudianteActual;

        this.estudianteReceptor = estudianteReceptor;
        this.nombreEstudiante = estudianteReceptor.getNombre() + " " + estudianteReceptor.getApPaterno();

        initComponents();
        configurarDiseñoTarjeta();
        cargarDatos();
    }
    private void configurarDiseñoTarjeta(){
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 100)); // Altura fija para que se vea uniforme en la lista
        
        // Borde redondeado gris y margen interno
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), // Línea separadora abajo
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding interno
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        // 2. FOTO CIRCULAR (Izquierda)
        panelFoto = new PanelFotoCircular();
        panelFoto.setPreferredSize(new Dimension(70, 70)); // Tamaño de la foto
        panelFoto.setMinimumSize(new Dimension(70, 70));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3; // La foto abarca 3 filas de texto
        gbc.insets = new Insets(0, 0, 0, 15); // Espacio a la derecha de la foto
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelFoto, gbc);

        // 3. NOMBRE (Arriba derecha)
        lblNombre = new JLabel("Nombre Estudiante");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(new Color(33, 33, 33)); // Negro suave
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1.0; // Ocupar el ancho restante
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 2, 0); // Pequeño espacio abajo
        add(lblNombre, gbc);

        // 4. CARRERA (Centro derecha)
        lblCarrera = new JLabel("Carrera");
        lblCarrera.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblCarrera.setForeground(new Color(100, 100, 100)); // Gris
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        add(lblCarrera, gbc);

        // 5. HOBBIES (Abajo derecha)
        lblHobbies = new JLabel("Intereses...");
        lblHobbies.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblHobbies.setForeground(new Color(150, 150, 150)); // Gris claro
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(lblHobbies, gbc);
    }
    

    private void cargarDatos() {
        lblNombre.setText(this.nombreEstudiante);
        lblCarrera.setText(estudianteReceptor.getCarrera());

        Set<String> hobbies = estudianteReceptor.getHobbies();
        if (hobbies != null && !hobbies.isEmpty()) {
            String textoHobbies = String.join(", ", hobbies);
            if(textoHobbies.length() > 40) textoHobbies = textoHobbies.substring(0, 37) + "...";
            lblHobbies.setText("Intereses: " + textoHobbies);
        } else {
            lblHobbies.setText("Sin intereses visibles");
        }

        if (estudianteReceptor.getFotoBase64() != null && !estudianteReceptor.getFotoBase64().isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(estudianteReceptor.getFotoBase64());
                panelFoto.setImagenBytes(fotoBytes);
            } catch (Exception e) {
                ImageIcon icon = crearAvatarCircular(70, obtenerIniciales(nombreEstudiante));
                panelFoto.setImagen(icon.getImage());
            }
        } else {
            ImageIcon icon = crearAvatarCircular(70, obtenerIniciales(nombreEstudiante));
            panelFoto.setImagen(icon.getImage());
        }
        panelFoto.repaint();
    }

    private ImageIcon crearAvatarCircular(int size, String iniciales) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor = generarColorPorId(estudianteReceptor.getId());
        g2.setColor(bgColor);
        g2.fillOval(0, 0, size, size);

        g2.setColor(Color.WHITE); 
        g2.setFont(new Font("SansSerif", Font.BOLD, (int) (size / 2.5)));
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
            new Color(100, 181, 246), // Azul
            new Color(129, 199, 132), // Verde
            new Color(255, 183, 77),  // Naranja
            new Color(229, 115, 115), // Rojo
            new Color(186, 104, 200)  // Violeta
        };
        return colores[(int) (id % colores.length)];
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - 1;
        int height = getHeight() - 1;
        int arc = 30; 
        int shadowOffset = 5; 

        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(shadowOffset, shadowOffset, width - shadowOffset * 2, height - shadowOffset * 2, arc, arc);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, width - shadowOffset * 2, height - shadowOffset * 2, arc, arc);

        g2.setColor(new Color(240, 240, 240));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, width - shadowOffset * 2, height - shadowOffset * 2, arc, arc);

        g2.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombreEstudiante1 = new javax.swing.JLabel();
        lblIntereseYhobies = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        lblNombreEstudiante1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        lblNombreEstudiante1.setForeground(new java.awt.Color(0, 0, 0));
        lblNombreEstudiante1.setText("Nombre estudiante");

        lblIntereseYhobies.setFont(new java.awt.Font("SansSerif", 3, 13)); // NOI18N
        lblIntereseYhobies.setForeground(new java.awt.Color(0, 0, 0));
        lblIntereseYhobies.setText("Intereses y hobbies");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
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
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblIntereseYhobies;
    private javax.swing.JLabel lblNombreEstudiante1;
    // End of variables declaration//GEN-END:variables
}
