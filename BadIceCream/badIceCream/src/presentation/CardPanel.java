package presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/*
 * esto es solo para crear cajas redondeadas xd
 * es como una clase auxiliar
 */
public class CardPanel extends JPanel {
    private int radius = 30;

    public CardPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); 
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo redondeado
        g2.setColor(new Color(0, 0, 0, 160)); 
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
    }
}