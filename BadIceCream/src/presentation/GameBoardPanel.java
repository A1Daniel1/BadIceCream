package presentation;

import domain.*;
import java.awt.*;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

/**
 * Panel actualizado para renderizar ambos jugadores en modos multijugador.
 */
public class GameBoardPanel extends JPanel implements GameListener {
    private static final long serialVersionUID = 1L;
    public Game game;
    private static final int CELL_SIZE = 40;

    // Imágenes (mantenidas del código original)
    private Image fresaImage, chocolateImage, vainillaImage;
    private Image iceImage, muroImage;
    private Image fogataImage, fogataApagadaImage, baldosaCaliente, nieveImage;
    private Image calamarImage, trollImage, macetaImage, narvalImage;
    private Image platanoImage, uvaImage, piñaImage, cerezaImage, cactusImage, cactusPuasImage, defaulFruitImage;

    public GameBoardPanel(Game game) {
        this.game = game;
        setBackground(new Color(200, 230, 255));
        loadImages();
        game.addListener(this);
    }

    private void loadImages() {
        try {
            fresaImage = loadImage("/resources/images/fresa.png");
            chocolateImage = loadImage("/resources/images/chocolate.png");
            vainillaImage = loadImage("/resources/images/vainilla.png");
            iceImage = loadImage("/resources/images/ice.png");
            muroImage = loadImage("/resources/images/muro.png");
            fogataImage = loadImage("/resources/images/fogata.png");
            baldosaCaliente = loadImage("/resources/images/baldosaCaliente.png");
            nieveImage = loadImage("/resources/images/nieve.png");
            fogataApagadaImage = loadImageWithFallback("/resources/images/fogataApagada.png",
                    fogataImage, 0.5f);
            calamarImage = loadImage("/resources/images/calamar.png");
            trollImage = loadImage("/resources/images/troll.png");
            macetaImage = loadImage("/resources/images/maceta.png");
            narvalImage = loadImageWithFallback("/resources/images/narval.png", macetaImage, 1.0f);
            platanoImage = loadImage("/resources/images/platano.png");
            uvaImage = loadImage("/resources/images/uva.png");
            piñaImage = loadImageWithFallback("/resources/images/piña.png", platanoImage, 1.0f);
            cerezaImage = loadImageWithFallback("/resources/images/cereza.png", platanoImage, 1.0f);
            cactusImage = loadImageWithFallback("/resources/images/cactus.png", platanoImage, 1.0f);
            cactusPuasImage = loadImageWithFallback("/resources/images/cactusPuas.png", cactusImage, 1.0f);
            defaulFruitImage = platanoImage;
        } catch (Exception e) {
            System.err.println("Error cargando imágenes: " + e.getMessage());
        }
    }

    private Image loadImage(String path) throws Exception {
        return ImageIO.read(getClass().getResource(path));
    }

    private Image loadImageWithFallback(String path, Image fallback, float opacity) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            return fallback;
        }
    }

    @Override
    public void onGameUpdated() {
        repaint();
    }

    @Override
    public void onGameOver() {
        repaint();
    }

    @Override
    public void onVictory() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (game == null || game.getLevel() == null) {
            return;
        }

        Level level = game.getLevel();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = level.getWidth();
        int height = level.getHeight();
        int offsetX = (getWidth() - width * CELL_SIZE) / 2;
        int offsetY = (getHeight() - height * CELL_SIZE) / 2;

        // Dibujar celdas
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);
                int drawX = offsetX + x * CELL_SIZE;
                int drawY = offsetY + y * CELL_SIZE;
                drawCell(g2d, pos, drawX, drawY, level);
            }
        }

        drawStateOverlay(g2d);
    }

    private void drawCell(Graphics2D g2d, Position pos, int x, int y, Level level) {
        Image bgImage = getCellBackground(pos, level);
        if (bgImage != null) {
            g2d.drawImage(bgImage, x, y, CELL_SIZE, CELL_SIZE, this);
        } else {
            g2d.setColor(new Color(240, 248, 255));
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
        drawCellContent(g2d, pos, x, y, level);
    }

    private Image getCellBackground(Position pos, Level level) {
        if (level.isWall(pos)) {
            return muroImage;
        } else if (level.isIceBlock(pos)) {
            return iceImage;
        } else if (level.isHotTile(pos)) {
            return baldosaCaliente;
        } else if (level.isCampfire(pos)) {
            Campfire fire = level.getCampfireAt(pos);
            return (fire != null && fire.isLit()) ? fogataImage : fogataApagadaImage;
        } else {
            return nieveImage;
        }
    }

    private void drawCellContent(Graphics2D g2d, Position pos, int x, int y, Level level) {
        // Dibujar jugadores
        Player player = game.getPlayer();
        Player player2 = game.getPlayer2();

        if (player != null && player.getPosition().equals(pos)) {
            drawPlayer(g2d, player, x, y);
        }

        if (player2 != null && player2.getPosition().equals(pos)) {
            drawPlayer(g2d, player2, x, y);

            // Si ambos jugadores están en la misma posición, dibujar indicador
            if (player != null && player.getPosition().equals(pos)) {
                g2d.setColor(new Color(255, 255, 0, 100));
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        drawEnemies(g2d, pos, x, y, level);
        drawFruits(g2d, pos, x, y, level);
    }

    private void drawPlayer(Graphics2D g2d, Player player, int x, int y) {
        Image playerImage = null;
        switch (player.getFlavor()) {
            case CHOCOLATE:
                playerImage = chocolateImage;
                break;
            case STRAWBERRY:
                playerImage = fresaImage;
                break;
            case VANILLA:
                playerImage = vainillaImage;
                break;
        }

        if (playerImage != null) {
            // Si el jugador está muerto, dibujarlo semitransparente
            if (!player.isAlive()) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2d.drawImage(playerImage, x, y, CELL_SIZE, CELL_SIZE, this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            } else {
                g2d.drawImage(playerImage, x, y, CELL_SIZE, CELL_SIZE, this);
            }
        } else {
            g2d.setColor(player.isAlive() ? Color.CYAN : Color.GRAY);
            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    private void drawEnemies(Graphics2D g2d, Position pos, int x, int y, Level level) {
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isAlive() && enemy.getPosition().equals(pos)) {
                Image enemyImage = getEnemyImage(enemy);
                if (enemyImage != null) {
                    g2d.drawImage(enemyImage, x, y, CELL_SIZE, CELL_SIZE, this);
                } else {
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                }
            }
        }
    }

    private Image getEnemyImage(Enemy enemy) {
        String className = enemy.getClass().getSimpleName();
        if (className.equals("Narwhal")) {
            return narvalImage;
        }
        switch (enemy.getEnemyType()) {
            case ORANGE_SQUID:
                return calamarImage;
            case POT:
                return macetaImage;
            case TROLL:
                return trollImage;
            default:
                return null;
        }
    }

    private void drawFruits(Graphics2D g2d, Position pos, int x, int y, Level level) {
        for (Fruit fruit : level.getFruits()) {
            if (fruit.getPosition().equals(pos) && !fruit.isCollected()) {
                Image fruitImage = getFruitImage(fruit);
                if (fruitImage != null) {
                    if (fruit.getFruitType().isMovable()) {
                        long time = System.currentTimeMillis();
                        int offset = (int) (Math.sin(time / 200.0) * 2);
                        g2d.drawImage(fruitImage, x + 2 + offset, y + 2 + offset,
                                CELL_SIZE - 4, CELL_SIZE - 4, this);
                    } else {
                        g2d.drawImage(fruitImage, x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, this);
                    }
                    if (fruit instanceof Cactus && ((Cactus) fruit).hasThorns()) {
                        drawThornsIndicator(g2d, x, y);
                    }
                } else {
                    g2d.setColor(Color.GREEN);
                    g2d.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                }
            }
        }
    }

    private Image getFruitImage(Fruit fruit) {
        if (fruit instanceof Cactus) {
            return ((Cactus) fruit).hasThorns() ? cactusPuasImage : cactusImage;
        }
        switch (fruit.getFruitType()) {
            case GRAPE:
                return uvaImage;
            case BANANA:
                return platanoImage;
            case CHERRY:
                return cerezaImage;
            case PINEAPPLE:
                return piñaImage;
            case CACTUS:
                return cactusImage;
            default:
                return defaulFruitImage;
        }
    }

    private void drawThornsIndicator(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(255, 0, 0, 100));
        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        long time = System.currentTimeMillis();
        if ((time / 500) % 2 == 0) {
            g2d.setColor(Color.RED);
            g2d.drawRect(x + 1, y + 1, CELL_SIZE - 3, CELL_SIZE - 3);
        }
    }

    private void drawStateOverlay(Graphics2D g2d) {
        GameState state = game.getState();
        String message = null;
        String subMessage = null;
        Color bgColor = null;

        switch (state) {
            case PAUSED:
                message = "PAUSADO";
                bgColor = new Color(0, 0, 0, 180);
                break;
            case GAME_OVER:
                message = "GAME OVER";
                bgColor = new Color(139, 0, 0, 200);
                subMessage = getGameOverMessage();
                break;
            case VICTORY:
                message = "¡VICTORIA!";
                bgColor = new Color(0, 128, 0, 200);
                subMessage = getVictoryMessage();
                break;
            default:
                return;
        }

        if (message != null) {
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(message);
            g2d.drawString(message, (getWidth() - msgWidth) / 2, getHeight() / 2);

            if (subMessage != null) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                fm = g2d.getFontMetrics();
                int subWidth = fm.stringWidth(subMessage);
                g2d.drawString(subMessage, (getWidth() - subWidth) / 2, getHeight() / 2 + 50);
            }

            String returnMsg = "Volviendo al menú...";
            g2d.setFont(new Font("Arial", Font.ITALIC, 18));
            fm = g2d.getFontMetrics();
            int returnWidth = fm.stringWidth(returnMsg);
            g2d.drawString(returnMsg, (getWidth() - returnWidth) / 2, getHeight() / 2 + 90);
        }
    }

    private String getGameOverMessage() {
        Player p1 = game.getPlayer();
        Player p2 = game.getPlayer2();

        if (game.getGameMode().isMultiplayer() && p1 != null && p2 != null) {
            return String.format("P1: %d | P2: %d", p1.getScore(), p2.getScore());
        } else if (p1 != null) {
            return "Puntos: " + p1.getScore();
        }
        return "";
    }

    private String getVictoryMessage() {
        Player p1 = game.getPlayer();
        Player p2 = game.getPlayer2();

        if (game.getGameMode().isMultiplayer() && p1 != null && p2 != null) {
            if (p1.getScore() > p2.getScore()) {
                return String.format("Ganador: P1 (%d pts)", p1.getScore());
            } else if (p2.getScore() > p1.getScore()) {
                return String.format("Ganador: P2 (%d pts)", p2.getScore());
            } else {
                return String.format("¡EMPATE! (%d pts)", p1.getScore());
            }
        } else if (p1 != null) {
            return "Puntos: " + p1.getScore();
        }
        return "";
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