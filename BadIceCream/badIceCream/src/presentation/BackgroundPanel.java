package presentation;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel que muestra una imagen de fondo.
 * Se utiliza para pintar una imagen escalada que cubre todo el panel.
 */
public class BackgroundPanel extends JPanel {

    private Image background;

    /**
     * Constructor de la clase BackgroundPanel.
     * 
     * @param path La ruta de la imagen de fondo.
     */
    public BackgroundPanel(String path) {
        background = new ImageIcon(path).getImage();
        setOpaque(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}