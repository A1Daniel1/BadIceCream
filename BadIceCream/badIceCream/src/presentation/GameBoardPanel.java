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
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

import javax.swing.JPanel;

/**
 * Panel principal del juego que renderiza el tablero, los personajes y objetos.
 * Se encarga de dibujar el nivel, el jugador, los enemigos y la interfaz
 * superpuesta (HUD).
 */
public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public Game game;
    private static final int CELL_SIZE = 40;

    private Image fresaImage;
    private Image chocolateImage;
    private Image vainillaImage;
    private Image iceImage;
    private Image muroImage;
    private Image fogataImage;
    private Image baldosaCaliente;
    private Image calamarImage;
    private Image trollImage;
    private Image macetaImage;
    private Image platanoImage;
    private Image uvaImage;
    private Image piñaImage;
    private Image cerezaImage;
    private Image defaulFruitImage;
    private Image nieveImage;

    /**
     * Constructor de la clase GameBoardPanel.
     * 
     * @param game La instancia del juego.
     */
    public GameBoardPanel(Game game) {
        this.game = game;
        setBackground(new Color(200, 230, 255));
        loadImages();
    }

    /**
     * Carga las imágenes de los recursos.
     */
    private void loadImages() {
        try {
            // helados
            fresaImage = ImageIO.read(getClass().getResource("/resources/images/fresa.png"));
            chocolateImage = ImageIO.read(getClass().getResource("/resources/images/chocolate.png"));
            vainillaImage = ImageIO.read(getClass().getResource("/resources/images/vainilla.png"));

            // muros
            iceImage = ImageIO.read(getClass().getResource("/resources/images/ice.png"));
            muroImage = ImageIO.read(getClass().getResource("/resources/images/muro.png"));

            // objetos
            fogataImage = ImageIO.read(getClass().getResource("/resources/images/fogata.png"));
            baldosaCaliente = ImageIO.read(getClass().getResource("/resources/images/baldosaCaliente.png"));
            nieveImage = ImageIO.read(getClass().getResource("/resources/images/nieve.png"));

            // enemigos
            calamarImage = ImageIO.read(getClass().getResource("/resources/images/calamar.png"));
            trollImage = ImageIO.read(getClass().getResource("/resources/images/troll.png"));
            macetaImage = ImageIO.read(getClass().getResource("/resources/images/maceta.png"));

            // frutas
            platanoImage = ImageIO.read(getClass().getResource("/resources/images/platano.png"));
            uvaImage = ImageIO.read(getClass().getResource("/resources/images/uva.png"));
            piñaImage = ImageIO.read(getClass().getResource("/resources/images/piña.png"));
            cerezaImage = ImageIO.read(getClass().getResource("/resources/images/cereza.png"));
            defaulFruitImage = ImageIO.read(getClass().getResource("/resources/images/platano.png"));

        } catch (Exception e) {
            System.out.println("Error cargando imagen. Verifica que la carpeta 'images' esté DENTRO de 'src'");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Level level = game.getLevel();
        // Removed: if (level == null) return;

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

                Image imgToDraw;

                // Color de fondo
                if (level.isWall(pos)) {
                    imgToDraw = muroImage;
                } else if (level.isIceBlock(pos)) {
                    imgToDraw = iceImage;
                } else {
                    imgToDraw = nieveImage;
                }

                // DIBUJAR LA IMAGEN DEL ESCENARIO
                if (imgToDraw != null) {
                    g2d.drawImage(imgToDraw, drawX, drawY, CELL_SIZE, CELL_SIZE, this);
                } else {
                    g2d.setColor(new Color(240, 248, 255));
                    g2d.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                }

                drawCellContent(g2d, pos, drawX, drawY);
            }
        }

        drawStateOverlay(g2d);
    }

    /**
     * Dibuja el contenido de una celda (jugador, enemigos, frutas).
     * 
     * @param g2d Contexto gráfico.
     * @param pos Posición lógica en el tablero.
     * @param x   Coordenada X de dibujo.
     * @param y   Coordenada Y de dibujo.
     */
    private void drawCellContent(Graphics2D g2d, Position pos, int x, int y) {
        Level level = game.getLevel();
        Player player = game.getPlayer();

        // Dibujar jugador
        // Removed: player != null && player.getPosition() != null
        if (player.getPosition().equals(pos)) {
            switch (player.getFlavor()) {
                case CHOCOLATE:
                    g2d.drawImage(chocolateImage, x, y, CELL_SIZE, CELL_SIZE, this);
                    break;
                case STRAWBERRY:
                    g2d.drawImage(fresaImage, x, y, CELL_SIZE, CELL_SIZE, this);
                    break;
                case VANILLA:
                    g2d.drawImage(vainillaImage, x, y, CELL_SIZE, CELL_SIZE, this);
                    break;
            }
        }

        // Dibujar enemigos
        // Removed: if (level.getEnemies() != null)
        for (Enemy enemy : level.getEnemies()) {
            // Removed: enemy != null && enemy.getPosition() != null
            if (enemy.isAlive() && enemy.getPosition().equals(pos)) {
                switch (enemy.getEnemyType()) {
                    case ORANGE_SQUID:
                        g2d.drawImage(calamarImage, x, y, CELL_SIZE, CELL_SIZE, this);
                        break;
                    case POT:
                        g2d.drawImage(macetaImage, x, y, CELL_SIZE, CELL_SIZE, this);
                        break;
                    case TROLL:
                        g2d.drawImage(trollImage, x, y, CELL_SIZE, CELL_SIZE, this);
                        break;
                    default:
                        break;
                }
            }
        }

        // Dibujar frutas
        // Removed: if (level.getFruits() != null)
        for (Fruit fruit : level.getFruits()) {
            // Removed: fruit != null && fruit.getPosition() != null
            if (fruit.getPosition().equals(pos) && !fruit.isCollected()) {
                Image imgToDraw = defaulFruitImage;

                switch (fruit.getFruitType()) {
                    case GRAPE:
                        imgToDraw = uvaImage;
                        break;
                    case BANANA:
                        imgToDraw = platanoImage;
                        break;
                    case CHERRY:
                        imgToDraw = cerezaImage;
                        break;
                    case PINEAPPLE:
                        imgToDraw = piñaImage;
                        break;
                    default:
                        break;
                }

                if (imgToDraw != null) {
                    g2d.drawImage(imgToDraw, x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, this);
                } else {
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                }
            }
        }
    }

    /**
     * Dibuja una superposición según el estado del juego (Pausa, Game Over,
     * Victoria).
     * 
     * @param g2d Contexto gráfico.
     */
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
                // Removed: if (player != null)
                String scoreMsg = "Puntos: " + player.getScore();
                g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                fm = g2d.getFontMetrics();
                int scoreWidth = fm.stringWidth(scoreMsg);
                g2d.drawString(scoreMsg, (getWidth() - scoreWidth) / 2, getHeight() / 2 + 50);
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