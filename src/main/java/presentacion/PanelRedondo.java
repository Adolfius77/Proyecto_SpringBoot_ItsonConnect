/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Un JPanel personalizado que tiene esquinas redondeadas y un borde.
 * @author adoil (¡y Asistente de programación!)
 */
public class PanelRedondo extends JPanel {

    private int radius;
    private Color borderColor; // --- NUEVO --- Variable para el color del borde

    /**
     * Constructor.
     */
    public PanelRedondo() {
        super();
        this.radius = 20; // Un radio por defecto de 20
        this.borderColor = Color.BLACK; // --- NUEVO --- Color de borde por defecto
        
        // Hacemos que el JPanel no pinte su fondo rectangular por defecto.
        setOpaque(false);
    }

    // --- Getters y Setters ---

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint(); // Pide al panel que se vuelva a dibujar
    }

    // --- NUEVO --- Getters y Setters para el color del borde
    
    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); // Vuelve a dibujar si cambia el color del borde
    }
    
    /**
     * Dibuja el fondo redondeado del panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Llamamos al super.paintComponent() para que dibuje 
        // correctamente cualquier componente HIJO dentro de este panel.
        super.paintComponent(g); 
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja el RELLENO (fondo)
        g2.setColor(getBackground());
        g2.fillRoundRect(
                0, 0, // Posición
                getWidth(), // Ancho
                getHeight(), // Alto
                radius, radius // Radio
        );
    }

    /**
     * --- NUEVO ---
     * Dibuja el borde redondeado del panel.
     * Este método se llama DESPUÉS de paintComponent.
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Establece el color del borde
        g2.setColor(getBorderColor()); 
        
        // 2. Dibuja el CONTORNO (borde)
        // Se resta 1 al ancho y alto para que el borde de 1px
        // quede completamente dentro de los límites del componente.
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }
}