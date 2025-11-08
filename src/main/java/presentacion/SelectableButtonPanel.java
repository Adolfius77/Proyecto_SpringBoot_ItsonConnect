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

/**
 *
 * @author luisb
 */
public class SelectableButtonPanel extends JPanel {
    private final List<JToggleButton> buttons = new ArrayList<>();

    public SelectableButtonPanel(String[] items, List<String> preselected) {
        setLayout(new WrapLayout(FlowLayout.LEFT, 10, 8));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));

        for (String item : items) {
            JToggleButton btn = createStyledButton(item);
            if (preselected != null && preselected.contains(item)) {
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
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> updateButtonStyle(button));
        return button;
    }

    private void updateButtonStyle(JToggleButton button) {
        if (button.isSelected()) {
            button.setBackground(new Color(66, 133, 244)); // Azul suave
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(new Color(40, 100, 200), 1, true));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        }
    }

    public List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        for (JToggleButton btn : buttons) {
            if (btn.isSelected()) selected.add(btn.getText());
        }
        return selected;
    }
}
