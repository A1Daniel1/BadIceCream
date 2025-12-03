package presentation;

import java.awt.*;
import javax.swing.*;

/**
 * Panel para la selección del modo de juego.
 * Permite elegir entre diferentes configuraciones de jugadores (actualmente
 * solo visual).
 */
public class MenuModoPanel extends BackgroundPanel {

    /**
     * Constructor de la clase MenuModoPanel.
     * 
     * @param frame  La ventana principal del juego.
     * @param parent El panel contenedor del menú.
     */
    public MenuModoPanel(GameFrame frame, MenuPanel parent) {
        super("src/resources/images/fondo.png");

        setLayout(new GridBagLayout());

        // Panel contenedor simple
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(2, 2, 25, 25));
        container.setPreferredSize(new Dimension(600, 600));
        container.setOpaque(false);
        container.setBackground(new Color(0, 0, 0, 0));

        // Botones
        ImageOptionButton b1 = new ImageOptionButton("Jugador Solo", "bot.png");
        ImageOptionButton b2 = new ImageOptionButton("Jugador vs Jugador", "human.png");
        ImageOptionButton b3 = new ImageOptionButton("Jugador vs Máquina", "bot.png");
        ImageOptionButton b4 = new ImageOptionButton("Máquina vs Máquina", "human.png");

        // Acciones
        b1.addActionListener(e -> parent.showScreen("sabor"));
        b2.addActionListener(e -> parent.showScreen("sabor"));
        b3.addActionListener(e -> parent.showScreen("sabor"));
        b4.addActionListener(e -> parent.showScreen("sabor"));

        container.add(b1);
        container.add(b2);
        container.add(b3);
        container.add(b4);

        // Crear panel con fondo redondeado
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo redondeado
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                g2.dispose();
            }
        };

        roundedPanel.setLayout(new GridLayout(2, 2, 25, 25));
        roundedPanel.setPreferredSize(new Dimension(600, 600));
        roundedPanel.setOpaque(false);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        roundedPanel.add(b1);
        roundedPanel.add(b2);
        roundedPanel.add(b3);
        roundedPanel.add(b4);

        add(roundedPanel);
    }
}