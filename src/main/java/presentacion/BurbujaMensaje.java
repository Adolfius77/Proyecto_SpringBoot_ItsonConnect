package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BurbujaMensaje extends JPanel {

    private final int RADIUS = 15;

    public BurbujaMensaje(String contenido, boolean esMio) {
        super();
        setLayout(new BorderLayout());
        setOpaque(false); 

        JTextArea texto = new JTextArea(contenido);
        texto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        texto.setWrapStyleWord(true);
        texto.setLineWrap(true);
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setOpaque(false); 

        if (esMio) {
            Color azul = new Color(0, 122, 255);
            setBackground(azul);
            texto.setForeground(Color.WHITE);
            texto.setBackground(azul); 
        } else {
            Color gris = new Color(230, 230, 235);
            setBackground(gris);
            texto.setForeground(Color.BLACK);
            texto.setBackground(gris);
        }

        int anchoMaximo = 250;
        Dimension tamanoNatural = texto.getPreferredSize();

        if (tamanoNatural.width > anchoMaximo) {
            texto.setSize(new Dimension(anchoMaximo, Short.MAX_VALUE));
            int alturaNecesaria = texto.getPreferredSize().height;
            texto.setPreferredSize(new Dimension(anchoMaximo, alturaNecesaria));
        }

        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        add(texto, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);

        super.paintComponent(g);
    }
}