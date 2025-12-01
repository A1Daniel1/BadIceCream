package presentation;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class ImageOptionButton extends JButton {

    private Image image;

    public ImageOptionButton(String text, String imageName) {
        super(text);

        image = loadIcon(imageName);
        if (image != null) {
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        }

        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(BOTTOM);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setPreferredSize(new Dimension(240, 240));

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private Image loadIcon(String name) {
        java.io.File f = new java.io.File("src/resources/images/" + name);
        if (f.exists()) {
            return new ImageIcon(f.getAbsolutePath()).getImage();
        }

        URL url = getClass().getResource("/resources/images/" + name);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }

        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        if (getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 60));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        }

        g2.dispose();
        
        super.paintComponent(g);
    }
}