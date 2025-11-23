package presentation;

import domain.IceCreamFlavour;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Panel de menú principal
 */
class MenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JButton startButton;
    private JComboBox<String> modeComboBox;
    private JComboBox<IceCreamFlavour> flavorComboBox;
    private GameFrame gameFrame;
    
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(100, 180, 255));
        
        // Título
        JLabel titleLabel = new JLabel("Bad Dopo-Cream");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("DOPO 2025-2");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.WHITE);
        
        // Selección de modalidad
        JLabel modeLabel = new JLabel("Modalidad de Juego:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel.setForeground(Color.WHITE);
        
        String[] modes = {"Player vs Player", "Player vs Machine", "Machine vs Machine"};
        modeComboBox = new JComboBox<>(modes);
        modeComboBox.setMaximumSize(new Dimension(300, 40));
        modeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Selección de sabor
        JLabel flavorLabel = new JLabel("Sabor de Helado:");
        flavorLabel.setFont(new Font("Arial", Font.BOLD, 18));
        flavorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flavorLabel.setForeground(Color.WHITE);
        
        flavorComboBox = new JComboBox<>(IceCreamFlavour.values());
        flavorComboBox.setMaximumSize(new Dimension(300, 40));
        flavorComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botón de inicio
        startButton = new JButton("Iniciar Juego");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(34, 197, 94));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startGame());
        
        // Instrucciones
        JTextArea instructions = new JTextArea(
            "Controles:\n" +
            "- Flechas o WASD: Mover\n" +
            "- ESPACIO: Crear bloque de hielo\n" +
            "- SHIFT + ESPACIO: Destruir bloque\n" +
            "- P o ESC: Pausar"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(255, 255, 255, 200));
        instructions.setFont(new Font("Arial", Font.PLAIN, 14));
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Agregar componentes con espaciado
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(subtitleLabel);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(modeLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(modeComboBox);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(flavorLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(flavorComboBox);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(instructions);
        add(Box.createVerticalGlue());
    }
    
    private void startGame() {
        IceCreamFlavour flavor = (IceCreamFlavour) flavorComboBox.getSelectedItem();
        if (flavor != null) {
            gameFrame.startGame(flavor);
        }
    }
}
