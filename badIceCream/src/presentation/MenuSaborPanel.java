package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import domain.IceCreamFlavour;

public class MenuSaborPanel extends BackgroundPanel {

    private static final long serialVersionUID = 1L;

    public MenuSaborPanel(GameFrame frame, MenuPanel parent) {
        super("src/resources/images/fondo.png");

        setLayout(new GridBagLayout());

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 3, 25, 25));
        container.setPreferredSize(new Dimension(600, 250));
        container.setOpaque(false);

        ImageOptionButton vainilla = new ImageOptionButton("Vainilla", "vainilla.png");
        ImageOptionButton chocolate = new ImageOptionButton("Chocolate", "chocolate.png");
        ImageOptionButton fresa = new ImageOptionButton("Fresa", "fresa.png");

        vainilla.addActionListener(e -> {
            parent.setSelectedFlavor(IceCreamFlavour.VANILLA);
            parent.showScreen("nivel");
        });
        
        chocolate.addActionListener(e -> {
            parent.setSelectedFlavor(IceCreamFlavour.CHOCOLATE);
            parent.showScreen("nivel");
        });
        
        fresa.addActionListener(e -> {
            parent.setSelectedFlavor(IceCreamFlavour.STRAWBERRY);
            parent.showScreen("nivel");
        });

        container.add(vainilla);
        container.add(chocolate);
        container.add(fresa);

        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                
                g2.dispose();
            }
        };
        
        roundedPanel.setLayout(new GridLayout(1, 3, 25, 25));
        roundedPanel.setPreferredSize(new Dimension(600, 250));
        roundedPanel.setOpaque(false);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        roundedPanel.add(vainilla);
        roundedPanel.add(chocolate);
        roundedPanel.add(fresa);

        add(roundedPanel);
    }
}