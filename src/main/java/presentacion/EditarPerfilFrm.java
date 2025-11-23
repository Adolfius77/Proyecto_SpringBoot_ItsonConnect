package presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.EstudianteDTO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Hobby;

public class EditarPerfilFrm extends JFrame {

    private final JLabel imageLabel;
    private final EstudianteDTO estudianteLogueado;
    private byte[] fotoBytes;

    // Componentes del formulario
    private final JTextField txtNombre;
    private final JTextArea txtBio;
    private final JTextField txtCarrera;
    private final JTextField txtEmail;
    private final JTextField txtFecha;
    private final JTextField txtGenero;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Color COLOR_PRIMARY_BLUE = new Color(59, 130, 246); // Azul botón guardar
    private final Color COLOR_BG_READONLY = new Color(229, 231, 235); // Gris fondo inputs bloqueados
    private final Color COLOR_BORDER = new Color(209, 213, 219);      // Gris borde inputs/foto
    private final Color COLOR_TEXT_LABEL = new Color(55, 65, 81);     // Gris oscuro etiquetas
    private final Color COLOR_TEXT_INPUT = new Color(17, 24, 39);     // Negro suave texto
    private final Color COLOR_WHITE = Color.WHITE;

    public EditarPerfilFrm(EstudianteDTO estudianteLogueado) {
        this.estudianteLogueado = estudianteLogueado;

        // Lógica de foto
        if (this.estudianteLogueado.getFotoBase64() != null && !this.estudianteLogueado.getFotoBase64().isEmpty()) {
            this.fotoBytes = Base64.getDecoder().decode(this.estudianteLogueado.getFotoBase64());
        }

        // Configuración Ventana
        setTitle("Editar Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(COLOR_WHITE);

        // 1. PANEL PRINCIPAL (Todo el fondo blanco, con margen externo)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_WHITE);
        mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); // Márgenes amplios como en el diseño

        GridBagConstraints gbc = new GridBagConstraints();

        // --- TÍTULO ---
        JLabel title = new JLabel("Editar Perfil");
        title.setFont(new Font("SansSerif", Font.BOLD, 32)); // Fuente grande y negrita
        title.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 50, 0); // Espacio grande debajo del título
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(title, gbc);

        // --- COLUMNA IZQUIERDA: FOTO Y BOTÓN ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(COLOR_WHITE);

        // Avatar (Imagen)
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(220, 220));
        imageLabel.setMinimumSize(new Dimension(220, 220));
        // Borde redondeado negro/gris oscuro y grueso como en la imagen
        imageLabel.setBorder(new LineBorder(Color.BLACK, 1, true));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.BLACK);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (this.fotoBytes != null) {
            mostrarImagenDesdeBytes(this.fotoBytes);
        } else {
            // Placeholder
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setText("Sin Foto");
        }

        // Botón "Cambiar Foto de Perfil"
        JButton btnCambiarFoto = createOutlinedButton("Cambiar Foto de Perfil", COLOR_PRIMARY_BLUE);
        btnCambiarFoto.addActionListener(e -> cargarNuevaImagen());

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(0, 0, 20, 0); // Espacio entre foto y botón
        leftPanel.add(imageLabel, gbcLeft);

        gbcLeft.gridy = 1;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL; // Botón ancho completo respecto a la foto
        leftPanel.add(btnCambiarFoto, gbcLeft);

        // Agregar Panel Izquierdo al Principal
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 1.0; // Empujar hacia arriba
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 60); // Espacio grande (gap) entre columnas
        mainPanel.add(leftPanel, gbc);

        // --- COLUMNA DERECHA: FORMULARIO ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(COLOR_WHITE);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.weightx = 1.0;
        gbcRight.gridx = 0;
        int row = 0;

        // 1. Nombre (Editable)
        txtNombre = createStyledTextField(estudianteLogueado.getNombre() + " " + estudianteLogueado.getApPaterno(), true);
        addFormItem(rightPanel, "Nombre", txtNombre, row++, gbcRight);

        // 2. Bio (TextArea Editable)
        JLabel lblBio = createStyledLabel("Bio");
        txtBio = new JTextArea(""); // Valor ejemplo o del DTO
        txtBio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBio.setForeground(COLOR_TEXT_INPUT);
        txtBio.setLineWrap(true);
        txtBio.setWrapStyleWord(true);
        // Borde gris redondeado igual que los textfields
        txtBio.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollBio = new JScrollPane(txtBio);
        scrollBio.setBorder(null); // Quitamos borde del scroll, el area ya tiene borde
        scrollBio.setPreferredSize(new Dimension(100, 80)); // Altura fija

        gbcRight.gridy = row * 2;
        gbcRight.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(lblBio, gbcRight); // Label Bio

        gbcRight.gridy = (row * 2) + 1;
        gbcRight.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(scrollBio, gbcRight); // Input Bio
        row++;

        // 3. Carrera (Solo lectura visual, Clicable)
        txtCarrera = createStyledTextField(estudianteLogueado.getCarrera(), false); // false = fondo gris
        txtCarrera.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txtCarrera.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDialogoCarrera();
            }
        });
        addFormItem(rightPanel, "Carrera", txtCarrera, row++, gbcRight);

        // 4. Hobbies e Intereses
        JLabel lblHobbies = createStyledLabel("Hobbies e Intereses");
        List<Hobby> hobbiesList = obtenerHobbies();
        SelectableButtonPanel hobbiesPanel = new SelectableButtonPanel(hobbiesList, estudianteLogueado.getHobbies());

        gbcRight.gridy = row * 2;
        gbcRight.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(lblHobbies, gbcRight);

        gbcRight.gridy = (row * 2) + 1;
        gbcRight.insets = new Insets(0, 0, 15, 0); // Espacio extra antes de la línea
        rightPanel.add(hobbiesPanel, gbcRight);
        row++;

        // Separador (Línea fina)
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(240, 240, 240));
        gbcRight.gridy = row * 2;
        gbcRight.insets = new Insets(0, 0, 10, 0);
        rightPanel.add(sep, gbcRight);
        row++; // Saltamos fila lógica

        // 5. Email (Solo lectura)
        txtEmail = createStyledTextField(estudianteLogueado.getCorreo(), false);
        addFormItem(rightPanel, "Email", txtEmail, row++, gbcRight);

        // 6. Fecha Registro (Solo lectura)
        String fechaStr = estudianteLogueado.getFechaRegistro() != null ? estudianteLogueado.getFechaRegistro().toString() : "";
        txtFecha = createStyledTextField(fechaStr, false);
        addFormItem(rightPanel, "Fecha de Registro", txtFecha, row++, gbcRight);

        // 7. Género (Solo lectura)
        txtGenero = createStyledTextField(estudianteLogueado.getGenero(), false);
        addFormItem(rightPanel, "Género", txtGenero, row++, gbcRight);

        // --- BOTONES INFERIORES (Derecha) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(COLOR_WHITE);

        JButton btnVolver = createOutlinedButton("Volver al Inicio", Color.GRAY); // Borde gris
        btnVolver.addActionListener(e -> volverAlInicio());

        JButton btnGuardar = createSolidButton("Guardar Cambios", COLOR_PRIMARY_BLUE); // Fondo azul
        btnGuardar.addActionListener(e -> saveChanges(hobbiesPanel));

        buttonPanel.add(btnVolver);
        buttonPanel.add(btnGuardar);

        gbcRight.gridy = row * 2;
        gbcRight.insets = new Insets(30, 0, 0, 0); // Espacio superior para separar del formulario
        rightPanel.add(buttonPanel, gbcRight);

        // Agregar Panel Derecho al Principal
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // El formulario toma el resto del ancho
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(rightPanel, gbc);

        // ScrollPane General
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(20); // Scroll rápido

        add(mainScroll);
        setSize(1200, 920); // Ventana grande para que respire el diseño
        setLocationRelativeTo(null);
    }

    private void addFormItem(JPanel panel, String labelText, JComponent component, int row, GridBagConstraints gbc) {
        JLabel label = createStyledLabel(labelText);

        // Configuración para el Label
        gbc.gridy = row * 2;
        gbc.insets = new Insets(0, 0, 6, 0); // Espacio pequeño (6px) entre label e input
        panel.add(label, gbc);

        // Configuración para el Input
        gbc.gridy = (row * 2) + 1;
        gbc.insets = new Insets(0, 0, 20, 0); // Espacio grande (20px) entre input y siguiente campo
        panel.add(component, gbc);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(COLOR_TEXT_LABEL);
        return label;
    }

    /**
     * Crea TextField con estilo: Borde redondeado gris, Padding interno. Si
     * editable = false, pone fondo gris claro (estilo "disabled" moderno).
     */
    private JTextField createStyledTextField(String text, boolean editable) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setForeground(COLOR_TEXT_INPUT);
        field.setEditable(editable);

        // Padding interno generoso (arriba/abajo: 10, izq/der: 12)
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true), // Borde gris redondeado
                new EmptyBorder(10, 12, 10, 12)
        ));

        if (!editable) {
            field.setBackground(COLOR_BG_READONLY);
            field.setForeground(new Color(107, 114, 128)); // Texto gris medio para readonly
        } else {
            field.setBackground(COLOR_WHITE);
        }
        return field;
    }

    /**
     * Botón con borde de color y fondo blanco (Ej: Cambiar foto, Volver).
     */
    private JButton createOutlinedButton(String text, Color accentColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        // Si el acento es gris (Volver), el texto es casi negro. Si es azul, texto azul.
        btn.setForeground(accentColor == Color.GRAY ? new Color(55, 65, 81) : accentColor);
        btn.setBackground(COLOR_WHITE);
        btn.setFocusPainted(false);

        // Borde del color de acento (o gris suave si es botón volver)
        Color borderColor = (accentColor == Color.GRAY) ? COLOR_BORDER : accentColor;
        btn.setBorder(new LineBorder(borderColor, 1, true));

        btn.setPreferredSize(new Dimension(200, 45)); // Botón ancho y alto
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Botón sólido de color (Ej: Guardar Cambios).
     */
    private JButton createSolidButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        // Padding interno sin borde visual
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void abrirDialogoCarrera() {
        DialogCarreras dialog = new DialogCarreras(this);
        dialog.setVisible(true);
        String seleccion = dialog.getCarreraSeleccionada();
        if (seleccion != null) {
            txtCarrera.setText(seleccion);
            estudianteLogueado.setCarrera(seleccion);
        }
    }

    private void saveChanges(SelectableButtonPanel panel) {
        if (txtCarrera.getText().trim().isEmpty() || txtCarrera.getText().equals("Seleccionar carrera")) {
            JOptionPane.showMessageDialog(this, "El campo Carrera no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Long estudianteId = estudianteLogueado.getId();
            String apiUrl = ConfigCliente.BASE_URL + "/api/estudiantes/" + estudianteId;

            estudianteLogueado.setHobbies(panel.getSelectedHobbies());
            estudianteLogueado.setCarrera(txtCarrera.getText());
            estudianteLogueado.setNombre(txtNombre.getText());

            // Aquí podrías setear la Bio si tuvieras el campo en EstudianteDTO
            // estudianteLogueado.setBio(txtBio.getText());
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
                JOptionPane.showMessageDialog(this, "Error al actualizar. Código: " + code, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión al actualizar el perfil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlInicio() {
        new inicioConnectFrm(this.estudianteLogueado).setVisible(true);
        this.dispose();
    }

    private void mostrarImagenDesdeBytes(byte[] datos) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(datos)) {
            BufferedImage img = ImageIO.read(bais);
            if (img != null) {
                // Escalamos la imagen al tamaño del label (220x220)
                Image scaledInstance = img.getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledInstance));
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
        }
        return Collections.emptyList();
    }
}
