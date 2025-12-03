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

/**
 * Panel para la selección del nivel de juego.
 * Permite elegir entre los niveles disponibles.
 */
public class MenuNivelPanel extends BackgroundPanel {

    /**
     * Constructor de la clase MenuNivelPanel.
     * 
     * @param frame  La ventana principal del juego.
     * @param parent El panel contenedor del menú.
     */
    public MenuNivelPanel(GameFrame frame, MenuPanel parent) {
        super("src/resources/images/fondo.png");

        setLayout(new GridBagLayout());

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 3, 25, 25));
        container.setPreferredSize(new Dimension(600, 250));
        container.setOpaque(false);

        ImageOptionButton nivel1 = new ImageOptionButton("Nivel 1", "ice.png");
        ImageOptionButton nivel2 = new ImageOptionButton("Nivel 2", "ice.png");
        ImageOptionButton nivel3 = new ImageOptionButton("Nivel 3", "ice.png");

        nivel1.addActionListener(e -> {
            frame.startGame(parent.getSelectedFlavor());
        });

        nivel2.addActionListener(e -> {
            frame.startGame(parent.getSelectedFlavor());
        });

        nivel3.addActionListener(e -> {
            frame.startGame(parent.getSelectedFlavor());
        });

        container.add(nivel1);
        container.add(nivel2);
        container.add(nivel3);

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

        roundedPanel.add(nivel1);
        roundedPanel.add(nivel2);
        roundedPanel.add(nivel3);

        add(roundedPanel);
    }
}