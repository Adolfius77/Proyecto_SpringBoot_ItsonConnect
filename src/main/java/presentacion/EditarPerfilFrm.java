package presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EstudianteDTO;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Hobby;

public class EditarPerfilFrm extends JFrame {

    private final JLabel imageLabel;
    private final EstudianteDTO estudianteLogueado;
    private byte[] fotoBytes;
    private final JTextArea area;
    private final JButton btnCarrera;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EditarPerfilFrm(EstudianteDTO estudianteLogueado) {

        this.estudianteLogueado = estudianteLogueado;
        if (this.estudianteLogueado.getFotoBase64() != null && !this.estudianteLogueado.getFotoBase64().isEmpty()) {
            this.fotoBytes = Base64.getDecoder().decode(this.estudianteLogueado.getFotoBase64());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        mainPanel.setPreferredSize(new Dimension(600, 400));

        JLabel title = new JLabel("Editar Perfil");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 16, 0);
        mainPanel.add(title, c);

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
        mainPanel.add(imageLabel, c);

        if (this.fotoBytes != null) {
            mostrarImagenDesdeBytes(this.fotoBytes);
        }

        JButton photoButton = new JButton("Cambiar Foto de Perfil");
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
        mainPanel.add(photoButton, c);

        JLabel nombreLabel = new JLabel("Nombre");
        nombreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(nombreLabel, c);

        JTextField nombreField = createTextField(estudianteLogueado.getNombre() + " " + estudianteLogueado.getApPaterno() + " " + estudianteLogueado.getApMaterno());
        int h = nombreField.getPreferredSize().height;
        nombreField.setEditable(false);
        nombreField.setPreferredSize(new Dimension(200, h));
        nombreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(nombreField, c);

        JLabel bioLabel = new JLabel("Bio");
        bioLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(bioLabel, c);

        area = new JTextArea("Soy Luis Fernando", 3, 20);
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
        mainPanel.add(area, c);

        JLabel majorLabel = new JLabel("Carrera");
        majorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(majorLabel, c);

        btnCarrera = new JButton(estudianteLogueado.getCarrera() != null
                ? estudianteLogueado.getCarrera()
                : "Seleccionar carrera");
        btnCarrera.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCarrera.setFocusPainted(false);
        btnCarrera.setBackground(new Color(230, 242, 255));
        btnCarrera.setForeground(new Color(0, 102, 204));
        btnCarrera.setPreferredSize(new Dimension(200, 36));
        btnCarrera.addActionListener(e -> {
            DialogCarreras dialog = new DialogCarreras(this);
            dialog.setVisible(true);

            String seleccion = dialog.getCarreraSeleccionada();
            if (seleccion != null) {
                btnCarrera.setText(seleccion);
                estudianteLogueado.setCarrera(seleccion);
            }
        });
        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(btnCarrera, c);

        JLabel titleHobbies = new JLabel("Hobbies e Intereses");
        titleHobbies.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(titleHobbies, c);

        List<Hobby> hobbies = obtenerHobbies();
        SelectableButtonPanel hobbiesPanel = new SelectableButtonPanel(hobbies, estudianteLogueado.getHobbies());
        JScrollPane scrollHobbies = new JScrollPane(hobbiesPanel);
        scrollHobbies.setBorder(null);
        scrollHobbies.setPreferredSize(new Dimension(500, 75));
        scrollHobbies.setMinimumSize(new Dimension(500, 75));
        scrollHobbies.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        mainPanel.add(scrollHobbies, c);

        JButton btnVolver = new JButton("Volver al Inicio");
        btnVolver.addActionListener(e -> volverAlInicio());
        btnVolver.setBackground(new Color(220, 220, 220));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFocusPainted(false);
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVolver.setPreferredSize(new Dimension(200, 40));

        JButton saveProfileButton = new JButton("Guardar Cambios");
        saveProfileButton.addActionListener(e -> saveChanges(hobbiesPanel));
        saveProfileButton.setBackground(new Color(0, 102, 204));
        saveProfileButton.setForeground(Color.WHITE);
        saveProfileButton.setFocusPainted(false);
        saveProfileButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveProfileButton.setPreferredSize(new Dimension(200, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnVolver);
        buttonPanel.add(saveProfileButton);

        c.gridx = 0;
        c.gridy = 11;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 0, 20, 0);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        mainPanel.add(buttonPanel, c);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 12;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(emailLabel, c);

        JTextField emailField = createTextField(estudianteLogueado.getCorreo());
        emailField.setEditable(false);
        c.gridx = 0;
        c.gridy = 13;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(emailField, c);

        JLabel fechaLabel = new JLabel("Fecha de Registro");
        fechaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 14;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(fechaLabel, c);

        JTextField fechaField = createTextField(estudianteLogueado.getFechaRegistro());
        fechaField.setEditable(false);
        c.gridx = 0;
        c.gridy = 15;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(fechaField, c);

        JLabel generoLabel = new JLabel("Género");
        generoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 16;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 6, 8);
        mainPanel.add(generoLabel, c);

        JTextField generoField = createTextField(estudianteLogueado.getGenero());
        generoField.setEditable(false);
        c.gridx = 0;
        c.gridy = 17;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(generoField, c);

        c.gridx = 0;
        c.gridy = 18;
        c.gridwidth = 2;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(Box.createVerticalGlue(), c);

        add(mainPanel);
        pack();
        setSize(getWidth(), 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void volverAlInicio() {
        new inicioConnectFrm(this.estudianteLogueado).setVisible(true);
        this.dispose();
    }

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

    private void saveChanges(SelectableButtonPanel panel) {
        if (btnCarrera.getText().trim().isEmpty() || btnCarrera.getText().equals("Seleccionar carrera")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El campo Carrera no puede estar vacío.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Long estudianteId = estudianteLogueado.getId();
            String apiUrl = ConfigCliente.BASE_URL + "/api/estudiantes/" + estudianteId;

            estudianteLogueado.setHobbies(panel.getSelectedHobbies());
            estudianteLogueado.setCarrera(btnCarrera.getText());
            if (fotoBytes != null) {
                estudianteLogueado.setFotoBase64(Base64.getEncoder().encodeToString(fotoBytes));
            }

            String jsonInput = objectMapper.writeValueAsString(estudianteLogueado);

            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Perfil actualizado con éxito.");
                new inicioConnectFrm(this.estudianteLogueado).setVisible(true);
                this.dispose();
            } else {
                String errorResponse = "";
                try (Scanner scanner = new Scanner(conn.getErrorStream(), "UTF-8")) {
                    errorResponse = scanner.useDelimiter("\\A").next();
                }
                JOptionPane.showMessageDialog(this, "Error al actualizar. Código: " + code + "\nMensaje: " + errorResponse,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error de conexión al actualizar el perfil: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

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
                    this.fotoBytes = baos.toByteArray();

                    mostrarImagenDesdeBytes(this.fotoBytes);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo leer la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<Hobby> obtenerHobbies() {
        try {
            URL url = new URL(ConfigCliente.BASE_URL + "/api/hobbies?limit=100");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (Scanner sc = new Scanner(conn.getInputStream())) {
                    String json = sc.useDelimiter("\\A").next();
                    Hobby[] hobbiesArr = objectMapper.readValue(json, Hobby[].class);
                    return Arrays.asList(hobbiesArr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los hobbies: " + e.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
        return Collections.emptyList();
    }

}