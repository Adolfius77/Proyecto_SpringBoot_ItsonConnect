/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author luisb
 */
public class EditarPerfilPanelCorrecto extends JPanel {
    private JLabel imageLabel;

    public EditarPerfilPanelCorrecto() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setPreferredSize(new Dimension(600, 400));


        // Título (centrado, ocupa 2 columnas)
        JLabel title = new JLabel("Edit Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 16, 0);
        add(title, c);

        // Imagen (centrada, ocupa 2 columnas)
        imageLabel = new JLabel("Cargar imagen", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setMinimumSize(new Dimension(150, 150));
        imageLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(239, 230, 230));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 8, 0);
        add(imageLabel, c);

        // Botón (centrado, ocupa 2 columnas)
        JButton photoButton = new JButton("Add Profile Photo");
        photoButton.addActionListener(e -> cargarNuevaImagen());
        photoButton.setBackground(new Color(230, 242, 255));
        photoButton.setForeground(new Color(0, 102, 204));
        photoButton.setFocusPainted(false);
        photoButton.setPreferredSize(new Dimension(200, 36));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 20, 0);
        add(photoButton, c);

        // --- Ahora el formulario: label a la izquierda y campo a la derecha ---

        // Label Nombre (columna 0, alineado a la izquierda/WEST)
        JLabel nombreLabel = new JLabel("Nombre");
        nombreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        add(nombreLabel, c);

        // TextField Nombre (columna 1, se expande horizontalmente)
        JTextField nombreField = createTextField("Luis");
        int h = nombreField.getPreferredSize().height;
        nombreField.setPreferredSize(new Dimension(200, h));
        nombreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;               // permite que la columna de la derecha se expanda
        c.insets = new Insets(0, 0, 6, 0);
        add(nombreField, c);
        
        //Label Bio
        JLabel bioLabel = new JLabel("Bio");
        bioLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        add(bioLabel, c);
        
        //TextArea
        JTextArea area = new JTextArea("Soy Luis Fernando", 3, 20); // 3 líneas visibles
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        area.setForeground(Color.BLACK);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 0);
        add(area, c);
        
        //Major 
        JLabel majorLabel = new JLabel("Major");
        majorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        add(majorLabel, c);

        // TextField major (columna 1, se expande horizontalmente)
        JTextField majorField = createTextField("Software mega software");
        int j = majorField.getPreferredSize().height;
        majorField.setPreferredSize(new Dimension(200, j));
        majorField.setMaximumSize(new Dimension(Integer.MAX_VALUE, j));
        c.gridx = 0;
        c.gridy = 8;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;               // permite que la columna de la derecha se expanda
        c.insets = new Insets(0, 0, 6, 8);
        add(majorField, c);
        
        //Year 
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        add(yearLabel, c);

        // TextField year (columna 1, se expande horizontalmente)
        String[] opciones = {"1", "2", "3", "4", "5"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboBox.setMaximumRowCount(5); // cuántas se ven antes de que aparezca la barra de scroll
        comboBox.setSelectedIndex(0); // selecciona la primera por defecto
        c.gridx = 1;
        c.gridy = 8;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;               // permite que la columna de la derecha se expanda
        c.insets = new Insets(0, 0, 6, 0);
        add(comboBox, c);
        
        
        JLabel titleHobbies = new JLabel("Hobbies & Interests");
        titleHobbies.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 0, 0, 0);
        add(titleHobbies, c);
        
        //Panel Hobbies
        String[] frutas = {"Manzana", "Banana", "Papaya", "Mango", "Uva", "Sandía", "Pera", "Kiwi", "Melón", "Manzana", "Banana", "Papaya", "Mango", "Uva", "Sandía", "Pera", "Kiwi", "Melón"};
        String[] pre = {"Manzana", "Banana", "Papaya", "Mango", "Uva", "Pera", "Kiwi", "Melón"};
        List<String> lista = Arrays.asList(pre);
        
        SelectableButtonPanel hobbiesPanel = new SelectableButtonPanel(frutas, lista);
        JScrollPane scrollHobbies = new JScrollPane(hobbiesPanel);
        scrollHobbies.setBorder(null);
        scrollHobbies.setPreferredSize(new Dimension(500, 200));
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollHobbies, c);
        
        List<String> selec = hobbiesPanel.getSelectedItems();
        
        JButton saveProfileButton = new JButton("Save Changes");
        saveProfileButton.addActionListener(e -> cargarNuevaImagen());
        saveProfileButton.setBackground(new Color(230, 242, 255));
        saveProfileButton.setForeground(new Color(0, 102, 204));
        saveProfileButton.setFocusPainted(false);
        saveProfileButton.setPreferredSize(new Dimension(200, 60));
        c.gridx = 0;
        c.gridy = 11;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 20, 0);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        add(saveProfileButton, c);

        // Añadir un glue vertical simple (una fila adicional con peso vertical) para empujar contenido arriba
        c.gridx = 0;
        c.gridy = 15;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.VERTICAL;
        add(Box.createVerticalGlue(), c);
    }

    // Helper: crea JTextField con estilo
    private JTextField createTextField(String initialText) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        field.setForeground(Color.BLACK);
        field.setText(initialText);
        return field;
    }

    // Muestra imagen a partir de bytes con escalado manteniendo proporción
    private void mostrarImagenDesdeBytes(byte[] datos) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(datos)) {
            BufferedImage img = ImageIO.read(bais);
            if (img != null) {
                int w = 150, h = 150;
                BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaled.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                double imgRatio = (double) img.getWidth() / img.getHeight();
                double boxRatio = (double) w / h;
                int drawW = w, drawH = h;
                if (imgRatio > boxRatio) {
                    drawW = w;
                    drawH = (int) (w / imgRatio);
                } else {
                    drawH = h;
                    drawW = (int) (h * imgRatio);
                }
                int x = (w - drawW) / 2;
                int y = (h - drawH) / 2;

                g2.setComposite(AlphaComposite.SrcOver);
                g2.drawImage(img, x, y, drawW, drawH, null);
                g2.dispose();

                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText(null);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Abre un JFileChooser y muestra la imagen seleccionada usando mostrarImagenDesdeBytes
    private void cargarNuevaImagen() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(f);
                if (img != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img, "png", baos);
                    mostrarImagenDesdeBytes(baos.toByteArray());
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo leer la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //sout
    // Demo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Editar Perfil - GridBagLayout");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(new EditarPerfilPanelCorrecto());
            f.pack();                       // ajusta tamaño exacto al contenido
            f.setSize(f.getWidth(), 850);   // pero forzamos un ancho fijo y una altura mínima
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}