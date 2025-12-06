package presentation;

import java.awt.*;
import javax.swing.*;
import domain.*;

/**
 * Panel para la selección del nivel de juego.
 * Ahora inicia el juego con la modalidad seleccionada.
 */
public class MenuNivelPanel extends BackgroundPanel {

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
            startGameWithMode(frame, parent, 1);
        });

        nivel2.addActionListener(e -> {
            startGameWithMode(frame, parent, 2);
        });

        nivel3.addActionListener(e -> {
            startGameWithMode(frame, parent, 3);
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
    
    private void startGameWithMode(GameFrame frame, MenuPanel parent, int levelNumber) {
        GameMode mode = parent.getGameMode();
        IceCreamFlavour flavor = parent.getSelectedFlavor();
        AIProfile profile1 = parent.getAIProfile1();
        AIProfile profile2 = parent.getAIProfile2();
        
        // Si es modo Player simple, usar el método simplificado
        if (mode == GameMode.PLAYER) {
            frame.getGame().startGame(levelNumber, flavor);
            frame.startGameLoop();
        } else {
            // Para modos multijugador, usar el método completo
            frame.startGame(levelNumber, flavor, mode, profile1, profile2);
        }
    }
}