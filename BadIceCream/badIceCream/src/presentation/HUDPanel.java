package presentation;

import domain.Level;
import domain.Player;
import domain.Fruit;
import domain.Game;
import domain.GameState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel del HUD (Head-Up Display)
 */
public class HUDPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel fruitsLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton menuButton;
    private JButton musicButton;
    private Game game;

    public HUDPanel(Game game, GameFrame gameFrame) {
        this.game = game;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 60));

        fruitsLabel = new JLabel("Frutas: 0/0");
        fruitsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        scoreLabel = new JLabel("Puntos: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        timeLabel = new JLabel("Tiempo: 3:00");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        pauseButton = new JButton("||");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 16));
        pauseButton.addActionListener(e -> togglePause());

        resetButton = new JButton("R");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(e -> {
            game.reset();
            gameFrame.requestFocus();
        });

        musicButton = new JButton("♫");
        musicButton.setFont(new Font("Arial", Font.BOLD, 16));
        musicButton.setToolTipText("Activar/Silenciar música");
        musicButton.addActionListener(e -> toggleMusic());

        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton.addActionListener(e -> gameFrame.showMenu());

        add(fruitsLabel);
        add(scoreLabel);
        add(timeLabel);
        add(pauseButton);
        add(resetButton);
        add(musicButton);
        add(menuButton);
    }

    public void update() {
        Level level = game.getLevel();
        Player player = game.getPlayer();

        if (level != null && player != null) {
            int collected = (int) level.getFruits().stream().filter(Fruit::isCollected).count();
            int total = level.getFruits().size();
            fruitsLabel.setText("Frutas: " + collected + "/" + total);

            scoreLabel.setText("Puntos: " + player.getScore());

            int time = game.getTimeRemaining();
            int mins = time / 60;
            int secs = time % 60;
            timeLabel.setText(String.format("Tiempo: %d:%02d", mins, secs));

            pauseButton.setText(game.getState() == GameState.PAUSED ? ">" : "||");

            // Actualizar botón de música
            musicButton.setText(MusicManager.isMuted() ? "♫̸" : "♫");
        }
    }

    private void togglePause() {
        if (game.getState() == GameState.PLAYING) {
            game.pause();
        } else if (game.getState() == GameState.PAUSED) {
            game.resume();
        }
    }

    private void toggleMusic() {
        MusicManager.toggleMute();
        musicButton.setText(MusicManager.isMuted() ? "♫̸" : "♫");
    }
}