package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuInicioPanel extends BackgroundPanel {

    public MenuInicioPanel(GameFrame frame, MenuPanel parent) {
        super("src/resources/images/fondo.png"); 

        setLayout(new GridBagLayout()); 

        CardPanel card = new CardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(500, 700));

        JLabel title = new JLabel("BAD DOPO-CREAM");
        title.setFont(new Font("SansSerif", Font.BOLD, 52));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel logo = new JLabel();
        ImageIcon icon = new ImageIcon("src/resources/images/logo.png");
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(scaled));
        logo.setAlignmentX(CENTER_ALIGNMENT);

        ModernButton jugar = new ModernButton("JUGAR");
        ModernButton cargar = new ModernButton("CARGAR NIVEL");
        ModernButton controles = new ModernButton("CONTROLES");

        jugar.setAlignmentX(CENTER_ALIGNMENT);
        cargar.setAlignmentX(CENTER_ALIGNMENT);
        controles.setAlignmentX(CENTER_ALIGNMENT);

        jugar.addActionListener(e -> parent.showScreen("modo"));
        cargar.addActionListener(e -> parent.showScreen("nivel"));
        controles.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "WASD/Flechas – Mover\nESPACIO – Crear hielo\nSHIFT+ESPACIO – Romper\nESC – Pausa"));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(logo);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(jugar);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(cargar);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(controles);

        add(card);
    }
}

