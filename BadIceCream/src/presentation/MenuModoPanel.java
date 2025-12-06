package presentation;

import java.awt.*;
import javax.swing.*;
import domain.*;

/**
 * Panel para la selección del modo de juego.
 * Permite elegir entre Player, PvsP, PvsM y MvsM.
 */
public class MenuModoPanel extends BackgroundPanel {

    public MenuModoPanel(GameFrame frame, MenuPanel parent) {
        super("src/resources/images/fondo.png");

        setLayout(new GridBagLayout());

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(2, 2, 25, 25));
        container.setPreferredSize(new Dimension(600, 600));
        container.setOpaque(false);
        container.setBackground(new Color(0, 0, 0, 0));

        ImageOptionButton b1 = new ImageOptionButton("Jugador Solo", "bot.png");
        ImageOptionButton b2 = new ImageOptionButton("Jugador vs Jugador", "human.png");
        ImageOptionButton b3 = new ImageOptionButton("Jugador vs Máquina", "bot.png");
        ImageOptionButton b4 = new ImageOptionButton("Máquina vs Máquina", "human.png");

        // Modo Player
        b1.addActionListener(e -> {
            parent.setGameMode(GameMode.PLAYER);
            parent.showScreen("sabor");
        });
        
        // Modo Player vs Player
        b2.addActionListener(e -> {
            parent.setGameMode(GameMode.PLAYER_VS_PLAYER);
            parent.showScreen("sabor");
        });
        
        // Modo Player vs Machine - Seleccionar perfil de IA
        b3.addActionListener(e -> {
            AIProfile profile = selectAIProfile(frame, "Selecciona el perfil de la IA");
            if (profile != null) {
                parent.setGameMode(GameMode.PLAYER_VS_MACHINE);
                parent.setAIProfile1(profile);
                parent.showScreen("sabor");
            }
        });
        
        // Modo Machine vs Machine - Seleccionar 2 perfiles
        b4.addActionListener(e -> {
            AIProfile profile1 = selectAIProfile(frame, "Selecciona el perfil de la IA 1");
            if (profile1 != null) {
                AIProfile profile2 = selectAIProfile(frame, "Selecciona el perfil de la IA 2");
                if (profile2 != null) {
                    parent.setGameMode(GameMode.MACHINE_VS_MACHINE);
                    parent.setAIProfile1(profile1);
                    parent.setAIProfile2(profile2);
                    parent.showScreen("nivel");
                }
            }
        });

        container.add(b1);
        container.add(b2);
        container.add(b3);
        container.add(b4);

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
    
    private AIProfile selectAIProfile(JFrame parent, String title) {
        Object[] options = {
            "Hungry (Busca frutas)",
            "Fearful (Evita peligros)",
            "Expert (Estrategia óptima)"
        };
        
        int choice = JOptionPane.showOptionDialog(
            parent,
            "Selecciona el perfil de comportamiento:",
            title,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]
        );
        
        switch (choice) {
            case 0: return AIProfile.HUNGRY;
            case 1: return AIProfile.FEARFUL;
            case 2: return AIProfile.EXPERT;
            default: return null;
        }
    }
}