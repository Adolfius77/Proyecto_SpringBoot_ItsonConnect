/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Hobby;

/**
 *
 * @author luisb
 */
public class SelectableButtonPanel extends JPanel {
    private final List<JToggleButton> buttons = new ArrayList<>();
    private final List<Hobby> hobbies = new ArrayList<>(); // guardamos la referencia a los hobbies originales

    public SelectableButtonPanel(List<Hobby> hobbies, List<Hobby> preselected) {
        setLayout(new WrapLayout(FlowLayout.LEFT, 10, 8));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));

        for (Hobby hobby : hobbies) {
            JToggleButton btn = createStyledButton(hobby.getNombre());
            this.hobbies.add(hobby); // guardamos el objeto completo

            if (preselected != null && preselected.contains(hobby)) {
                btn.setSelected(true);
                updateButtonStyle(btn);
            }

            buttons.add(btn);
            add(btn);
        }
    }

    private JToggleButton createStyledButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // eliminamos el fondo por defecto
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.BLACK);

        // márgenes internos (padding)
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), // borde redondeado
                new EmptyBorder(6, 14, 6, 14) // top, left, bottom, right
        ));

        // fondo redondeado con color dinámico
        button.setBackground(Color.WHITE);

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(245, 247, 250));
                    button.setOpaque(true);
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(Color.WHITE);
                    button.setOpaque(true);
                    button.repaint();
                }
            }
        });

        // Acción de selección
        button.addActionListener(e -> updateButtonStyle(button));

        return button;
}


    private void updateButtonStyle(JToggleButton button) {
        if (button.isSelected()) {
            button.setOpaque(true);
            button.setBackground(new Color(210, 227, 252)); // azul muy claro
            button.setForeground(new Color(25, 103, 210));
            button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(138, 180, 248), 1, true),
                    new EmptyBorder(6, 14, 6, 14)
            ));
        } else {
            button.setOpaque(true);
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1, true),
                    new EmptyBorder(6, 14, 6, 14)
            ));
        }
        button.repaint();
}

    public List<Hobby> getSelectedHobbies() {
    List<Hobby> selected = new ArrayList<>();

    for (int i = 0; i < buttons.size(); i++) {
        if (buttons.get(i).isSelected()) {
            selected.add(hobbies.get(i)); // hobbies es la lista paralela a buttons
        }
    }
    return selected;
}
}
