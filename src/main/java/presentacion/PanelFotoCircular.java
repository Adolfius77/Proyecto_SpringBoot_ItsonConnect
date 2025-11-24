package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelFotoCircular extends JPanel {

    private Image imagen;
    private boolean tieneImagen = false;
    
    // Colores del placeholder (el diseño gris de tu imagen)
    private final Color colorFondoPlaceholder = new Color(229, 231, 235); // Gris claro
    private final Color colorIconoPlaceholder = new Color(156, 163, 175); // Gris más oscuro

    public PanelFotoCircular() {
        // Hacemos el panel transparente para que solo se vea el círculo
        setOpaque(false);
        setPreferredSize(new Dimension(150, 150)); // Tamaño por defecto
    }

    /**
     * Establece una imagen a partir de un objeto Image.
     */
    public void setImagen(Image img) {
        this.imagen = img;
        this.tieneImagen = (img != null);
        repaint(); // Vuelve a pintar el componente con la nueva foto
    }

    /**
     * Método útil para tu proyecto: Recibe los bytes de la BD o Base64 decodificado
     */
    public void setImagenBytes(byte[] bytes) {
        try {
            if (bytes != null && bytes.length > 0) {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                BufferedImage buffImg = ImageIO.read(bis);
                setImagen(buffImg);
            } else {
                setImagen(null); // Volver al estado placeholder
            }
        } catch (Exception e) {
            e.printStackTrace();
            setImagen(null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        // Activar suavizado para que el círculo no se vea pixelado (Antialiasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int diametro = Math.min(getWidth(), getHeight());
        int x = (getWidth() - diametro) / 2;
        int y = (getHeight() - diametro) / 2;

        // Creamos la forma del círculo
        Ellipse2D.Double circulo = new Ellipse2D.Double(x, y, diametro, diametro);

        // --- CASO 1: EL USUARIO TIENE FOTO ---
        if (tieneImagen && imagen != null) {
            // Usamos el círculo como "máscara" (clip)
            g2.setClip(circulo);
            
            // Dibujamos la imagen estirada para cubrir todo el círculo
            g2.drawImage(imagen, x, y, diametro, diametro, this);
            
            // Quitamos el clip para poder dibujar bordes si quisiéramos después
            g2.setClip(null);
            
            // Opcional: Dibujar un borde fino alrededor de la foto
            // g2.setColor(Color.GRAY);
            // g2.setStroke(new BasicStroke(1));
            // g2.draw(circulo);
        } 
        
        // --- CASO 2: NO HAY FOTO (DIBUJAR PLACEHOLDER TIPO TU IMAGEN) ---
        else {
            // 1. Dibujar fondo gris circular
            g2.setColor(colorFondoPlaceholder);
            g2.fill(circulo);

            // 2. Dibujar la silueta de "usuario" (cabeza y cuerpo)
            g2.setColor(colorIconoPlaceholder);
            
            // Cabeza (círculo pequeño centrado arriba)
            int headSize = diametro / 3;
            int headX = x + (diametro - headSize) / 2;
            int headY = y + (diametro / 4);
            g2.fillOval(headX, headY, headSize, headSize);
            
            // Cuerpo (medio óvalo abajo)
            int bodyWidth = (int) (diametro * 0.6);
            int bodyHeight = (int) (diametro * 0.4);
            int bodyX = x + (diametro - bodyWidth) / 2;
            int bodyY = y + (diametro / 2) + (diametro / 10);
            
            // Usamos clip de nuevo para que el cuerpo no se salga del círculo principal
            g2.setClip(circulo);
            
            // Dibujamos un arco para los hombros
            g2.setStroke(new BasicStroke(headSize / 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // Aquí dibujamos un óvalo o arco simulando los hombros
            g2.drawArc(bodyX, bodyY, bodyWidth, bodyHeight * 2, 0, 180);
            
            g2.setClip(null);
        }
    }
}