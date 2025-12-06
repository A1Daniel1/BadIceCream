package presentation;

import domain.*;
import java.awt.*;
import javax.swing.*;

/**
 * Panel del HUD actualizado para mostrar información de ambos jugadores.
 */
public class HUDPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel fruitsLabel;
    private JLabel scoreLabel;
    private JLabel score2Label;
    private JLabel timeLabel;
    private JLabel modeLabel;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton menuButton;
    private JButton musicButton;
    private Game game;

    public HUDPanel(Game game, GameFrame gameFrame) {
        this.game = game;
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 60));

        modeLabel = new JLabel("Modo: Player");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        modeLabel.setForeground(new Color(100, 100, 100));

        fruitsLabel = new JLabel("Frutas: 0/0");
        fruitsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        scoreLabel = new JLabel("P1: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(0, 100, 200));

        score2Label = new JLabel("P2: 0");
        score2Label.setFont(new Font("Arial", Font.BOLD, 16));
        score2Label.setForeground(new Color(200, 0, 100));

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

        menuButton = new JButton("Menú");
        menuButton.setFont(new Font("Arial", Font.BOLD, 16));
        menuButton.addActionListener(e -> gameFrame.showMenu());

        add(modeLabel);
        add(fruitsLabel);
        add(scoreLabel);
        
        // Solo mostrar P2 en modos multijugador
        if (game.getGameMode().isMultiplayer()) {
            add(score2Label);
        }
        
        add(timeLabel);
        add(pauseButton);
        add(resetButton);
        add(musicButton);
        add(menuButton);
    }

    public void update() {
        Level level = game.getLevel();
        Player player = game.getPlayer();
        Player player2 = game.getPlayer2();

        if (level != null && player != null) {
            // Actualizar etiqueta de modo
            String modeName = game.getGameMode().getName();
            if (game.getGameMode() == GameMode.PLAYER_VS_MACHINE && game.getAIPlayer1() != null) {
                modeName += " (" + game.getAIPlayer1().getProfile().getName() + ")";
            } else if (game.getGameMode() == GameMode.MACHINE_VS_MACHINE) {
                if (game.getAIPlayer1() != null && game.getAIPlayer2() != null) {
                    modeName += " (" + game.getAIPlayer1().getProfile().getName() + 
                               " vs " + game.getAIPlayer2().getProfile().getName() + ")";
                }
            }
            modeLabel.setText("Modo: " + modeName);
            
            // Frutas
            int collected = (int) level.getFruits().stream().filter(Fruit::isCollected).count();
            int total = level.getFruits().size();
            fruitsLabel.setText("Frutas: " + collected + "/" + total);

            // Puntuaciones
            String p1Status = player.isAlive() ? "" : " [MUERTO]";
            scoreLabel.setText("P1: " + player.getScore() + p1Status);

            if (game.getGameMode().isMultiplayer() && player2 != null) {
                String p2Status = player2.isAlive() ? "" : " [MUERTO]";
                score2Label.setText("P2: " + player2.getScore() + p2Status);
            }

            // Tiempo
            int time = game.getTimeRemaining();
            int mins = time / 60;
            int secs = time % 60;
            timeLabel.setText(String.format("Tiempo: %d:%02d", mins, secs));

            pauseButton.setText(game.getState() == GameState.PAUSED ? ">" : "||");
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