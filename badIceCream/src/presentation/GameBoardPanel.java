package presentation;

import domain.GameState;
import domain.Fruit;
import domain.Enemy;
import domain.Player;
import domain.Game;
import domain.Position;
import domain.Level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Panel del tablero de juego
 */
class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public Game game;
    private static final int CELL_SIZE = 40;
    
    public GameBoardPanel(Game game) {
        this.game = game;
        setBackground(new Color(200, 230, 255));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Level level = game.getLevel();
        if (level == null) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = level.getWidth();
        int height = level.getHeight();
        
        // Calcular offset para centrar el tablero
        int offsetX = (getWidth() - width * CELL_SIZE) / 2;
        int offsetY = (getHeight() - height * CELL_SIZE) / 2;
        
        // Dibujar celdas
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);
                int drawX = offsetX + x * CELL_SIZE;
                int drawY = offsetY + y * CELL_SIZE;
                
                // Color de fondo
                if (level.isWall(pos)) {
                    g2d.setColor(new Color(70, 70, 70));
                } else if (level.isIceBlock(pos)) {
                    g2d.setColor(new Color(173, 216, 230));
                } else {
                    g2d.setColor(new Color(240, 248, 255));
                }
                g2d.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                
                // Borde
                g2d.setColor(Color.GRAY);
                g2d.drawRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                
                // Contenido de la celda
                drawCellContent(g2d, pos, drawX, drawY);
            }
        }
        
        // Dibujar overlay de estado
        drawStateOverlay(g2d);
    }
    
    private void drawCellContent(Graphics2D g2d, Position pos, int x, int y) {
        Level level = game.getLevel();
        Player player = game.getPlayer();
        
        // Dibujar jugador
        if (player != null && player.getPosition() != null && player.getPosition().equals(pos)) {
            g2d.setColor(Color.decode(player.getFlavor().getColor()));
            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("P", x + 13, y + 27);
        }
        
        // Dibujar enemigos
        if (level.getEnemies() != null) {
            for (Enemy enemy : level.getEnemies()) {
                if (enemy != null && enemy.getPosition() != null && 
                    enemy.getPosition().equals(pos) && enemy.isAlive()) {
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    g2d.drawString(enemy.getSymbol(), x + 13, y + 27);
                }
            }
        }
        
        // Dibujar frutas
        if (level.getFruits() != null) {
            for (Fruit fruit : level.getFruits()) {
                if (fruit != null && fruit.getPosition() != null && 
                    fruit.getPosition().equals(pos) && !fruit.isCollected()) {
                    Color fruitColor;
                    switch (fruit.getFruitType()) {
                        case GRAPE:
                            fruitColor = new Color(138, 43, 226);
                            break;
                        case BANANA:
                            fruitColor = new Color(255, 255, 0);
                            break;
                        default:
                            fruitColor = Color.GREEN;
                    }
                    g2d.setColor(fruitColor);
                    g2d.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.drawString(fruit.getSymbol(), x + 15, y + 27);
                }
            }
        }
    }
    
    private void drawStateOverlay(Graphics2D g2d) {
        GameState state = game.getState();
        String message = null;
        Color bgColor = null;
        
        switch (state) {
            case PAUSED:
                message = "PAUSADO";
                bgColor = new Color(0, 0, 0, 180);
                break;
            case GAME_OVER:
                message = "GAME OVER";
                bgColor = new Color(139, 0, 0, 200);
                break;
            case VICTORY:
                message = "VICTORIA!";
                bgColor = new Color(0, 128, 0, 200);
                break;
            default:
                break;
        }
        
        if (message != null) {
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(message);
            g2d.drawString(message, (getWidth() - msgWidth) / 2, getHeight() / 2);
            
            if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
                Player player = game.getPlayer();
                if (player != null) {
                    String scoreMsg = "Puntos: " + player.getScore();
                    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                    fm = g2d.getFontMetrics();
                    int scoreWidth = fm.stringWidth(scoreMsg);
                    g2d.drawString(scoreMsg, (getWidth() - scoreWidth) / 2, getHeight() / 2 + 50);
                }
            }
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (game.getLevel() != null) {
            int width = game.getLevel().getWidth() * CELL_SIZE;
            int height = game.getLevel().getHeight() * CELL_SIZE;
            return new Dimension(width + 100, height + 100);
        }
        return new Dimension(800, 600);
    }
}
