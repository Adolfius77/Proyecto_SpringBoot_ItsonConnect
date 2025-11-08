/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author luisb
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EditarPerfilPanel extends JPanel {

    private JTextField nameField, majorField, yearField;
    private JTextArea bioArea;
    private JPanel hobbiesPanel;
    private List<String> hobbiesSeleccionados = new ArrayList<>();
    private List<String> hobbiesDisponibles;

    public EditarPerfilPanel(List<String> hobbies) {
        this.hobbiesDisponibles = hobbies;

        setPreferredSize(new Dimension(480, 620));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Panel principal con márgenes
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        // --- Título ---
        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Botón de Foto ---
        JButton photoButton = new JButton("Add Profile Photo");
        photoButton.setBackground(new Color(230, 242, 255));
        photoButton.setForeground(new Color(0, 102, 204));
        photoButton.setFocusPainted(false);
        photoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        photoButton.setPreferredSize(new Dimension(200, 40));
        mainPanel.add(photoButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Nombre ---
        nameField = createTextField("Your full name");
        mainPanel.add(labeledField("Name", nameField));

        // --- Bio ---
        bioArea = new JTextArea(3, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane bioScroll = new JScrollPane(bioArea);
        bioScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        bioScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(labeledField("Bio", bioScroll));

        // --- Major y Year ---
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rowPanel.setBackground(Color.WHITE);
        majorField = createTextField("e.g. Software Engineering");
        yearField = createTextField("e.g. 4");
        rowPanel.add(labeledField("Major", majorField));
        rowPanel.add(labeledField("Year", yearField));
        mainPanel.add(rowPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Hobbies predefinidos ---
        JLabel hobbiesLabel = new JLabel("Hobbies & Interests");
        hobbiesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        hobbiesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(hobbiesLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        hobbiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        hobbiesPanel.setBackground(Color.WHITE);

        for (String hobbie : hobbiesDisponibles) {
            JToggleButton tagButton = crearBotonHobbie(hobbie);
            hobbiesPanel.add(tagButton);
        }

        mainPanel.add(hobbiesPanel);

        // --- Botón Guardar ---
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(0, 153, 255));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(180, 45));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(saveButton);

        // Scroll solo vertical
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel labeledField(String labelText, Component field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return panel;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private JToggleButton crearBotonHobbie(String texto) {
        JToggleButton boton = new JToggleButton(texto);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        boton.setBackground(new Color(230, 242, 255));
        boton.setForeground(new Color(0, 102, 204));
        boton.setBorder(new RoundedBorder(15));
        boton.setOpaque(true);
        boton.addActionListener(e -> {
            if (boton.isSelected()) {
                boton.setBackground(new Color(0, 153, 255));
                boton.setForeground(Color.WHITE);
                hobbiesSeleccionados.add(texto);
            } else {
                boton.setBackground(new Color(230, 242, 255));
                boton.setForeground(new Color(0, 102, 204));
                hobbiesSeleccionados.remove(texto);
            }
        });
        return boton;
    }

    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(180, 200, 230));
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public List<String> getHobbiesSeleccionados() {
        return hobbiesSeleccionados;
    }

    // --- Para probarlo individualmente ---
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Ejemplo de Alineación Vertical");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Panel principal con fondo blanco
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Márgenes

        // Elemento centrado
        JLabel titulo = new JLabel("Título Centrado");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espaciador vertical
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));

        // Elemento alineado a la izquierda
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nombreField = new JTextField(15);
        nombreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nombreField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nombreLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(nombreField);
        panel.add(Box.createVerticalStrut(20));

        // Elemento centrado
        JButton boton = new JButton("Aceptar");
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(boton);

        frame.add(panel);
        frame.setVisible(true);
        }
}
