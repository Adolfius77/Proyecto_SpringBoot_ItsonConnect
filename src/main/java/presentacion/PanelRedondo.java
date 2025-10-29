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
 * Un JPanel personalizado que tiene esquinas redondeadas.
 * @author adoil (¡y Asistente de programación!)
 */
public class PanelRedondo extends JPanel {

    /**
     * El radio (curvatura) de las esquinas.
     * Un valor más alto lo hace más redondo.
     */
    private int radius;

    /**
     * Constructor.
     * Establece un radio por defecto y hace el panel transparente.
     */
    public PanelRedondo() {
        super(); // Llama al constructor de JPanel
        this.radius = 20; // Un radio por defecto de 20
        
        // ¡¡ESTA ES LA LÍNEA MÁS IMPORTANTE!!
        // Hacemos que el JPanel no pinte su fondo rectangular por defecto.
        // Si no hacemos esto, veremos un rectángulo con esquinas redondeadas encima.
        setOpaque(false);
    }

    // --- Getter y Setter para el radio ---

    public int getRadius() {
        return radius;
    }

    /**
     * Establece un nuevo radio para las esquinas.
     * @param radius El nuevo radio (ej. 20, 30, 50...)
     */
    public void setRadius(int radius) {
        this.radius = radius;
        repaint(); // Pide al panel que se vuelva a dibujar con el nuevo radio
    }

    /**
     * Este es el método donde ocurre la magia del dibujo.
     * Se llama automáticamente cada vez que el panel necesita pintarse.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Primero, llamamos al método original de JPanel
        // Esto es crucial para que los componentes *dentro* de este panel se dibujen
        super.paintComponent(g); 
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Activamos el Antialiasing para bordes suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Usamos el color de fondo que el usuario haya establecido (ej. con setBackground)
        g2.setColor(getBackground());
        
        // Dibujamos el rectángulo redondeado
        // Usamos las dimensiones completas del panel
        g2.fillRoundRect(
                0,                // Coordenada X (esquina superior izquierda)
                0,                // Coordenada Y (esquina superior izquierda)
                getWidth(),       // Ancho del panel
                getHeight(),      // Alto del panel
                radius,           // Radio (curva) del ancho de la esquina
                radius            // Radio (curva) del alto de la esquina
        );
    }
}